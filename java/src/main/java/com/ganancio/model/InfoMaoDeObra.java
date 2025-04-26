// InfoMaoDeObra.java
package com.ganancio.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoMaoDeObra {
    @JsonProperty("idServico")
    private int idServico;
    @JsonProperty("horas")
    private double horas;
    @JsonProperty("custoHora")
    private double custoHora;
    @JsonProperty("custoBruto")
    private double custoBruto;
    @JsonProperty("desconto")
    private double desconto;
    @JsonProperty("custoLiquido")
    private double custoLiquido;

    public InfoMaoDeObra() {}

    public InfoMaoDeObra(int idServico, double horas, double custoHora,
                         double custoBruto, double desconto, double custoLiquido) {
        this.idServico = idServico;
        this.horas = horas;
        this.custoHora = custoHora;
        this.custoBruto = custoBruto;
        this.desconto = desconto;
        this.custoLiquido = custoLiquido;
    }

    // getters/setters
    public int getIdServico() { return idServico; }
    public void setIdServico(int idServico) { this.idServico = idServico; }
    public double getHoras() { return horas; }
    public void setHoras(double horas) { this.horas = horas; }
    public double getCustoHora() { return custoHora; }
    public void setCustoHora(double custoHora) { this.custoHora = custoHora; }
    public double getCustoBruto() { return custoBruto; }
    public void setCustoBruto(double custoBruto) { this.custoBruto = custoBruto; }
    public double getDesconto() { return desconto; }
    public void setDesconto(double desconto) { this.desconto = desconto; }
    public double getCustoLiquido() { return custoLiquido; }
    public void setCustoLiquido(double custoLiquido) { this.custoLiquido = custoLiquido; }
}
