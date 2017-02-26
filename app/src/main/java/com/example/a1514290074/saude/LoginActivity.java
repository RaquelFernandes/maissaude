package com.example.a1514290074.saude;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etEmail;
    private EditText etSenha;
    private Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    // Usuário entrou
                    Log.d("teste", "onAuthStateChanged:signed_in:" + usuario.getUid());
                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                    finish();
                    startActivity(it);

                } else {
                    // Usuário não entrou
                    Log.d("teste", "onAuthStateChanged:signed_out");
                }
            }
        };

        etEmail = (EditText) findViewById(R.id.login_et_email);
        etSenha = (EditText) findViewById(R.id.login_et_senha);
        btnEntrar = (Button) findViewById(R.id.login_btn_entrar);

        etSenha.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnEntrar.performClick();
                    return true;
                }
                return false;
            }
        });
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

    public void irParaCadastro(View v) {
        Intent intent = new Intent(this, CadastroActivity.class);
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        intent.putExtra("email", email);
        intent.putExtra("senha", senha);
        startActivity(intent);
    }

    private boolean validar() {
        Boolean valido = true;

        TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.login_et_email_wrapper);
        TextInputLayout senhaWrapper = (TextInputLayout) findViewById(R.id.login_et_senha_wrapper);

        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        if (!Validacao.campoObrigatorio(email)) {
            valido = false;
            emailWrapper.setError(getString(R.string.erro_campo_obrigatorio));
        } else {
            emailWrapper.setErrorEnabled(false);
        }

        if (!Validacao.campoObrigatorio(senha)) {
            valido = false;
            senhaWrapper.setError(getString(R.string.erro_campo_obrigatorio));
        } else {
            senhaWrapper.setErrorEnabled(false);
        }

        return valido;
    }

    public void entrar(View v) {
        final ProgressDialog loader = ProgressDialog.show(LoginActivity.this, "",
                getString(R.string.login_pdlg_acessando_conta), true);
        loader.show();

        Boolean valido = validar();

        // fecha teclado
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (!valido) {
            loader.dismiss();
        } else {
            String email = etEmail.getText().toString();
            String senha = etSenha.getText().toString();

            mAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loader.dismiss();
                            if (!task.isSuccessful()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(R.string.login_dlg_erro_titulo)
                                        .setMessage(R.string.login_dlg_erro_mensagem)
                                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        }).create().show();
                            }
                        }
                    });
        }
    }
}
