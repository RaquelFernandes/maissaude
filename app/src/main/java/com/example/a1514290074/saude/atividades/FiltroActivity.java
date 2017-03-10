package com.example.a1514290074.saude.atividades;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a1514290074.saude.R;

public class FiltroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filtro, menu);
        return true;
    }

    public void aplicar(MenuItem item) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("dados", "teste");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
