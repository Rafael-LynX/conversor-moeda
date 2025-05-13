package com.conversor.moeda;

public class Moedas {
    private String nome;
    private String sigla;
    private double valor;

    public Moedas(String nome, String sigla, double valor) {
        this.nome = nome;
        this.sigla = sigla;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }
    public String getSigla() {
        return sigla;
    }
    public double getValor() {
        return valor;
    }
}
