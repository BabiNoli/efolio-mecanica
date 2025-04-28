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
  private final JButton botaoNovo;
  private final JButton botaoBuscar;
  private final JButton botaoSair;

  public PainelBoasVindas() {
    setLayout(new BorderLayout(10,10));
    JLabel lbl = new JLabel("Bem‑vindo ao Sistema de Orçamentos", SwingConstants.CENTER);
    lbl.setFont(new Font("SansSerif", Font.BOLD, 24));
    add(lbl, BorderLayout.NORTH);


    JPanel botoes = new JPanel(new GridLayout(3,1,5,5));
    botaoNovo = new JButton("Novo Orçamento");
    botaoBuscar = new JButton("Buscar Orçamento");
    botaoSair = new JButton("Sair");
    botoes.add(botaoNovo);
    botoes.add(botaoBuscar);
    botoes.add(botaoSair);
    add(botoes,  BorderLayout.CENTER);
  }

  public void adicionarAcaoNovoOrcamento(ActionListener a) {
    botaoNovo.addActionListener(a);
  }

  public void adicionarAcaoSair(ActionListener a) {
    botaoSair.addActionListener(a);
  }

  public void adicionarAcaoBuscarOrcamento(ActionListener a) { 
    botaoBuscar.addActionListener(a); }
  
}