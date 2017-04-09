package com.danisousa.maissaude.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.danisousa.maissaude.R;
import com.danisousa.maissaude.modelos.Estabelecimento;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

public class IntentHelper {

    public static void abrirMapa(Context context, Estabelecimento estabelecimento) {
        String nome = estabelecimento.getNomeFantasia();
        String lat = Double.toString(estabelecimento.getLat());
        String lon = Double.toString(estabelecimento.getLong());
        String url = MessageFormat.format("geo:{0},{1}?q={0},{1}({2})", lat, lon, nome);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public static void direcoesDoMapa(Context context, Estabelecimento estabelecimento) {
        String lat = Double.toString(estabelecimento.getLat());
        String lon = Double.toString(estabelecimento.getLong());
        String uriStr = MessageFormat.format("google.navigation:q={0},{1}", lat, lon);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr));
//                intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public static void ligar(Context context, Estabelecimento estabelecimento) {
        Uri uri = Uri.parse("tel:" + estabelecimento.getTelefone());
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    public static void compartilharTexto(Context context, Estabelecimento estabelecimento) {
        CharSequence titulo = context.getResources().getText(R.string.compartilhar_txt);
        String nome = estabelecimento.getNomeFantasia();
        String nomeEncoded = "";

        try {
            nomeEncoded = URLEncoder.encode(nome, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String lat = Double.toString(estabelecimento.getLat());
        String lon = Double.toString(estabelecimento.getLong());
        String texto = MessageFormat.format("{0} - http://maps.google.com/maps?q={1},{2}+({3})", nome, lat, lon, nomeEncoded);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, texto);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, titulo));
    }
}
