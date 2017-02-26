package com.example.a1514290074.saude;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final int ESCOLHER_FOTO = 1;
    private static final String FOTO_PERFIL = "foto";

    EditText etNome;
    EditText etEmail;
    EditText etSenha;
    EditText etConfirmarSenha;
    ImageView ivFoto;
    Button btnCriarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    // Usuário entrou
                    Log.d("teste", "onAuthStateChanged:signed_in:" + usuario.getUid());
                    Intent it = new Intent(CadastroActivity.this, MainActivity.class);
                    finish();
                    startActivity(it);
                } else {
                    // Usuário não entrou
                    Log.d("teste", "onAuthStateChanged:signed_out");
                }
            }
        };

        etNome = (EditText) findViewById(R.id.cadastro_et_nome);
        etEmail = (EditText) findViewById(R.id.cadastro_et_email);
        etSenha = (EditText) findViewById(R.id.cadastro_et_senha);
        etConfirmarSenha = (EditText) findViewById(R.id.cadastro_et_confirmar_senha);
        ivFoto = (ImageView) findViewById(R.id.iv_foto);
        btnCriarConta = (Button) findViewById(R.id.cadastro_btn_cadastrar);

        etConfirmarSenha.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnCriarConta.performClick();
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(FOTO_PERFIL)) {
            // Carrega foto do usuário circular
            Drawable fotoCircular = Helper.imagemCircular(getResources(), (Bitmap) savedInstanceState.getParcelable(FOTO_PERFIL));
            ivFoto.setImageDrawable(fotoCircular);
        } else {
            // Carrega foto padrão circular
            Drawable fotoCircular = Helper.imagemCircular(getResources(), R.drawable.usuario);
            ivFoto.setImageDrawable(fotoCircular);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("email")) {
                etEmail.setText(extras.getString("email"));
            }
            if (extras.containsKey("senha")) {
                etSenha.setText(extras.getString("senha"));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bitmap foto = ((RoundedBitmapDrawable) ivFoto.getDrawable()).getBitmap();
        outState.putParcelable(FOTO_PERFIL, foto);
    }

    private boolean validar() {

        Boolean valido = true;

        TextInputLayout nomeWrapper = (TextInputLayout) findViewById(R.id.cadastro_et_nome_wrapper);
        TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.cadastro_et_email_wrapper);
        TextInputLayout senhaWrapper = (TextInputLayout) findViewById(R.id.cadastro_et_senha_wrapper);
        TextInputLayout confirmarSenhaWrapper = (TextInputLayout) findViewById(R.id.cadastro_et_confirmar_senha_wrapper);

        String nome = etNome.getText().toString();
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        String confirmarSenha = etConfirmarSenha.getText().toString();

        if (!Validacao.nome(nome)) {
            valido = false;
            nomeWrapper.setError(getString(R.string.erro_campo_obrigatorio));
        } else {
            nomeWrapper.setErrorEnabled(false);
        }

        if (!Validacao.email(email)) {
            valido = false;
            emailWrapper.setError(getString(R.string.erro_email));
        } else {
            emailWrapper.setErrorEnabled(false);
        }

        if (!Validacao.senha(senha)) {
            valido = false;
            senhaWrapper.setError(getString(R.string.erro_tamanho_senha));
        } else {
            senhaWrapper.setErrorEnabled(false);
        }

        if (!Validacao.confirmarSenha(senha, confirmarSenha)) {
            valido = false;
            confirmarSenhaWrapper.setError(getString(R.string.erro_confirmar_senha));
        } else {
            confirmarSenhaWrapper.setErrorEnabled(false);
        }

        return valido;
    }

    public void cadastrar(View v) {
        final ProgressDialog loader = ProgressDialog.show(CadastroActivity.this, "",
                "Criando conta, \naguarde...", true);

        Boolean valido = validar();

        // fecha teclado
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (!valido) {
            loader.dismiss();
        } else {
            String email = etEmail.getText().toString();
            String senha = etSenha.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loader.dismiss();
                            if (!task.isSuccessful()) {
                                String erro = getString(R.string.erro_firebase_generico);
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    erro = getString(R.string.erro_firebase_tamanho_senha);
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    erro = getString(R.string.erro_firebase_email);
                                } catch(FirebaseAuthUserCollisionException e) {
                                    erro = getString(R.string.erro_firebase_email_usado);
                                } catch(Exception e) {
                                    Log.e("AUTH", e.getMessage());
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);
                                builder.setTitle(R.string.cadastro_erro_titulo)
                                        .setMessage(erro)
                                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        }).create().show();
                            } else {
                                Toast.makeText(CadastroActivity.this, R.string.cadastro_toast_conta_criada,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
