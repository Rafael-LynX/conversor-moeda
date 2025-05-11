package com.conversor.moeda;

import com.google.gson.annotations.SerializedName;

public class Moedas {
    private String nome;
    private String sigla;
    private double valor;

    public Moedas(String nome, String sigla, double valor) {
        this.nome = nome;
        this.sigla = sigla;
        this.valor = valor;
    }

    public Moedas(MoedaAPI moedaAPI) {
        this.nome = moedaAPI.nome();
        this.sigla = moedaAPI.sigla();
        this.valor = moedaAPI.valor();
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSigla() {
        return sigla;
    }
    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "(Moedas{" +
                "nome='" + nome + '\'' +
                ", sigla='" + sigla + '\'' +
                ", valor=" + valor +
                '}' +
                ')';
    }
}
