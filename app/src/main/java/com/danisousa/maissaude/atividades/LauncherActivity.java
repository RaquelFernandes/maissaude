package com.danisousa.maissaude.atividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
//        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(it);

    }
}
