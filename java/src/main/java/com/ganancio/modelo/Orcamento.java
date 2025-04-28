package com.ganancio.modelo;


import java.util.ArrayList;
import java.util.List;

public class Orcamento {
  private final String id;
  private final String cliente;
  private final String dataNascimento;
  private final String dataOrcamento;
  private final List<String> itens;

  public Orcamento(String id, String cliente, String dataNascimento, String dataOrcamento, List<String> itens) {
    this.id = id;
    this.cliente = cliente;
    this.dataNascimento = dataNascimento;
    this.dataOrcamento = dataOrcamento;
    this.itens = new ArrayList<>(itens);
  }

  public String getId() { return id; }

  /** Gera as linhas formatadas para exibição ou gravação em .txt */
  public List<String> toLines() {
    List<String> lines = new ArrayList<>();
    lines.add("ID do Orçamento:       " + id);
    lines.add("Nome do Cliente:       " + cliente);
    lines.add("Data de Nascimento:    " + dataNascimento);
    lines.add("Data do Orçamento:     " + dataOrcamento);
    lines.add("");
    lines.addAll(itens);
    return lines;
  }

    public String getCliente() {
         return cliente;
    }

    public Object getDataOrcamento() {
        return dataOrcamento;
    }

    public Object getDataNascimento() {
        return dataNascimento;
    }
}
