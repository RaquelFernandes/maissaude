package com.example.a1514290074.saude.atividades;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.a1514290074.saude.R;
import com.example.a1514290074.saude.modelos.Estabelecimento;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetalhesActivity extends AppCompatActivity implements OnMapReadyCallback {

    Estabelecimento mEstabelecimento;
    AppBarLayout mAppBarLayout;
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        mEstabelecimento = (Estabelecimento) getIntent().getSerializableExtra("filme");

        setupCoordinatorLayout(mEstabelecimento.getNomeFantasia());

        setupFABBehavior();

//        TextView nome = (TextView) findViewById(R.id.detalhes_tv_nome);
//        TextView genero = (TextView) findViewById(R.id.detalhes_tv_genero);
//        TextView ano = (TextView) findViewById(R.id.detalhes_tv_ano);
//
//        nome.setText(mEstabelecimento.getNomeFantasia());
//        genero.setText(mEstabelecimento.getTipoUnidade());
//        ano.setText(mEstabelecimento.getRetencao());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.detalhes_mapa);
        mapFragment.getMapAsync(this);
    }

    private void setupFABBehavior() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_rotas);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // Not collapsed
                if (verticalOffset == 0) {
                    mFloatingActionButton.show();
                }
                // Collapsed
                else {
                    mFloatingActionButton.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                        @Override
                        public void onHidden(FloatingActionButton fab) {
                            super.onShown(fab);
                            fab.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        UiSettings mapConfig = map.getUiSettings();
        mapConfig.setCompassEnabled(false);
        mapConfig.setMapToolbarEnabled(false);

        LatLng posicao = new LatLng(mEstabelecimento.getLatitude(), mEstabelecimento.getLongitude());

        CameraPosition posicaoDaCamera = new CameraPosition.Builder()
                .target(posicao)
                .zoom(16)
                .build();

        map.moveCamera(CameraUpdateFactory.newCameraPosition(posicaoDaCamera));
        map.addMarker(new MarkerOptions()
                .position(posicao)
                .title(mEstabelecimento.getNomeFantasia()));

    }

    private void setupCoordinatorLayout(String titulo) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(titulo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) findViewById(R.id.appbar).getLayoutParams();
//        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
//        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
//            @Override
//            public boolean canDrag(AppBarLayout appBarLayout) {
//                if (appBarLayout.set)
//                return false;
//            }
//        });
//        params.setBehavior(behavior);
    }
}
