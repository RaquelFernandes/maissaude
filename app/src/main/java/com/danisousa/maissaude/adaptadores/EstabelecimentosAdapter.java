package com.danisousa.maissaude.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.atividades.DetalhesActivity;
import com.danisousa.maissaude.dados.FavoritosDAO;
import com.danisousa.maissaude.fragmentos.FavoritosFragment;
import com.danisousa.maissaude.modelos.Estabelecimento;
import com.danisousa.maissaude.utils.ClipboardHelper;
import com.danisousa.maissaude.utils.Acoes;

import java.util.ArrayList;
import java.util.List;

import static com.danisousa.maissaude.utils.Acoes.ABRIR_NO_GMAPS;
import static com.danisousa.maissaude.utils.Acoes.ADICIONAR_OU_REMOVER_FAVORITOS;
import static com.danisousa.maissaude.utils.Acoes.COMPARTILHAR;
import static com.danisousa.maissaude.utils.Acoes.COPIAR_ENDEREÇO;
import static com.danisousa.maissaude.utils.Acoes.COPIAR_TELEFONE;
import static com.danisousa.maissaude.utils.Acoes.LIGAR;

public class EstabelecimentosAdapter extends RecyclerView.Adapter<EstabelecimentosAdapter.EstabelecimentoViewHolder> {

    private Context mContext;
    private Location mLocalizacao;
    private List<Estabelecimento> mEstabelecimentos;
    private AtualizarEstablecimentos mAtualizarInterface;
    private Class mFragmentClass;

    private static final String TAG = "EstabelecimentosAdapter";

    public interface AtualizarEstablecimentos {
        void atualizarEstabelecimentos();
    }

    public class EstabelecimentoViewHolder extends RecyclerView.ViewHolder {

        public TextView nome, tipo, distancia;

        public EstabelecimentoViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.nomeFantasia);
            tipo = (TextView) view.findViewById(R.id.tipoUnidade);
            distancia = (TextView) view.findViewById(R.id.distancia);
        }
    }

    public EstabelecimentosAdapter(Context context, AtualizarEstablecimentos atualizarInterface) {
        mContext = context;
        mEstabelecimentos = new ArrayList<>();
        mAtualizarInterface = atualizarInterface;
    }

    public void atualizarEstabelecimentos() {
        if (mAtualizarInterface != null) {
            mAtualizarInterface.atualizarEstabelecimentos();
        }
    }

    @Override
    public EstabelecimentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new EstabelecimentoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EstabelecimentoViewHolder holder, int position) {
        final int index = position;

        Estabelecimento estabelecimento = mEstabelecimentos.get(position);
        holder.nome.setText(estabelecimento.getNomeFantasia());
        holder.tipo.setText(estabelecimento.getTipoUnidade());
        holder.itemView.setOnClickListener(v -> onClickViewHolder(v, index));
        holder.itemView.setOnLongClickListener(v -> onLongClickViewHolder(v, index));
        if (mLocalizacao != null) {
            holder.distancia.setText(estabelecimento.getDistanciaFormatada(mLocalizacao.getLatitude(), mLocalizacao.getLongitude()));
        }
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

                    if (mFragmentClass == FavoritosFragment.class) {
                        FavoritosDAO.getInstance().remover(estabelecimento);
                        Snackbar.make(coordinatorLayout, activity.getString(R.string.remover_dos_favoritos, nome), Snackbar.LENGTH_LONG)
                                .setAction(R.string.snackbar_desfazer, v -> FavoritosDAO.getInstance().salvar(estabelecimento)).show();
                    } else {
                        FavoritosDAO.getInstance().salvar(estabelecimento);
                        Snackbar.make(coordinatorLayout, activity.getString(R.string.add_aos_favoritos, nome), Snackbar.LENGTH_LONG)
                                .setAction(R.string.snackbar_desfazer, v -> FavoritosDAO.getInstance().remover(estabelecimento)).show();
                    }
            }
        });
        builder.show();
        return true;
    }

    public Location getLocalizacao() {
        return mLocalizacao;
    }

    public void setLocalizacao(Location localizacao) {
        mLocalizacao = localizacao;
    }

    public List<Estabelecimento> getEstabelecimentos() {
        return mEstabelecimentos;
    }

    public void setEstabelecimentos(List<Estabelecimento> estabelecimentos) {
        mEstabelecimentos = estabelecimentos;
    }

    public Class getFragmentClass() {
        return mFragmentClass;
    }

    public void setFragmentClass(Class fragmentClass) {
        mFragmentClass = fragmentClass;
    }
}