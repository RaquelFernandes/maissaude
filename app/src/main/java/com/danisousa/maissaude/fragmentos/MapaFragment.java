package com.danisousa.maissaude.fragmentos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.atividades.DetalhesActivity;
import com.danisousa.maissaude.atividades.MainActivity;
import com.danisousa.maissaude.modelos.Estabelecimento;
import com.danisousa.maissaude.servicos.ApiEstabelecimentosInterface;
import com.danisousa.maissaude.servicos.TcuApi;
import com.danisousa.maissaude.utils.Cluster;
import com.danisousa.maissaude.utils.ClusterRenderer;
import com.danisousa.maissaude.utils.LocalizacaoHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapaFragment extends Fragment implements OnMapReadyCallback, LocalizacaoHelper.LocalizacaoListener {

    private MainActivity mMainActivity;
    private FrameLayout mMapProgress;
    private MapView mMapView;
    private GoogleMap mMap;
    private ApiEstabelecimentosInterface mServico;
    private Location mLocalizacao;
    private List<Estabelecimento> mEstabelecimentos;
    private ClusterManager<Cluster> mClusterManager;
    private CameraPosition mPosicaoCamera;

    private static final String TAG = "MapaFragment";
    private static final String ESTABALECIMENTOS = "Estabelecimentos";
    private static final String POSICAO_CAMERA = "PosicaoCamera";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) this.getActivity();
        if (savedInstanceState != null) {
            mEstabelecimentos = (ArrayList<Estabelecimento>) savedInstanceState.getSerializable(ESTABALECIMENTOS);
            mPosicaoCamera = savedInstanceState.getParcelable(POSICAO_CAMERA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        mServico = TcuApi.getInstance().getServico();

        mMapProgress = (FrameLayout) view.findViewById(R.id.map_progress);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        mMainActivity.addLocalizacaoListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ESTABALECIMENTOS, (ArrayList<Estabelecimento>) mEstabelecimentos);
        outState.putParcelable(POSICAO_CAMERA, mMap.getCameraPosition());
    }

    private void carregarEstabelecimentos() {
        Call<List<Estabelecimento>> call = mServico.getEstabelecimentosPorCoordenadas(
                mLocalizacao.getLatitude(),
                mLocalizacao.getLongitude(),
                100, // 100km de raio
                null, // categoria
                100 // quantidade de resultados
        );

        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if (response == null) {
                    onFailure(call, new Exception("Null response from API"));
                    return;
                }
                mEstabelecimentos = response.body();
                Log.i(TAG, "Estabelecimentos carregados: " + Integer.toString(response.body().size()));
                configurarMapa();
            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MapaFragment.this.getActivity(), "Erro ao tentar se comunicar com o servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarMapa() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "PERMISSÃO NEGADA");
            return;
        }

        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        configurarClusters();
        configurarCamera();

        Log.d(TAG, "Mapa configurado");
    }

    private void configurarClusters() {
        mClusterManager = new ClusterManager<>(getActivity(), mMap);
        mClusterManager.setRenderer(new ClusterRenderer(getActivity(), mMap, mClusterManager));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterItemInfoWindowClickListener(cluster -> {
            Context context = MapaFragment.this.getActivity();
            Estabelecimento estabelecimento = cluster.getEstabelecimento();
            Intent it = new Intent(context, DetalhesActivity.class);
            it.putExtra(DetalhesActivity.EXTRA_ESTABELECIMENTO, estabelecimento);
            context.startActivity(it);
        });
        adicionarMarcadores();
    }

    private void adicionarMarcadores() {
        for (Estabelecimento estabelecimento : mEstabelecimentos) {
            Cluster item = new Cluster(estabelecimento);
            mClusterManager.addItem(item);
        }
    }

    private void configurarCamera() {
        CameraPosition posicaoCamera = (mPosicaoCamera != null) ? mPosicaoCamera : new CameraPosition.Builder()
                .target(new LatLng(mLocalizacao.getLatitude(), mLocalizacao.getLongitude()))
                .zoom(12)
                .build();
        if (mPosicaoCamera != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(posicaoCamera));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(posicaoCamera));
        }
    }

    @Override
    public void onLocalizacaoChanged(Location localizacao) {
        if (mLocalizacao == null) {
            mLocalizacao = localizacao;
            Log.d(TAG, "Localização: " + mLocalizacao.toString());
            if (mEstabelecimentos == null) {
                carregarEstabelecimentos();
            } else {
                configurarMapa();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Mapa carregado");
        mMap = googleMap;
        mMapProgress.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}