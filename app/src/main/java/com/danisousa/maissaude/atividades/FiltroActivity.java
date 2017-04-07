package com.danisousa.maissaude.atividades;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.danisousa.maissaude.R;

public class FiltroActivity extends AppCompatActivity {

    private static final String EXTRA_FILTROS = "filtros";

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_aplicar:
                aplicar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void aplicar() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_FILTROS, "teste");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
