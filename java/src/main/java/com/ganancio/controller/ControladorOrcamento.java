// ControladorOrcamento.java
package com.ganancio.controller;

import com.ganancio.pdf.GerenciadorPDF;
import com.ganancio.repositorio.RepositorioJSON;
import com.ganancio.ui.*;
import com.ganancio.util.GerenciadorIDOrcamento;
import com.ganancio.model.ResumoOrcamento;
import com.ganancio.IntegradorOCaml;

import javax.swing.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ganancio.model.InfoMaoDeObra;
import com.ganancio.model.InfoPeca;

public class ControladorOrcamento {
    private JanelaPrincipal janela;
    private RepositorioJSON  repo;
    private GerenciadorPDF   geradorPdf;

    public ControladorOrcamento() {
        this.janela     = new JanelaPrincipal();
        this.repo       = new RepositorioJSON();
        this.geradorPdf = new GerenciadorPDF();
        configurarAcoes();
    }

    public void iniciar() {
        janela.setVisible(true);
    }

    private void configurarAcoes() {
        // (a mesma lógica que havia em JanelaPrincipal.configureAcoes,
        //  mas agora puxando repo, geradorPdf, GerenciadorIDOrcamento)
        // Exemplo do botão "Salvar" no PainelExibirOrcamento:
        PainelExibirOrcamento painelExibir =
            (PainelExibirOrcamento) janela.obterPainel(JanelaPrincipal.EXIBIR_ORCAMENTO);
        painelExibir.adicionarAcaoSalvar(e -> {
            try {
                ResumoOrcamento resumo = montarResumoAtual(); // TODO: criar método
                resumo.setIdOrcamento(GerenciadorIDOrcamento.gerarNovoID());
                repo.salvar(resumo);
                JOptionPane.showMessageDialog(janela, "Orçamento salvo com sucesso!");
                janela.mostrarPainel(JanelaPrincipal.BOAS_VINDAS);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(janela, "Erro ao salvar:\n" + ex.getMessage(),
                                              "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelExibir.adicionarAcaoGerarPdf(e -> {
            try {
                ResumoOrcamento resumo = montarResumoAtual(); // TODO
                String out = "orcamento_" + resumo.getIdOrcamento() + ".pdf";
                geradorPdf.gerarPDF(resumo, out);
                JOptionPane.showMessageDialog(janela, "PDF gerado: " + out);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(janela, "Erro ao gerar PDF:\n" + ex.getMessage(),
                                              "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        
    }

    private ResumoOrcamento criarResumo(String idsServicos,
                                       String nomeCliente,
                                       String dataNasc) throws Exception {
        List<String> linhas = IntegradorOCaml.executar("orcamento_total", idsServicos);
        if (linhas.isEmpty()) return null;

        List<InfoPeca> pecas    = new ArrayList<>();
        List<InfoMaoDeObra> mos = new ArrayList<>();
        double custoFixo = 0, total = 0;
        boolean lendoPecas = false, lendoMo = false;

        for (String l : linhas) {
            if (l.startsWith("PEÇAS:")) {
                lendoPecas = true; lendoMo = false;
                continue;
            }
            if (l.startsWith("MÃO-DE-OBRA:") || l.startsWith("MÃO DE OBRA:")) {
                lendoPecas = false; lendoMo = true;
                continue;
            }
            if (l.startsWith("Custo Fixo")) {
                custoFixo = Double.parseDouble(l.split(":")[1].trim());
            } else if (l.startsWith("Total")) {
                total = Double.parseDouble(l.split(":")[1].trim());
            } else if (lendoPecas) {
                // Exemplo de linha de peça:
                // "1 – Filtro de Oleo: bruto=7.00  desc=0.70  líquido=6.30"
                String[] partes = l.split("–|bruto=|desc=|líquido=");
                int id       = Integer.parseInt(partes[0].trim());
                String nome  = partes[1].replace(":", "").trim();
                double bruto = Double.parseDouble(partes[2].trim());
                double desc  = Double.parseDouble(partes[3].trim());
                double liq   = Double.parseDouble(partes[4].trim());
                pecas.add(new InfoPeca(id, nome, bruto, desc, liq));

            } else if (lendoMo) {
                // Exemplo de linha de mão-de-obra:
                // "1 – horas=0.50  bruto=4.00  desc=0.00  líquido=4.00"
                String[] p = l.split("–|horas=|bruto=|desc=|líquido=");
                int id         = Integer.parseInt(p[0].trim());
                double horas   = Double.parseDouble(p[1].trim());
                double bruto   = Double.parseDouble(p[2].trim());
                double desc    = Double.parseDouble(p[3].trim());
                double liq     = Double.parseDouble(p[4].trim());
                double custoHora = horas > 0 ? bruto / horas : 0;
                mos.add(new InfoMaoDeObra(id, horas, custoHora, bruto, desc, liq));
            }
        }

        // gera ID e data do orçamento
        String idOrc     = GerenciadorIDOrcamento.gerarNovoID();
        LocalDate hoje   = LocalDate.now();

        return new ResumoOrcamento(
            idOrc,
            nomeCliente,
            dataNasc,
            hoje,
            pecas,
            mos,
            custoFixo,
            total
        );
    }

    private ResumoOrcamento montarResumoAtual() throws Exception {
        // obtém o painel atual de exibição de orçamento
        PainelExibirOrcamento painel =
            (PainelExibirOrcamento) janela.obterPainel(JanelaPrincipal.EXIBIR_ORCAMENTO);
        // coleta os valores informados pelo usuário
        String idsServicos = painel.getIdsServicos();
        String nomeCliente = painel.getNomeCliente();
        String dataNasc    = painel.getDataNasc();
        // delega para criarResumo
        return criarResumo(idsServicos, nomeCliente, dataNasc);
}

}
