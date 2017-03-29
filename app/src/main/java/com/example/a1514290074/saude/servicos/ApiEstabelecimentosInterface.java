package com.example.a1514290074.saude.servicos;

import com.example.a1514290074.saude.modelos.Estabelecimento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEstabelecimentosInterface {

    String BASE_URL = "http://mobile-aceite.tcu.gov.br/mapa-da-saude/";
    int QUANTIDADE = 100;

    @GET("rest/estabelecimentos?quantidade=" + QUANTIDADE)
    Call<List<Estabelecimento>> getTodosEstabelecimentos(@Query("pagina") int pagina);

    @GET("rest/estabelecimentos/latitude/{latitude}/longitude/{longitude}/raio/{raio}")
    Call<List<Estabelecimento>> getEstabelecimentosPorCoordenadas(
            @Path("latitude") double latitude,
            @Path("longitude") double longitude,
            @Path("raio") float raio
    );

    @GET("rest/estabelecimentos/unidade/{codUnidade}")
    Call<Estabelecimento> getEstabelecimento(@Path("codUnidade") String codUnidade);
}
