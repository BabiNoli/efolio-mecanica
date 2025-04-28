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
  private final JComboBox<String> cbTipo;
  private final JTextField        tfValor;
  private final JButton           botaoBuscar, botaoVoltar;

  public PainelBuscarOrcamento() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6,6,6,6); c.fill = GridBagConstraints.HORIZONTAL;

    cbTipo    = new JComboBox<>(new String[]{
      "ID Orçamento",
      "Nome do Cliente",
      "Data do Orçamento",
      "Data de Nascimento"
    });
    tfValor   = new JTextField(16);
    botaoBuscar = new JButton("Buscar");
    botaoVoltar = new JButton("Voltar");

    c.gridx=0; c.gridy=0; add(new JLabel("Filtrar por:"), c);
    c.gridx=1; add(cbTipo, c);
    c.gridx=0; c.gridy=1; add(new JLabel("Valor:"), c);
    c.gridx=1; add(tfValor, c);
    c.gridx=0; c.gridy=2; add(botaoBuscar, c);
    c.gridx=1; add(botaoVoltar, c);
  }

  public String    getTipo()  { return (String)cbTipo.getSelectedItem(); }
  public String    getValor() { return tfValor.getText().trim(); }
  public void adicionarAcaoBuscar(ActionListener a) { botaoBuscar.addActionListener(a); }
  public void adicionarAcaoVoltar(ActionListener a) { botaoVoltar.addActionListener(a); }
}
