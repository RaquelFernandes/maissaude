package com.danisousa.maissaude.fragmentos;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.danisousa.maissaude.adaptadores.EstabelecimentosAdapter;
import com.danisousa.maissaude.R;
import com.danisousa.maissaude.atividades.MainActivity;
import com.danisousa.maissaude.modelos.Estabelecimento;
import com.danisousa.maissaude.utils.LocalizacaoHelper;

import java.util.ArrayList;
import java.util.List;

public class ProximosFragment extends Fragment implements LocalizacaoHelper.LocalizacaoListener {

    private MainActivity mMainActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EstabelecimentosAdapter mAdapter;
    private Location mLocalizacao;
    private List<Estabelecimento> mEstabelecimentos;

    private static final String ESTABELECIMENTOS = "Estabelecimentos";
    private static final String LOCALIZACAO = "Localização";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) this.getActivity();
        if (savedInstanceState != null) {
            mEstabelecimentos = (ArrayList<Estabelecimento>) savedInstanceState.getSerializable(ESTABELECIMENTOS);
            mLocalizacao = savedInstanceState.getParcelable(LOCALIZACAO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_view, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.azul_claro);
        mSwipeRefreshLayout.setOnRefreshListener(() -> mAdapter.atualizarProximos(mLocalizacao, mSwipeRefreshLayout));

        ProgressBar inicioProgressBar = (ProgressBar) view.findViewById(R.id.inicio_progress_bar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        if (mEstabelecimentos != null) {
            mAdapter = new EstabelecimentosAdapter(getActivity(), mEstabelecimentos, mLocalizacao);
            inicioProgressBar.setVisibility(View.GONE);
        } else {
            mAdapter = new EstabelecimentosAdapter(getActivity(), inicioProgressBar);
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration separador = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            separador.setDrawable(getContext().getDrawable(R.drawable.separador_lista));
        }
        recyclerView.addItemDecoration(separador);

        mMainActivity.addLocalizacaoListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Estabelecimento> estabelecimentos = (ArrayList<Estabelecimento>) mAdapter.getEstabelecimentos();
        outState.putSerializable(ESTABELECIMENTOS, estabelecimentos);
        outState.putParcelable(LOCALIZACAO, mLocalizacao);
    }

    @Override
    public void onLocalizacaoChanged(Location localizacao) {
        mLocalizacao = localizacao;
        mAdapter.atualizarProximos(mLocalizacao);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
