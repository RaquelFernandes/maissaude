package com.example.a1514290074.saude.listeners;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Toast;

import com.example.a1514290074.saude.adaptadores.MoviesAdapter;
import com.example.a1514290074.saude.atividades.DetalhesActivity;
import com.example.a1514290074.saude.modelos.Movie;

public class RecyclerClickListener implements ClickListener {

    private Context mContext;
    private MoviesAdapter mAdapter;

    public RecyclerClickListener(Context context, MoviesAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
    }

    @Override
    public void onClick(View view, int position) {
        Movie filme = mAdapter.getItem(position);
        Intent it = new Intent(mContext, DetalhesActivity.class);
        it.putExtra("filme", filme);
        mContext.startActivity(it);
    }

    @Override
    public void onLongClick(View view, int position) {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

        final int ADICIONAR_AOS_FAVORITOS = 0;
        final int ABRIR_NO_GMAPS = 1;
        final int COMPARTILHAR = 2;

        final CharSequence[] items = {
                "Ligar",
                "Abrir no Google Maps",
                "Enviar rotas ao Google Maps",
                "Adicionar aos Favoritos",
                "Compartilhar"
        };

        Movie filme = mAdapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(filme.getTitle());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case ADICIONAR_AOS_FAVORITOS:
                        Toast.makeText(mContext, "Adicionar aos Favoritos", Toast.LENGTH_SHORT).show();
                        return;
                    case ABRIR_NO_GMAPS:
                        Toast.makeText(mContext, "Abrir no Google Maps", Toast.LENGTH_SHORT).show();
                        return;
                    case COMPARTILHAR:
                        Toast.makeText(mContext, "Compartilhar", Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        Toast.makeText(mContext, "Nenhum", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
}
