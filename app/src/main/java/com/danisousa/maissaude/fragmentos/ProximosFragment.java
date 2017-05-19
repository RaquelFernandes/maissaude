package com.danisousa.maissaude.fragmentos;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.danisousa.maissaude.adaptadores.EstabelecimentosAdapter;
import com.danisousa.maissaude.R;
import com.danisousa.maissaude.utils.LocalizacaoHelper;
import com.google.android.gms.maps.model.LatLng;

public class ProximosFragment extends Fragment implements LocationListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mInicioProgressBar;
    private RecyclerView mRecyclerView;
    private EstabelecimentosAdapter mAdapter;

    private LatLng mLocalizacao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.azul_claro);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.atualizarProximos(mLocalizacao, mSwipeRefreshLayout);
            }
        });

        mInicioProgressBar = (ProgressBar) view.findViewById(R.id.inicio_progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new EstabelecimentosAdapter(getActivity(), mInicioProgressBar);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration separador = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            separador.setDrawable(getContext().getDrawable(R.drawable.separador_lista));
        }
        mRecyclerView.addItemDecoration(separador);

        LocalizacaoHelper.getLocalizacao(getActivity(), this);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LocalizacaoHelper.REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("LOCALIZAÇÂO", "Permitido: " + grantResults.toString());
                    LocalizacaoHelper.getLocalizacao(getActivity(), this);
                } else {
                    Log.d("LOCALIZAÇÂO", "Não permitido: " + grantResults.toString());
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.main_dlg_titulo_localizacao_negada)
                            .setMessage(R.string.main_dlg_mensagem_localizacao_negada)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LocalizacaoHelper.getLocalizacao(getActivity(), ProximosFragment.this);
                                }
                            })
                            .show();
                }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocalizacao = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("LOCALIZAÇÂO", "Nova localização: " + mLocalizacao.toString());
        mAdapter.atualizarProximos(mLocalizacao);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
