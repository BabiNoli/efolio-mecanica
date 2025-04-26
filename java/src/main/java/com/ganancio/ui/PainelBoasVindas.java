// PainelBoasVindas.java
package com.ganancio.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PainelBoasVindas extends JPanel {
    private final JButton botaoNovoOrcamento;
    private final JButton botaoBuscarOrcamento;
    private final JButton botaoSair;

    public PainelBoasVindas() {
        setLayout(new BorderLayout(10,10));

        JLabel titulo = new JLabel("Bem–vind@ ao Sistema de Orçamentos", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new GridLayout(3,1,5,5));
        botaoNovoOrcamento   = new JButton("Novo Orçamento");
        botaoBuscarOrcamento = new JButton("Buscar Orçamentos");
        botaoSair            = new JButton("Sair");
        painelBotoes.add(botaoNovoOrcamento);
        painelBotoes.add(botaoBuscarOrcamento);
        painelBotoes.add(botaoSair);

        add(painelBotoes, BorderLayout.CENTER);
    }

    public void adicionarAcaoNovoOrcamento(ActionListener acao) {
        botaoNovoOrcamento.addActionListener(acao);
    }

    public void adicionarAcaoBuscarOrcamento(ActionListener acao) {
        botaoBuscarOrcamento.addActionListener(acao);
    }

    public void adicionarAcaoSair(ActionListener acao) {
        botaoSair.addActionListener(acao);
    }
}
