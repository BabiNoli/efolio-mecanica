// PainelNovoOrcamento.java
package com.ganancio.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class PainelNovoOrcamento extends JPanel {
    private final JTextField          campoNomeCliente;
    private final JFormattedTextField campoDataNascimento;
    private final JTextField          campoIdsServicos;
    private final JButton             botaoGerarOrcamento;
    private final JButton             botaoVoltar;

    public PainelNovoOrcamento() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JLabel rotuloNome = new JLabel("Nome do Cliente:");
        campoNomeCliente  = new JTextField(20);

        JLabel rotuloData = new JLabel("Data Nasc. (dd/MM/yyyy):");
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.setPlaceholderCharacter('_');
            campoDataNascimento = new JFormattedTextField(mf);
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }

        JLabel rotuloIds = new JLabel("IDs dos Serviços (ex:1,2,5):");
        campoIdsServicos = new JTextField(15);

        botaoGerarOrcamento = new JButton("Gerar Orçamento");
        botaoVoltar         = new JButton("Voltar");

        gbc.gridx = 0; gbc.gridy = 0; add(rotuloNome, gbc);
        gbc.gridx = 1;            add(campoNomeCliente, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(rotuloData, gbc);
        gbc.gridx = 1;            add(campoDataNascimento, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(rotuloIds, gbc);
        gbc.gridx = 1;            add(campoIdsServicos, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(botaoGerarOrcamento, gbc);
        gbc.gridx = 1;            add(botaoVoltar, gbc);
    }

    public String getNomeCliente() {
        return campoNomeCliente.getText().trim();
    }

    public String getDataNascimento() {
        return campoDataNascimento.getText().trim();
    }

    public String getIdsServicos() {
        return campoIdsServicos.getText().trim();
    }

    public void adicionarAcaoGerar(ActionListener acao) {
        botaoGerarOrcamento.addActionListener(acao);
    }

    public void adicionarAcaoVoltar(ActionListener acao) {
        botaoVoltar.addActionListener(acao);
    }
}
