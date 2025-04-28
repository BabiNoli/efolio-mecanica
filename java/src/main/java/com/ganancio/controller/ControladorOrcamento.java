package com.ganancio.controller;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.swing.JOptionPane;

import com.ganancio.IntegradorOCaml;
import com.ganancio.armazenamento.OrcamentosSalvos;
import com.ganancio.modelo.Orcamento;
import com.ganancio.ui.JanelaPrincipal;
import com.ganancio.ui.PainelBoasVindas;
import com.ganancio.ui.PainelBuscarOrcamento;
import com.ganancio.ui.PainelNovoOrcamento;

public class ControladorOrcamento {
  private JanelaPrincipal frame;

  public ControladorOrcamento() {
    frame = new JanelaPrincipal();
    configureActions();
  }

  public void iniciar() {
    frame.setVisible(true);
  }

  private void configureActions() {
    // Boas‑Vindas
    PainelBoasVindas p0 = (PainelBoasVindas) frame.getPanel(JanelaPrincipal.BOAS_VINDAS);
    p0.adicionarAcaoNovoOrcamento(e -> frame.showPanel(JanelaPrincipal.NOVO_ORCAMENTO));
    p0.adicionarAcaoSair(e -> System.exit(0));
    p0.adicionarAcaoBuscarOrcamento(e -> frame.showPanel(JanelaPrincipal.BUSCAR_ORCAMENTO));

    // Novo Orçamento
    PainelNovoOrcamento p1 = (PainelNovoOrcamento) frame.getPanel(JanelaPrincipal.NOVO_ORCAMENTO);
    p1.adicionarAcaoVoltar(e -> frame.showPanel(JanelaPrincipal.BOAS_VINDAS));
    p1.adicionarAcaoGerar(e -> {
      try {
        // captura dados da UI
        String ids  = p1.getIdsServicos();
        String nome = p1.getNomeCliente();
        String nasc = p1.getDataNascimento();

        // gera ID e data do orçamento
        String idOrc   = UUID.randomUUID().toString();
        String dataOrc = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // chama OCaml
        List<String> linhas = IntegradorOCaml.executar("orcamento_total", ids);

        // monta lista com cabeçalho + detalhes
        List<String> todas = new ArrayList<>();
        todas.add("ID do Orçamento:         " + idOrc);
        todas.add("Nome do Cliente:         " + nome);
        todas.add("Data de Nascimento:  " + nasc);
        todas.add("Data do Orçamento:   " + dataOrc);
        todas.add("");                // linha em branco
        todas.addAll(linhas);

        // exibe opções para o usuário
        String[] op = { "Excluir", "Salvar" };
        int escolha = JOptionPane.showOptionDialog(
          frame,
          String.join("\n", todas),
          "Orçamento",
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.PLAIN_MESSAGE,
          null,
          op,
          op[1]
        );
        if (escolha == 0) {
          // ao clicar em Excluir, volta à tela inicial
          frame.showPanel(JanelaPrincipal.BOAS_VINDAS);
        }
        else if (escolha == 1) {
          // ao clicar em Salvar, grava o .txt
        
          Orcamento o = new Orcamento(idOrc, nome, nasc, dataOrc, linhas);
          Path salvo = OrcamentosSalvos.salvar(o);
          JOptionPane.showMessageDialog(
            frame,
            "Orçamento salvo em:\n" + salvo.toAbsolutePath(),
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE
          );
        }

        // limpa campos após salvar o orçamento
        p1.limparCampos();

      } catch (Exception ex) {
        JOptionPane.showMessageDialog(
          frame,
          "Erro: " + ex.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE
        );
      }
    });

    PainelBuscarOrcamento p2 = (PainelBuscarOrcamento)
      frame.getPanel(JanelaPrincipal.BUSCAR_ORCAMENTO);
    p2.adicionarAcaoVoltar(e ->
      frame.showPanel(JanelaPrincipal.BOAS_VINDAS)
    );
    p2.adicionarAcaoBuscar(e -> {
      try {
        String tipo  = p2.getTipo();
        String valor = p2.getValor();
        List<Orcamento> resultados = OrcamentosSalvos.buscar(tipo, valor);
        if (resultados.isEmpty()) {
          JOptionPane.showMessageDialog(
            frame, "Nenhum resultado para: " + valor,
            "Busca", JOptionPane.INFORMATION_MESSAGE
          );
          return;
        }
        String[] opts = resultados.stream()
          .map(o -> o.getId() + " – " + o.getCliente())
          .toArray(String[]::new);
        String sel = (String) JOptionPane.showInputDialog(
          frame,
          "Selecione um orçamento:",
          "Resultados",
          JOptionPane.PLAIN_MESSAGE,
          null,
          opts,
          opts[0]
        );
        if (sel != null) {
          Orcamento o = resultados.get(
            Arrays.asList(opts).indexOf(sel)
          );
          JOptionPane.showMessageDialog(
            frame,
            String.join("\n", o.toLines()),
            "Orçamento Selecionado",
            JOptionPane.PLAIN_MESSAGE
          );
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(
          frame, "Erro na busca: " + ex.getMessage(),
          "Erro", JOptionPane.ERROR_MESSAGE
        );
      }
    });
  }
}