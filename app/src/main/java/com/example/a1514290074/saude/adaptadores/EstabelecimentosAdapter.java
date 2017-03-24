package com.example.a1514290074.saude.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1514290074.saude.R;
import com.example.a1514290074.saude.atividades.DetalhesActivity;
import com.example.a1514290074.saude.modelos.Estabelecimento;

import java.util.List;

public class EstabelecimentosAdapter extends RecyclerView.Adapter<EstabelecimentosAdapter.MyViewHolder> {

    private List<Estabelecimento> mMoviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, genre, distancia;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.nomeFantasia);
            genre = (TextView) view.findViewById(R.id.tipoUnidade);
            distancia = (TextView) view.findViewById(R.id.distancia);
        }
    }


    public EstabelecimentosAdapter(List<Estabelecimento> moviesList) {
        this.mMoviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linha_lista_estabelecimentos, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int index = position;

        Estabelecimento estabelecimento = mMoviesList.get(position);
        holder.title.setText(estabelecimento.getNomeFantasia());
        holder.genre.setText(estabelecimento.getTipoUnidade());
        holder.distancia.setText("25km");

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
        return mMoviesList.size();
    }

    public Estabelecimento getItem(int position) {
        return mMoviesList.get(position);
    }

    private void onClickViewHolder(View view, int position) {
        Context context = view.getContext();

        Estabelecimento filme = getItem(position);
        Intent it = new Intent(context, DetalhesActivity.class);
        it.putExtra("filme", filme);
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
                "Abrir no Google Maps",
                "Compartilhar",
                "Copiar Telefone",
                "Copiar Endereço",
                "Adicionar aos Favoritos"
        };

        Estabelecimento filme = getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(filme.getNomeFantasia());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case LIGAR:
                        Toast.makeText(context, "Ligar", Toast.LENGTH_SHORT).show();
                        return;
                    case ABRIR_NO_GMAPS:
                        Toast.makeText(context, "Abrir no Google Maps", Toast.LENGTH_SHORT).show();
                        return;
                    case COMPARTILHAR:
                        Toast.makeText(context, "Compartilhar", Toast.LENGTH_SHORT).show();
                        return;
                    case COPIAR_TELEFONE:
                        Toast.makeText(context, "Copiar Telefone", Toast.LENGTH_SHORT).show();
                        return;
                    case COPIAR_ENDEREÇO:
                        Toast.makeText(context, "Copiar Endereço", Toast.LENGTH_SHORT).show();
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