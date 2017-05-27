package com.danisousa.maissaude.dados;

import android.util.Log;

import com.danisousa.maissaude.modelos.Estabelecimento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritosDAO {

    private static final FavoritosDAO ourInstance = new FavoritosDAO();

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsuariosRef;
    private DatabaseReference mFavoritosRef;
    private List<Estabelecimento> mEstabelecimentos;

    private static final String TAG = "FavoritosDAO";

    public interface FavoritosListener {
        void onFavoritosChanged(List<Estabelecimento> estabelecimentos);
    }

    public static FavoritosDAO getInstance() {
        return ourInstance;
    }

    private FavoritosDAO() {
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();
        mUsuariosRef = mDatabase.getReference("usuarios");
        mFavoritosRef = mUsuariosRef.child(id).child("favoritos");
    }

    public void setFavoritosListener(final FavoritosDAO.FavoritosListener listener) {
        ValueEventListener valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Favoritos changed");
                List<Estabelecimento> estabelecimentos = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Estabelecimento estabelecimento = snapshot.getValue(Estabelecimento.class);
                    estabelecimentos.add(estabelecimento);
                }
                mEstabelecimentos = estabelecimentos;
                listener.onFavoritosChanged(estabelecimentos);
                Log.d(TAG, "Favoritos changed: " + estabelecimentos.size());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Erro ao carregar favoritos: ", databaseError.toException());
            }
        };
        mFavoritosRef.addValueEventListener(valueListener);
    }

    public void salvar(Estabelecimento estabelecimento) {
        mFavoritosRef.child(estabelecimento.getCodUnidade()).setValue(estabelecimento);
    }

    public void remover(Estabelecimento estabelecimento) {
        mFavoritosRef.child(estabelecimento.getCodUnidade()).removeValue();
    }

    public boolean estaNosFavoritos(String codUnidade) {
        for (Estabelecimento estabelecimento : mEstabelecimentos) {
            if (estabelecimento.getCodUnidade().equals(codUnidade)) {
                return true;
            }
        }
        return false;
    }
}
