package com.example.a1514290074.saude.atividades;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a1514290074.saude.R;
import com.google.firebase.auth.FirebaseAuth;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        Intent it;

        if (auth.getCurrentUser() != null) {
            it = new Intent(this, MainActivity.class);
        } else {
            it = new Intent(this, LoginActivity.class);
        }

        finish();
        startActivity(it);

    }
}
