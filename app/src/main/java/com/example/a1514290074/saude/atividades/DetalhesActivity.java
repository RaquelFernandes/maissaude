package com.example.a1514290074.saude.atividades;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1514290074.saude.R;
import com.example.a1514290074.saude.modelos.Movie;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;

public class DetalhesActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Movie filme = (Movie) getIntent().getSerializableExtra("filme");

        setupCoordinatorLayout(filme.getTitle());

        TextView nome = (TextView) findViewById(R.id.detalhes_tv_nome);
        TextView genero = (TextView) findViewById(R.id.detalhes_tv_genero);
        TextView ano = (TextView) findViewById(R.id.detalhes_tv_ano);

        nome.setText(filme.getTitle());
        genero.setText(filme.getGenre());
        ano.setText(filme.getYear());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        UiSettings mapConfig = map.getUiSettings();
        mapConfig.setCompassEnabled(false);
        mapConfig.setMapToolbarEnabled(false);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(-15.6197972, -47.6512968))
                .title("Marker"));

    }

    private void setupCoordinatorLayout(String titulo) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(titulo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) findViewById(R.id.appbar).getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);
    }
}
