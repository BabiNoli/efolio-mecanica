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
  private final JTextField          campoIds;
  private final JTextField          campoNome;
  private final JFormattedTextField campoData;
  private final JButton             botaoGerar;
  private final JButton             botaoVoltar;

  public PainelNovoOrcamento() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6,6,6,6); c.fill = GridBagConstraints.HORIZONTAL;

    campoIds  = new JTextField(12);
    campoNome = new JTextField(16);
    try {
      campoData = new JFormattedTextField(new MaskFormatter("##/##/####"));
    } catch(ParseException e) {
      throw new RuntimeException(e);
    }
    botaoGerar  = new JButton("Gerar Orçamento");
    botaoVoltar = new JButton("Voltar");

    c.gridx=0; c.gridy=0; add(new JLabel("IDs dos Serviços (ex:1,2):"), c);
    c.gridx=1; add(campoIds, c);
    c.gridx=0; c.gridy=1; add(new JLabel("Nome do Cliente:"), c);
    c.gridx=1; add(campoNome, c);
    c.gridx=0; c.gridy=2; add(new JLabel("Data de Nascimento:"), c);
    c.gridx=1; add(campoData, c);
    c.gridx=0; c.gridy=3; add(botaoGerar, c);
    c.gridx=1; add(botaoVoltar, c);
  }

   /** Limpa todos os campos de entrada deste painel */
  public void limparCampos() {
    campoIds.setText("");
    campoNome.setText("");
    campoData.setText("");  
  }

  public String getIdsServicos()        { return campoIds.getText().trim(); }
  public String getNomeCliente()        { return campoNome.getText().trim(); }
  public String getDataNascimento()     { return campoData.getText().trim(); }
  public void adicionarAcaoGerar(ActionListener a) { botaoGerar.addActionListener(a); }
  public void adicionarAcaoVoltar(ActionListener a) { botaoVoltar.addActionListener(a); }
}