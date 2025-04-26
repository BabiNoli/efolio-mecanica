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
import java.util.List;

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

        // … resto das ações …  
    }

    // TODO: implementa montagem de ResumoOrcamento juntando dados do UI + OCaml
    private ResumoOrcamento montarResumoAtual() {
        // 1) lê linhas do painelExibir, converte p/ InfoPeca e InfoMaoDeObra
        // 2) preenche cliente, data, custoFixo, total, etc.
        return null;
    }
}
