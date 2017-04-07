package com.danisousa.maissaude.modelos;

import java.io.Serializable;

public class EstabelecimentoOld implements Serializable {

    private String codUnidade;
    private String nomeFantasia;
    private String descricaoCompleta;
    private String tipoUnidade;
    private String retencao;
    private String categoriaUnidade;
    private String endereco;
    private String telefone;
    private String turnoAtendimento;
    private double latitude;
    private double longitude;
    private boolean temVinculoSus;
    private boolean temAtendimentoUrgencia;
    private boolean temAtendimentoAmbulatorial;
    private boolean temCentroCirurgico;
    private boolean temObstetra;
    private boolean temNeoNatal;
    private boolean temDialise;

    public EstabelecimentoOld() {
    }

    public EstabelecimentoOld(String nomeFantasia, String tipoUnidade, String retencao, double latitude, double longitude) {
        this.nomeFantasia = nomeFantasia;
        this.tipoUnidade = tipoUnidade;
        this.retencao = retencao;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String name) {
        this.nomeFantasia = name;
    }

    public String getRetencao() {
        return retencao;
    }

    public void setRetencao(String retencao) {
        this.retencao = retencao;
    }

    public String getTipoUnidade() {
        return tipoUnidade;
    }

    public void setTipoUnidade(String tipoUnidade) {
        this.tipoUnidade = tipoUnidade;
    }

    public String getCodUnidade() {
        return codUnidade;
    }

    public void setCodUnidade(String codUnidade) {
        this.codUnidade = codUnidade;
    }

    public String getDescricaoCompleta() {
        return descricaoCompleta;
    }

    public void setDescricaoCompleta(String descricaoCompleta) {
        this.descricaoCompleta = descricaoCompleta;
    }

    public String getCategoriaUnidade() {
        return categoriaUnidade;
    }

    public void setCategoriaUnidade(String categoriaUnidade) {
        this.categoriaUnidade = categoriaUnidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTurnoAtendimento() {
        return turnoAtendimento;
    }

    public void setTurnoAtendimento(String turnoAtendimento) {
        this.turnoAtendimento = turnoAtendimento;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isVinculoSus() {
        return temVinculoSus;
    }

    public void setVinculoSus(boolean vinculoSus) {
        this.temVinculoSus = vinculoSus;
    }

    public boolean isTemAtendimentoUrgencia() {
        return temAtendimentoUrgencia;
    }

    public void setTemAtendimentoUrgencia(boolean temAtendimentoUrgencia) {
        this.temAtendimentoUrgencia = temAtendimentoUrgencia;
    }

    public boolean isTemAtendimentoAmbulatorial() {
        return temAtendimentoAmbulatorial;
    }

    public void setTemAtendimentoAmbulatorial(boolean temAtendimentoAmbulatorial) {
        this.temAtendimentoAmbulatorial = temAtendimentoAmbulatorial;
    }

    public boolean isTemCentroCirurgico() {
        return temCentroCirurgico;
    }

    public void setTemCentroCirurgico(boolean temCentroCirurgico) {
        this.temCentroCirurgico = temCentroCirurgico;
    }

    public boolean isTemObstetra() {
        return temObstetra;
    }

    public void setTemObstetra(boolean temObstetra) {
        this.temObstetra = temObstetra;
    }

    public boolean isTemNeoNatal() {
        return temNeoNatal;
    }

    public void setTemNeoNatal(boolean temNeoNatal) {
        this.temNeoNatal = temNeoNatal;
    }

    public boolean isTemDialise() {
        return temDialise;
    }

    public void setTemDialise(boolean temDialise) {
        this.temDialise = temDialise;
    }
}
