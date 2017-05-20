package com.danisousa.maissaude.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.atividades.DetalhesActivity;
import com.danisousa.maissaude.modelos.Estabelecimento;
import com.danisousa.maissaude.servicos.ApiEstabelecimentosInterface;
import com.danisousa.maissaude.utils.ClipboardHelper;
import com.danisousa.maissaude.utils.IntentHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstabelecimentosAdapter extends RecyclerView.Adapter<EstabelecimentosAdapter.EstabelecimentoViewHolder> {

    private Context mContext;
    private ApiEstabelecimentosInterface mServico;
    private List<Estabelecimento> mEstabelecimentos;

    private Location mLocalizacao;

    private ProgressBar mInicioProgressBar;
    private ProgressBar mInifiniteScrollProgressBar;

    public class EstabelecimentoViewHolder extends RecyclerView.ViewHolder {

        public TextView nome, tipo, distancia;

        public EstabelecimentoViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.nomeFantasia);
            tipo = (TextView) view.findViewById(R.id.tipoUnidade);
            distancia = (TextView) view.findViewById(R.id.distancia);
        }
    }

    public EstabelecimentosAdapter(Context context, ProgressBar inicioProgressBar) {
        mContext = context;
        mInicioProgressBar = inicioProgressBar;
        mEstabelecimentos = new ArrayList<>();
    }

    public void atualizarProximos(Location localizacao) {
        atualizarProximos(localizacao, null);
    }

    public void atualizarProximos(Location localizacao, final SwipeRefreshLayout swipeRefreshLayout) {
        mLocalizacao = localizacao;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiEstabelecimentosInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mServico = retrofit.create(ApiEstabelecimentosInterface.class);

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
                if (response == null) {
                    onFailure(call, new Exception("Null response from API"));
                    return;
                }
                EstabelecimentosAdapter.this.mEstabelecimentos = response.body();
                Log.i("EstAdapter", Integer.toString(response.body().size()));
                notifyDataSetChanged();
                mInicioProgressBar.setVisibility(View.GONE);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(EstabelecimentosAdapter.this.mContext, "Erro ao tentar se comunicar com o servidor", Toast.LENGTH_SHORT).show();
            }
        });
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
        holder.distancia.setText(estabelecimento.getDistancia(mLocalizacao.getLatitude(), mLocalizacao.getLongitude()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickViewHolder(v, index);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onLongClickViewHolder(v, index);
            }
        });
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
        final Context context = view.getContext();

        final int LIGAR = 0;
        final int ABRIR_NO_GMAPS = 1;
        final int COMPARTILHAR = 2;
        final int COPIAR_TELEFONE = 3;
        final int COPIAR_ENDEREÇO = 4;
        final int ADICIONAR_AOS_FAVORITOS = 5;

        final CharSequence[] items = {
                "Ligar",
                "Abrir no Mapa",
                "Compartilhar",
                "Copiar Telefone",
                "Copiar Endereço",
                "Adicionar aos Favoritos"
        };

        final Estabelecimento estabelecimento = getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(estabelecimento.getNomeFantasia());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case LIGAR:
                        IntentHelper.ligar(mContext, estabelecimento);
                        return;
                    case ABRIR_NO_GMAPS:
                        IntentHelper.abrirMapa(mContext, estabelecimento);
                        return;
                    case COMPARTILHAR:
                        IntentHelper.compartilharTexto(mContext, estabelecimento);
                        return;
                    case COPIAR_TELEFONE:
                        ClipboardHelper.copiarTexto(mContext, estabelecimento.getTelefone());
//                        Toast.makeText(context, "Telefone copiado para a área de transferência", Toast.LENGTH_SHORT).show();
                        return;
                    case COPIAR_ENDEREÇO:
                        ClipboardHelper.copiarTexto(mContext, estabelecimento.getEndereco());
//                        Toast.makeText(context, "Endereço copiado para a área de transferência", Toast.LENGTH_SHORT).show();
                        return;
                    case ADICIONAR_AOS_FAVORITOS:
                        Toast.makeText(context, "Adicionar aos Favoritos", Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        Toast.makeText(context, "Nenhum", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
        return true;
    }

}