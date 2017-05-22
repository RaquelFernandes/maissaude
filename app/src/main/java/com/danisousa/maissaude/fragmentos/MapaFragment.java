package com.danisousa.maissaude.fragmentos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.servicos.ApiEstabelecimentosInterface;
import com.danisousa.maissaude.servicos.TcuApi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap mMap;
    private ApiEstabelecimentosInterface mServico;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);

        mServico = TcuApi.getInstance().getServico();

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LOCALIZAÇÂO", "PERMISSÃO NEGADA");
            return;
        }

//        Call<List<Estabelecimento>> call = mServico.getEstabelecimentosPorCoordenadas(
//                mLocalizacao.getLatitude(),
//                mLocalizacao.getLongitude(),
//                100, // 100km de raio
//                "URGÊNCIA", // categoria
//                1 // quantidade de resultados
//        );
//
//        call.enqueue(new Callback<List<Estabelecimento>>() {
//            @Override
//            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
//                if (response == null) {
//                    onFailure(call, new Exception("Null response from API"));
//                    return;
//                }
//                List<Estabelecimento> estabelecimentos = response.body();
//                Log.i("EstAdapter", Integer.toString(response.body().size()));
//
//                Intent intent = new Intent(MainActivity.this, DetalhesActivity.class);
//                intent.putExtra(DetalhesActivity.EXTRA_ESTABELECIMENTO, estabelecimentos.get(0));
//                startActivity(intent);
//                mProgessEmergencia.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
//                t.printStackTrace();
//                mProgessEmergencia.dismiss();
//                Toast.makeText(MainActivity.this, "Erro ao tentar se comunicar com o servidor", Toast.LENGTH_SHORT).show();
//            }
//        });

        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Título").snippet("Descrição"));

        // For zooming automatically to the location of the marker
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target()
//                .zoom(12)
//                .build();

//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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