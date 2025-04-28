package com.ganancio.ui;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JanelaPrincipal extends JFrame {
  public static final String BOAS_VINDAS    = "BoasVindas";
  public static final String NOVO_ORCAMENTO = "NovoOrcamento";
  public static final String BUSCAR_ORCAMENTO = "BUSCAR_ORCAMENTO";

  private CardLayout     cards;
  private JPanel         container;
  private Map<String,JPanel> panels;

  public JanelaPrincipal() {
    super("Sistema de Orçamentos");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(800,600);
    setLocationRelativeTo(null);

    cards     = new CardLayout();
    container = new JPanel(cards);
    panels    = new HashMap<>();

    addPanel(new PainelBoasVindas(),    BOAS_VINDAS);
    addPanel(new PainelNovoOrcamento(),  NOVO_ORCAMENTO);
    addPanel(new PainelBuscarOrcamento(), BUSCAR_ORCAMENTO);

    getContentPane().add(container);
    showPanel(BOAS_VINDAS);
  }

  private void addPanel(JPanel p, String name) {
    container.add(p, name);
    panels.put(name, p);
  }

  public void showPanel(String name) {
    cards.show(container, name);
  }

  public JPanel getPanel(String name) {
    return panels.get(name);
  }
}