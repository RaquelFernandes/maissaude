package com.danisousa.maissaude.adaptadores;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.atividades.DetalhesActivity;
import com.danisousa.maissaude.dados.FavoritosDAO;
import com.danisousa.maissaude.fragmentos.FavoritosFragment;
import com.danisousa.maissaude.fragmentos.ProximosFragment;
import com.danisousa.maissaude.modelos.Estabelecimento;
import com.danisousa.maissaude.servicos.ApiEstabelecimentosInterface;
import com.danisousa.maissaude.servicos.TcuApi;
import com.danisousa.maissaude.utils.ClipboardHelper;
import com.danisousa.maissaude.utils.Acoes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.danisousa.maissaude.utils.Acoes.ABRIR_NO_GMAPS;
import static com.danisousa.maissaude.utils.Acoes.ADICIONAR_OU_REMOVER_FAVORITOS;
import static com.danisousa.maissaude.utils.Acoes.COMPARTILHAR;
import static com.danisousa.maissaude.utils.Acoes.COPIAR_ENDEREÇO;
import static com.danisousa.maissaude.utils.Acoes.COPIAR_TELEFONE;
import static com.danisousa.maissaude.utils.Acoes.LIGAR;

public class EstabelecimentosAdapter extends RecyclerView.Adapter<EstabelecimentosAdapter.EstabelecimentoViewHolder> {

    private Context mContext;
    private ApiEstabelecimentosInterface mServico;
    private List<Estabelecimento> mEstabelecimentos;
    private Location mLocalizacao;
    private ProgressBar mInicioProgressBar;
    private Class mFragmentClass;

    private static final String TAG = "EstabelecimentosAdapter";

    public class EstabelecimentoViewHolder extends RecyclerView.ViewHolder {

        public TextView nome, tipo, distancia;

        public EstabelecimentoViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.nomeFantasia);
            tipo = (TextView) view.findViewById(R.id.tipoUnidade);
            distancia = (TextView) view.findViewById(R.id.distancia);
        }
    }

    private EstabelecimentosAdapter(Context context) {
        mContext = context;
        mEstabelecimentos = new ArrayList<>();
        mServico = TcuApi.getInstance().getServico();
    }

    public EstabelecimentosAdapter(Context context, ProgressBar inicioProgressBar) {
        this(context);
        mInicioProgressBar = inicioProgressBar;
    }

    public EstabelecimentosAdapter(Context context, List<Estabelecimento> estabelecimentos, Location localizacao) {
        this(context);
        mLocalizacao = localizacao;
        mEstabelecimentos = estabelecimentos;
        notifyDataSetChanged();
    }

    public void atualizarProximos(Location localizacao) {
        atualizarProximos(localizacao, null);
    }

    public void atualizarProximos(Location localizacao, final SwipeRefreshLayout swipeRefreshLayout) {
        mFragmentClass = ProximosFragment.class;
        mLocalizacao = localizacao;

        Call<List<Estabelecimento>> call = mServico.getEstabelecimentosPorCoordenadas(
                mLocalizacao.getLatitude(),
                mLocalizacao.getLongitude(),
                100, // 100km de raio
                null, // categoria. null = todas
                100 // quantidade de resultados
        );

        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if (response.body() == null) {
                    onFailure(call, new NetworkErrorException("Null response from API"));
                    return;
                }

                EstabelecimentosAdapter.this.mEstabelecimentos = response.body();
                Log.i(TAG, "Estabelecimentos: " + Integer.toString(response.body().size()));
                notifyDataSetChanged();

                if (mInicioProgressBar != null) {
                    mInicioProgressBar.setVisibility(View.GONE);
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(EstabelecimentosAdapter.this.mContext, R.string.erro_servidor, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void atualizarFavoritos(Location localizacao, List<Estabelecimento> estabelecimentos) {
        mFragmentClass = FavoritosFragment.class;
        mLocalizacao = localizacao;
        mEstabelecimentos = estabelecimentos;
        notifyDataSetChanged();
        Log.d(TAG, "Estabelecimentos: " + mEstabelecimentos.size());
    }

    @Override
    public EstabelecimentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false);

        return new EstabelecimentoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EstabelecimentoViewHolder holder, int position) {
        final int index = position;

        Estabelecimento estabelecimento = mEstabelecimentos.get(position);
        holder.nome.setText(estabelecimento.getNomeFantasia());
        holder.tipo.setText(estabelecimento.getTipoUnidade());
        holder.distancia.setText(estabelecimento.getDistanciaFormatada(mLocalizacao.getLatitude(), mLocalizacao.getLongitude()));
        holder.itemView.setOnClickListener(v -> onClickViewHolder(v, index));
        holder.itemView.setOnLongClickListener(v -> onLongClickViewHolder(v, index));
    }

    @Override
    public int getItemCount() {
        return mEstabelecimentos.size();
    }

    public Estabelecimento getItem(int position) {
        return mEstabelecimentos.get(position);
    }

    private void onClickViewHolder(View view, int position) {
        Context context = view.getContext();

        Estabelecimento estabelecimento = getItem(position);
        Intent it = new Intent(context, DetalhesActivity.class);
        it.putExtra(DetalhesActivity.EXTRA_ESTABELECIMENTO, estabelecimento);
        context.startActivity(it);
    }

    private boolean onLongClickViewHolder(View view, int position) {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

        final Context context = view.getContext();

        final Estabelecimento estabelecimento = getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(estabelecimento.getNomeFantasia());
        builder.setItems(Acoes.getLongPressNames(mFragmentClass), (dialog, item) -> {
            switch (item) {
                case LIGAR:
                    Acoes.ligar(mContext, estabelecimento);
                    return;
                case ABRIR_NO_GMAPS:
                    Acoes.abrirMapa(mContext, estabelecimento);
                    return;
                case COMPARTILHAR:
                    Acoes.compartilharTexto(mContext, estabelecimento);
                    return;
                case COPIAR_TELEFONE:
                    ClipboardHelper.copiarTexto(mContext, estabelecimento.getTelefone());
                    return;
                case COPIAR_ENDEREÇO:
                    ClipboardHelper.copiarTexto(mContext, estabelecimento.getEndereco());
                    return;
                case ADICIONAR_OU_REMOVER_FAVORITOS:
                    Activity activity = (Activity) mContext;
                    View coordinatorLayout = activity.findViewById(R.id.coordinator_main);
                    String nome = estabelecimento.getNomeFantasia();

                    if (mFragmentClass == ProximosFragment.class) {
                        FavoritosDAO.getInstance().salvar(estabelecimento);
                        Snackbar.make(coordinatorLayout, activity.getString(R.string.add_aos_favoritos, nome), Snackbar.LENGTH_LONG)
                                .setAction(R.string.snackbar_desfazer, v -> FavoritosDAO.getInstance().remover(estabelecimento)).show();

                    } else if (mFragmentClass == FavoritosFragment.class) {
                        FavoritosDAO.getInstance().remover(estabelecimento);
                        Snackbar.make(coordinatorLayout, activity.getString(R.string.remover_dos_favoritos, nome), Snackbar.LENGTH_LONG)
                                .setAction(R.string.snackbar_desfazer, v -> FavoritosDAO.getInstance().salvar(estabelecimento)).show();
                    }
            }
        });
        builder.show();
        return true;
    }

    public List<Estabelecimento> getEstabelecimentos() {
        return mEstabelecimentos;
    }

}