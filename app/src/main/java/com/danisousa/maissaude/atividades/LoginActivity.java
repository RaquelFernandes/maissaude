package com.danisousa.maissaude.atividades;

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

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.utils.Validacao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailEditText;
    private EditText mSenhaEditText;
    private Button mEntrarButton;
    private Button mCadastroButton;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    // Usuário entrou
                    finish();
                    startActivity(MainActivity.newIntent(LoginActivity.this));
                }
            }
        };

        mEmailEditText = (EditText) findViewById(R.id.login_et_email);
        mSenhaEditText = (EditText) findViewById(R.id.login_et_senha);
        mEntrarButton = (Button) findViewById(R.id.login_btn_entrar);
        mCadastroButton = (Button) findViewById(R.id.login_btn_cadastro);

        mEntrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
            }
        });
        mCadastroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaCadastro();
            }
        });

        mSenhaEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mEntrarButton.performClick();
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

    public void irParaCadastro() {
        String email = mEmailEditText.getText().toString();
        String senha = mSenhaEditText.getText().toString();
        Intent intent = CadastroActivity.newIntent(LoginActivity.this, email, senha);
        startActivity(intent);
    }

    private boolean validar() {
        Boolean valido = true;

        TextInputLayout emailWrapper = (TextInputLayout) findViewById(R.id.login_et_email_wrapper);
        TextInputLayout senhaWrapper = (TextInputLayout) findViewById(R.id.login_et_senha_wrapper);

        String email = mEmailEditText.getText().toString();
        String senha = mSenhaEditText.getText().toString();

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

    public void entrar() {
        mProgressDialog = ProgressDialog.show(LoginActivity.this, "",
                getString(R.string.login_pdlg_acessando_conta), true);
        mProgressDialog.show();

        // fecha teclado
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        Boolean valido = validar();

        if (!valido) {
            mProgressDialog.dismiss();
        } else {
            String email = mEmailEditText.getText().toString();
            String senha = mSenhaEditText.getText().toString();

            mAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressDialog.dismiss();
                            if (!task.isSuccessful()) {

                                String erro = getString(R.string.erro_firebase_login_generico);
                                try {
                                    throw task.getException();
                                } catch (FirebaseNetworkException e) {
                                    erro = getString(R.string.erro_firebase_login_internet);
                                } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException e) {
                                    erro = getString(R.string.erro_firebase_login_credenciais);
                                } catch (Exception e) {
                                    Log.e("AUTH", e.getMessage());
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(R.string.login_dlg_erro_titulo)
                                        .setMessage(erro)
                                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        }).create().show();
                            }
                        }
                    })
            ;
        }
    }
}
