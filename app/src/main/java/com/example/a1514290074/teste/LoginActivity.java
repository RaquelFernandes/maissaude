package com.example.a1514290074.teste;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void irParaCadastro(View v) {
        Intent intent = new Intent(this, CadastroActivity.class);
        etEmail = (EditText) findViewById(R.id.login_et_email);
        etSenha = (EditText) findViewById(R.id.login_et_senha);
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        intent.putExtra("email", email);
        intent.putExtra("senha", senha);
        startActivity(intent);
    }

    public void entrar() {

    }
}
