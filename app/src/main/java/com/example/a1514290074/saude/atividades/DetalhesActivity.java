package com.example.a1514290074.saude.atividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.a1514290074.saude.R;
import com.example.a1514290074.saude.modelos.Movie;

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        TextView nome = (TextView) findViewById(R.id.detalhes_tv_nome);
        TextView genero = (TextView) findViewById(R.id.detalhes_tv_genero);
        TextView ano = (TextView) findViewById(R.id.detalhes_tv_ano);

        Movie filme = (Movie) getIntent().getSerializableExtra("filme");

        nome.setText(filme.getTitle());
        genero.setText(filme.getGenre());
        ano.setText(filme.getYear());

    }
}
