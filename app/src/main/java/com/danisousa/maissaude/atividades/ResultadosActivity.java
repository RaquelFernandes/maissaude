package com.danisousa.maissaude.atividades;

import android.location.Location;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.adaptadores.EstabelecimentosAdapter;
import com.danisousa.maissaude.fragmentos.MapaFragment;
import com.danisousa.maissaude.modelos.Estabelecimento;
import com.danisousa.maissaude.modelos.Filtro;
import com.danisousa.maissaude.servicos.ApiEstabelecimentosInterface;
import com.danisousa.maissaude.servicos.TcuApi;
import com.danisousa.maissaude.utils.LocalizacaoHelper;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultadosActivity extends AppCompatActivity {

    private ProgressBar mInicioProgressBar;
    private RecyclerView mRecyclerView;
    private LinearLayout mListaVazia;
    private TextView mListaVaziaMensagem;
    private ApiEstabelecimentosInterface mServico;
    private EstabelecimentosAdapter mAdapter;
    private Filtro mFiltros;
    private Location mLocalizacao;

    private static final String TAG = "ResultadosAvtivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        mServico = TcuApi.getInstance().getServico();
        mLocalizacao = getIntent().getParcelableExtra(LocalizacaoHelper.LOCALIZACAO_EXTRA);
        mFiltros = (Filtro) getIntent().getSerializableExtra(BuscarActivity.EXTRA_FILTROS);

        setupView();
        carregarEstabelecimentos();
    }

    private void setupView() {
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setEnabled(false);

        mInicioProgressBar = (ProgressBar) findViewById(R.id.inicio_progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mListaVazia = (LinearLayout) findViewById(R.id.lista_vazia);
        mListaVaziaMensagem = (TextView) findViewById(R.id.lista_vazia_mensagem);

        mAdapter = new EstabelecimentosAdapter(this, mInicioProgressBar);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration separador = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            separador.setDrawable(getDrawable(R.drawable.separador_lista));
        }

        mRecyclerView.addItemDecoration(separador);
    }

    private void carregarEstabelecimentos() {
        Call<List<Estabelecimento>> call = mServico.getTodosEstabelecimentos(
                mFiltros.getNome(),
                mFiltros.getCidade(),
                mFiltros.getEstado(),
                mFiltros.getCategoria(),
                mFiltros.getSus(),
                mFiltros.getRetencao()
        );

        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if (response.body() == null) {
                    onFailure(call, new Exception("Null response from API"));
                    return;
                }
                if (response.body().size() == 0) {
                    onNotFound();
                    return;
                }
                List<Estabelecimento> estabelecimentos = response.body();
                Log.d(TAG, "Resposta da API: " + estabelecimentos.toString());

                if (mLocalizacao != null) {
                    Collections.sort(estabelecimentos, (est1, est2) ->
                            est1.getDistancia(mLocalizacao.getLatitude(), mLocalizacao.getLongitude())
                            .compareTo(est2.getDistancia(mLocalizacao.getLatitude(), mLocalizacao.getLongitude())));
                }

                mAdapter = new EstabelecimentosAdapter(ResultadosActivity.this, estabelecimentos, mLocalizacao);
                mRecyclerView.setAdapter(mAdapter);
                mInicioProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ResultadosActivity.this, "Erro ao tentar se comunicar com o servidor", Toast.LENGTH_SHORT).show();
                finish();
            }

            public void onNotFound() {
                mListaVaziaMensagem.setText("Nenhum resultado encontrado");
                mListaVazia.setVisibility(View.VISIBLE);
                mInicioProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
