// ResumoOrcamento.java
package com.ganancio.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResumoOrcamento {
    @JsonProperty("idOrcamento")
    private String idOrcamento;
    @JsonProperty("nomeCliente")
    private String nomeCliente;
    @JsonProperty("dataNascimento")
    private String dataNascimento;
    @JsonProperty("dataOrcamento")
    private LocalDate dataOrcamento;
    @JsonProperty("pecas")
    private List<InfoPeca> pecas;
    @JsonProperty("maoDeObra")
    private List<InfoMaoDeObra> maoDeObra;
    @JsonProperty("custoFixo")
    private double custoFixo;
    @JsonProperty("total")
    private double total;

    public ResumoOrcamento() {}

    public ResumoOrcamento(String idOrcamento, String nomeCliente, String dataNascimento,
                           LocalDate dataOrcamento, List<InfoPeca> pecas,
                           List<InfoMaoDeObra> maoDeObra, double custoFixo, double total) {
        this.idOrcamento = idOrcamento;
        this.nomeCliente = nomeCliente;
        this.dataNascimento = dataNascimento;
        this.dataOrcamento = dataOrcamento;
        this.pecas = pecas;
        this.maoDeObra = maoDeObra;
        this.custoFixo = custoFixo;
        this.total = total;
    }

    // getters/setters
    public String getIdOrcamento() { return idOrcamento; }
    public void setIdOrcamento(String idOrcamento) { this.idOrcamento = idOrcamento; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public LocalDate getDataOrcamento() { return dataOrcamento; }
    public void setDataOrcamento(LocalDate dataOrcamento) { this.dataOrcamento = dataOrcamento; }
    public List<InfoPeca> getPecas() { return pecas; }
    public void setPecas(List<InfoPeca> pecas) { this.pecas = pecas; }
    public List<InfoMaoDeObra> getMaoDeObra() { return maoDeObra; }
    public void setMaoDeObra(List<InfoMaoDeObra> maoDeObra) { this.maoDeObra = maoDeObra; }
    public double getCustoFixo() { return custoFixo; }
    public void setCustoFixo(double custoFixo) { this.custoFixo = custoFixo; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
