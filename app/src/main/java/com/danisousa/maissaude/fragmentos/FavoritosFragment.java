package com.danisousa.maissaude.fragmentos;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.danisousa.maissaude.atividades.MainActivity;
import com.danisousa.maissaude.dados.FavoritosDAO;
import com.danisousa.maissaude.modelos.Estabelecimento;
import com.danisousa.maissaude.adaptadores.EstabelecimentosAdapter;
import com.danisousa.maissaude.R;
import com.danisousa.maissaude.utils.LocalizacaoHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritosFragment extends Fragment implements FavoritosDAO.FavoritosListener, LocalizacaoHelper.LocalizacaoListener, EstabelecimentosAdapter.AtualizarEstablecimentos {

    private MainActivity mMainActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mInicioProgressBar;
    private RecyclerView mRecyclerView;
    private LinearLayout mListaVazia;
    private EstabelecimentosAdapter mAdapter;
    private Location mLocalizacao;
    private List<Estabelecimento> mEstabelecimentos = new ArrayList<>();

    private static final String TAG = "FavoritosFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_view, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setEnabled(false);

        mInicioProgressBar = (ProgressBar) view.findViewById(R.id.inicio_progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mListaVazia = (LinearLayout) view.findViewById(R.id.lista_vazia);

        mAdapter = new EstabelecimentosAdapter(mMainActivity, this);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration separador = new DividerItemDecoration(mMainActivity, DividerItemDecoration.VERTICAL);
        separador.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.separador_lista));

        mRecyclerView.addItemDecoration(separador);

        mMainActivity.addLocalizacaoListener(this);
        FavoritosDAO.getInstance().setFavoritosListener(this);

        return view;
    }

    @Override
    public void onLocalizacaoChanged(Location localizacao) {
        mLocalizacao = localizacao;
        mAdapter.atualizarEstabelecimentos();
    }

    @Override
    public void onFavoritosChanged(List<Estabelecimento> estabelecimentos) {
        mEstabelecimentos = estabelecimentos;
        mAdapter.atualizarEstabelecimentos();
    }

    @Override
    public void atualizarEstabelecimentos() {
        if (mEstabelecimentos != null) {
            mListaVazia.setVisibility(mEstabelecimentos.size() > 0 ? View.GONE : View.VISIBLE);
        }

        if (mLocalizacao != null) {
            Collections.sort(mEstabelecimentos, (est1, est2) ->
                    est1.getDistancia(mLocalizacao.getLatitude(), mLocalizacao.getLongitude())
                    .compareTo(est2.getDistancia(mLocalizacao.getLatitude(), mLocalizacao.getLongitude())));
        }

        mAdapter.setFragmentClass(FavoritosFragment.class);
        mAdapter.setLocalizacao(mLocalizacao);
        mAdapter.setEstabelecimentos(mEstabelecimentos);
        mAdapter.notifyDataSetChanged();
        mInicioProgressBar.setVisibility(View.GONE);
    }
}
