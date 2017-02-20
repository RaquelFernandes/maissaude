package com.example.a1514290074.teste;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class CadastroActivity extends AppCompatActivity {

    static final int ESCOLHER_FOTO = 1;

    EditText tvEmail;
    EditText tvSenha;
    ImageView ivFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        tvEmail = (EditText) findViewById(R.id.cadastro_et_email);
        tvSenha = (EditText) findViewById(R.id.cadastro_et_senha);
        ivFoto = (ImageView) findViewById(R.id.iv_foto);

        // Deixa a foto do usuário circular
        Drawable fotoCircular = Helper.imagemCircular(getResources(), R.drawable.usuario);
        ivFoto.setImageDrawable(fotoCircular);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("email")) {
                tvEmail.setText(extras.getString("email"));
            }
            if (extras.containsKey("senha")) {
                tvSenha.setText(extras.getString("senha"));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ESCOLHER_FOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "Erro ao tentar carregar foto", Toast.LENGTH_SHORT).show();
                return;
            }
            final Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap novaFoto = extras.getParcelable("data");
                Drawable fotoCircular = Helper.imagemCircular(getResources(), novaFoto);
                ivFoto.setImageDrawable(fotoCircular);
            }
        }
    }

    public void irParaLogin(View v) {
        finish();
    }

    public void trocarFoto(View v) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ESCOLHER_FOTO);
    }
}
