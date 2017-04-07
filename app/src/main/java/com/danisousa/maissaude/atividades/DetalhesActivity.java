package com.danisousa.maissaude.atividades;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.adaptadores.EstabelecimentosAdapter;
import com.danisousa.maissaude.modelos.Estabelecimento;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetalhesActivity extends AppCompatActivity implements OnMapReadyCallback {

    Estabelecimento mEstabelecimento;
    AppBarLayout mAppBarLayout;
    FloatingActionButton mFloatingActionButton;
    FrameLayout mMapLoadingBackground;

    TextView mNomeTextView;
    TextView mEnderecoTextView;
    TextView mTipoTextView;
    TextView mRetencaoTextView;
    TextView mTelefoneTextView;
    TextView mTurnoTextView;

    ImageView mTemSus;
    ImageView mTemUrgencia;
    ImageView mTemAmbulatorial;
    ImageView mTemCentroCirurgico;
    ImageView mTemObstetra;
    ImageView mTemNeonatal;
    ImageView mTemDialise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        mEstabelecimento = (Estabelecimento) getIntent()
                .getSerializableExtra(EstabelecimentosAdapter.EXTRA_ESTABELECIMENTO);

        setupCoordinatorLayout(mEstabelecimento.getNomeFantasia());

        setupView();
        bindView();

        setupFABBehavior();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.detalhes_mapa);
        mapFragment.getMapAsync(this);
    }

    private void setupView() {
        mMapLoadingBackground = (FrameLayout) findViewById(R.id.map_background);

        mNomeTextView = (TextView) findViewById(R.id.detalhes_tv_nome);
        mEnderecoTextView = (TextView) findViewById(R.id.detalhes_tv_endereco);
        mTipoTextView = (TextView) findViewById(R.id.detalhes_tv_tipo);
        mRetencaoTextView = (TextView) findViewById(R.id.detalhes_tv_retencao);
        mTelefoneTextView = (TextView) findViewById(R.id.detalhes_tv_telefone);
        mTurnoTextView = (TextView) findViewById(R.id.detalhes_tv_turno);

        mTemSus = (ImageView) findViewById(R.id.detalhes_ico_sus);
        mTemUrgencia = (ImageView) findViewById(R.id.detalhes_ico_urgencia);
        mTemAmbulatorial = (ImageView) findViewById(R.id.detalhes_ico_ambulatorial);
        mTemCentroCirurgico = (ImageView) findViewById(R.id.detalhes_ico_centro_cirurgico);
        mTemObstetra = (ImageView) findViewById(R.id.detalhes_ico_obstetra);
        mTemNeonatal = (ImageView) findViewById(R.id.detalhes_ico_neonatal);
        mTemDialise = (ImageView) findViewById(R.id.detalhes_ico_dialise);
    }

    private void bindView() {
        mNomeTextView.setText(mEstabelecimento.getNomeFantasia());
        mEnderecoTextView.setText(mEstabelecimento.getNomeFantasia());
        mTipoTextView.setText(mEstabelecimento.getNomeFantasia());
        mRetencaoTextView.setText(mEstabelecimento.getNomeFantasia());
        mTelefoneTextView.setText(mEstabelecimento.getNomeFantasia());
        mTurnoTextView.setText(mEstabelecimento.getNomeFantasia());

        mTemSus.setImageResource(getBooleanImageView(mEstabelecimento.temVinculoSus()));
        mTemUrgencia.setImageResource(getBooleanImageView(mEstabelecimento.temAtendimentoUrgencia()));
        mTemAmbulatorial.setImageResource(getBooleanImageView(mEstabelecimento.temAtendimentoAmbulatorial()));
        mTemCentroCirurgico.setImageResource(getBooleanImageView(mEstabelecimento.temCentroCirurgico()));
        mTemObstetra.setImageResource(getBooleanImageView(mEstabelecimento.temObstetra()));
        mTemNeonatal.setImageResource(getBooleanImageView(mEstabelecimento.temNeoNatal()));
        mTemDialise.setImageResource(getBooleanImageView(mEstabelecimento.temDialise()));
    }

    private int getBooleanImageView(boolean verdadeiro) {
        if (verdadeiro) {
            return R.drawable.ic_done;
        }
        return R.drawable.ic_clear;
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

        LatLng posicao = new LatLng(mEstabelecimento.getLat(), mEstabelecimento.getLong());

        CameraPosition posicaoDaCamera = new CameraPosition.Builder()
                .target(posicao)
                .zoom(16)
                .build();

        map.moveCamera(CameraUpdateFactory.newCameraPosition(posicaoDaCamera));
        map.addMarker(new MarkerOptions()
                .position(posicao)
                .title(mEstabelecimento.getNomeFantasia()));

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMapLoadingBackground.setVisibility(View.GONE);
            }
        });

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
