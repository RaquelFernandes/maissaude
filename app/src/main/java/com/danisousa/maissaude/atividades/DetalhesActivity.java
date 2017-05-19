package com.danisousa.maissaude.atividades;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.adaptadores.EstabelecimentosAdapter;
import com.danisousa.maissaude.modelos.Estabelecimento;
import com.danisousa.maissaude.utils.FotoHelper;
import com.danisousa.maissaude.utils.IntentHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class DetalhesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private Estabelecimento mEstabelecimento;

    private AppBarLayout mAppBarLayout;
    private FrameLayout mMapLoadingBackground;

    private FloatingActionButton mFloatingActionButton;

    private Button mLigarButton;
    private Button mCompartilharButton;
    private Button mSalvarButton;

    private TextView mNomeTextView;
    private TextView mEnderecoTextView;
    private TextView mTipoTextView;
    private TextView mRetencaoTextView;
    private TextView mTelefoneTextView;
    private TextView mTurnoTextView;

    private ImageView mTemSus;
    private ImageView mTemUrgencia;
    private ImageView mTemAmbulatorial;
    private ImageView mTemCentroCirurgico;
    private ImageView mTemObstetra;
    private ImageView mTemNeonatal;
    private ImageView mTemDialise;

    private ImageView mFotoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

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
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_rotas);

        mLigarButton = (Button) findViewById(R.id.detalhes_btn_ligar);
        mCompartilharButton = (Button) findViewById(R.id.detalhes_btn_compartilhar);
        mSalvarButton = (Button) findViewById(R.id.detalhes_btn_salvar);

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

        mFotoImageView = (ImageView) findViewById(R.id.detalhes_foto_iv);
    }

    private void bindView() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.direcoesDoMapa(DetalhesActivity.this, mEstabelecimento);
            }
        });

        mLigarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.ligar(DetalhesActivity.this, mEstabelecimento);
            }
        });
        mCompartilharButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.compartilharTexto(DetalhesActivity.this, mEstabelecimento);
            }
        });
        mSalvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        mNomeTextView.setText(mEstabelecimento.getNomeFantasia());
        mEnderecoTextView.setText(mEstabelecimento.getEndereco());
        mTipoTextView.setText(mEstabelecimento.getTipoUnidade());
        mRetencaoTextView.setText(mEstabelecimento.getRetencao());
        mTelefoneTextView.setText(mEstabelecimento.getTelefone());
        mTurnoTextView.setText(mEstabelecimento.getTurnoAtendimento());

        mTemSus.setImageResource(getBooleanImageView(mEstabelecimento.temVinculoSus()));
        mTemUrgencia.setImageResource(getBooleanImageView(mEstabelecimento.temAtendimentoUrgencia()));
        mTemAmbulatorial.setImageResource(getBooleanImageView(mEstabelecimento.temAtendimentoAmbulatorial()));
        mTemCentroCirurgico.setImageResource(getBooleanImageView(mEstabelecimento.temCentroCirurgico()));
        mTemObstetra.setImageResource(getBooleanImageView(mEstabelecimento.temObstetra()));
        mTemNeonatal.setImageResource(getBooleanImageView(mEstabelecimento.temNeoNatal()));
        mTemDialise.setImageResource(getBooleanImageView(mEstabelecimento.temDialise()));

        Drawable placeholderCircular = FotoHelper.imagemCircular(getResources(), R.drawable.usuario);
        mFotoImageView.setImageDrawable(placeholderCircular);

        FotoHelper.setFotoUsuario(this, mFotoImageView, mStorage, mAuth);
    }

    private int getBooleanImageView(boolean verdadeiro) {
        if (verdadeiro) {
            return R.drawable.ic_done;
        }
        return R.drawable.ic_clear;
    }

    private void setupFABBehavior() {
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
