// InfoPeca.java
package com.ganancio.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoPeca {
    @JsonProperty("id")
    private int id;
    @JsonProperty("nome")
    private String nome;
    @JsonProperty("marca")
    private String marca;
    @JsonProperty("categoria")
    private String categoria;
    @JsonProperty("precoBruto")
    private double precoBruto;
    @JsonProperty("desconto")
    private double desconto;
    @JsonProperty("precoLiquido")
    private double precoLiquido;

    public InfoPeca() {}  // Jackson

    public InfoPeca(int id, String nome, String marca, String categoria,
                    double precoBruto, double desconto, double precoLiquido) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.categoria = categoria;
        this.precoBruto = precoBruto;
        this.desconto = desconto;
        this.precoLiquido = precoLiquido;
    }

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public double getPrecoBruto() { return precoBruto; }
    public void setPrecoBruto(double precoBruto) { this.precoBruto = precoBruto; }
    public double getDesconto() { return desconto; }
    public void setDesconto(double desconto) { this.desconto = desconto; }
    public double getPrecoLiquido() { return precoLiquido; }
    public void setPrecoLiquido(double precoLiquido) { this.precoLiquido = precoLiquido; }
}
