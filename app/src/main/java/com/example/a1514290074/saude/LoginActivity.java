package com.example.a1514290074.saude;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.login_et_email);
        etSenha = (EditText) findViewById(R.id.login_et_senha);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    // Usuário entrou
                    Log.d("teste", "onAuthStateChanged:signed_in:" + usuario.getUid());
                } else {
                    // Usuário não entrou
                    Log.d("teste", "onAuthStateChanged:signed_out");
                }
            }
        };
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

    public void entrar(View v) {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.login_erro_autenticacao,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO: Ir para a activity principal
                            Toast.makeText(LoginActivity.this, "Usuario logou",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
