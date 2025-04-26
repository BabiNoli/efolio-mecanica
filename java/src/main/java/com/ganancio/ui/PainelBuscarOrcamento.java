// PainelBuscarOrcamento.java
package com.ganancio.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PainelBuscarOrcamento extends JPanel {
    private final JComboBox<String> comboCriterio;
    private final JTextField campoPesquisa;
    private final JButton botaoPesquisar;
    private final JButton botaoVoltar;

    public PainelBuscarOrcamento() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Rótulo e combo de critérios
        JLabel rotuloCriterio = new JLabel("Pesquisar por:");
        String[] criterios = {
            "ID do Orçamento",
            "Nome do Cliente",
            "Data de Nascimento",
            "Data do Orçamento"
        };
        comboCriterio = new JComboBox<>(criterios);

        // Campo de texto para a pesquisa
        JLabel rotuloPesquisa = new JLabel("Valor da pesquisa:");
        campoPesquisa = new JTextField(20);

        // Botões
        botaoPesquisar = new JButton("Pesquisar");
        botaoVoltar    = new JButton("Voltar");

        // Distribui componentes
        gbc.gridx = 0; gbc.gridy = 0;
        add(rotuloCriterio, gbc);
        gbc.gridx = 1;
        add(comboCriterio, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(rotuloPesquisa, gbc);
        gbc.gridx = 1;
        add(campoPesquisa, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(botaoPesquisar, gbc);
        gbc.gridx = 1;
        add(botaoVoltar, gbc);
    }

    /** Retorna o critério selecionado (por exemplo "ID do Orçamento") */
    public String getCriterioSelecionado() {
        return (String) comboCriterio.getSelectedItem();
    }

    /** Retorna o texto digitado para pesquisa */
    public String getTextoPesquisa() {
        return campoPesquisa.getText().trim();
    }

    /** Registra a ação a ser executada ao clicar em “Pesquisar” */
    public void adicionarAcaoPesquisar(ActionListener acao) {
        botaoPesquisar.addActionListener(acao);
    }

    /** Registra a ação a ser executada ao clicar em “Voltar” */
    public void adicionarAcaoVoltar(ActionListener acao) {
        botaoVoltar.addActionListener(acao);
    }
}
