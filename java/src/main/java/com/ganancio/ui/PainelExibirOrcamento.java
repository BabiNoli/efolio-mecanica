// PainelExibirOrcamento.java
package com.ganancio.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PainelExibirOrcamento extends JPanel {
    private final JTextArea areaOrcamento;
    private final JButton   botaoSalvar;
    private final JButton   botaoExcluir;
    private final JButton   botaoGerarPdf;
    private final JButton   botaoVoltar;

    private String idsServicos;
    private String nomeCliente;
    private String dataNasc;

    public void setDadosEntrada(String idsServicos, String nomeCliente, String dataNasc) {
        this.idsServicos = idsServicos;
        this.nomeCliente = nomeCliente;
        this.dataNasc    = dataNasc;
    }

    public PainelExibirOrcamento() {
        setLayout(new BorderLayout(10,10));

        // Área de texto para exibir todas as linhas do orçamento
        areaOrcamento = new JTextArea();
        areaOrcamento.setEditable(false);
        areaOrcamento.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(areaOrcamento), BorderLayout.CENTER);

        // Painel de botões na parte de baixo
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        botaoSalvar   = new JButton("Salvar");
        botaoExcluir  = new JButton("Excluir");
        botaoGerarPdf = new JButton("Gerar PDF");
        botaoVoltar   = new JButton("Voltar");

        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoGerarPdf);
        painelBotoes.add(botaoVoltar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    /**
     * Preenche a área de texto com as linhas do orçamento.
     * Cada elemento da lista vira uma linha.
     */
    public void exibirLinhas(List<String> linhas) {
        areaOrcamento.setText("");
        for (String linha : linhas) {
            areaOrcamento.append(linha + "\n");
        }
    }

    public void adicionarAcaoSalvar(ActionListener acao) {
        botaoSalvar.addActionListener(acao);
    }

    public void adicionarAcaoExcluir(ActionListener acao) {
        botaoExcluir.addActionListener(acao);
    }

    public void adicionarAcaoGerarPdf(ActionListener acao) {
        botaoGerarPdf.addActionListener(acao);
    }

    public void adicionarAcaoVoltar(ActionListener acao) {
        botaoVoltar.addActionListener(acao);
    }

    public String getIdsServicos() {
        return idsServicos;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getDataNasc() {
        return dataNasc;
    }
}
