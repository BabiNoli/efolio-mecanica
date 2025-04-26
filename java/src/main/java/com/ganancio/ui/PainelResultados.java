// PainelResultados.java
package com.ganancio.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class PainelResultados extends JPanel {
    private final DefaultListModel<String> modeloLista;
    private final JList<String>           listaOrcamentos;
    private final JButton                  botaoSelecionar;
    private final JButton                  botaoVoltar;

    public PainelResultados() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets   = new Insets(8, 8, 8, 8);
        gbc.fill     = GridBagConstraints.BOTH;
        gbc.weightx  = 1.0;
        gbc.weighty  = 1.0;
        gbc.gridx    = 0;
        gbc.gridy    = 0;
        gbc.gridwidth= 2;

        modeloLista     = new DefaultListModel<>();
        listaOrcamentos = new JList<>(modeloLista);
        listaOrcamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(listaOrcamentos);

        add(scroll, gbc);

        botaoSelecionar = new JButton("Selecionar");
        botaoVoltar     = new JButton("Voltar");

        gbc.weighty   = 0.0;
        gbc.gridwidth= 1;
        gbc.gridy    = 1;
        gbc.gridx    = 0;
        gbc.fill     = GridBagConstraints.HORIZONTAL;
        add(botaoSelecionar, gbc);

        gbc.gridx = 1;
        add(botaoVoltar, gbc);
    }

    /** Atualiza as linhas exibidas na lista */
    public void atualizarResultados(List<String> resultados) {
        modeloLista.clear();
        for (String linha : resultados) {
            modeloLista.addElement(linha);
        }
    }

    /** Índice selecionado na lista (-1 se nada) */
    public int obterIndiceSelecionado() {
        return listaOrcamentos.getSelectedIndex();
    }

    /** Valor (texto) selecionado na lista */
    public String obterValorSelecionado() {
        return listaOrcamentos.getSelectedValue();
    }

    public void adicionarAcaoSelecionar(ActionListener acao) {
        botaoSelecionar.addActionListener(acao);
    }

    public void adicionarAcaoVoltar(ActionListener acao) {
        botaoVoltar.addActionListener(acao);
    }
}
