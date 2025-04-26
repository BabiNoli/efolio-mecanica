// JanelaPrincipal.java
package com.ganancio.ui;

import com.ganancio.IntegradorOCaml;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class JanelaPrincipal extends JFrame {
    public static final String BOAS_VINDAS      = "BoasVindas";
    public static final String NOVO_ORCAMENTO   = "NovoOrcamento";
    public static final String BUSCAR_ORCAMENTO = "BuscarOrcamento";
    public static final String RESULTADOS       = "Resultados";
    public static final String EXIBIR_ORCAMENTO = "ExibirOrcamento";

    private CardLayout               gerenciadorCartoes;
    private JPanel                   painelConteudo;
    private Map<String,JPanel>       mapaPaineis;

    public JanelaPrincipal() {
        super("Sistema de Orçamentos – Oficina Ganâncio");
        inicializarJanela();
        inicializarPaineis();
        configurarAcoes();
    }

    private void inicializarJanela() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void inicializarPaineis() {
        gerenciadorCartoes = new CardLayout();
        painelConteudo     = new JPanel(gerenciadorCartoes);
        mapaPaineis        = new HashMap<>();

        adicionarPainel(new PainelBoasVindas(),      BOAS_VINDAS);
        adicionarPainel(new PainelNovoOrcamento(),   NOVO_ORCAMENTO);
        adicionarPainel(new PainelBuscarOrcamento(), BUSCAR_ORCAMENTO);
        adicionarPainel(new PainelResultados(),      RESULTADOS);
        adicionarPainel(new PainelExibirOrcamento(), EXIBIR_ORCAMENTO);

        getContentPane().add(painelConteudo);
        mostrarPainel(BOAS_VINDAS);
    }

    private void configurarAcoes() {
        // 1) Boas-vindas
        PainelBoasVindas painelBoas = (PainelBoasVindas) obterPainel(BOAS_VINDAS);
        painelBoas.adicionarAcaoNovoOrcamento(e -> mostrarPainel(NOVO_ORCAMENTO));
        painelBoas.adicionarAcaoBuscarOrcamento(e -> mostrarPainel(BUSCAR_ORCAMENTO));
        painelBoas.adicionarAcaoSair(e -> System.exit(0));

        // 2) Novo Orçamento
        PainelNovoOrcamento painelNovo = (PainelNovoOrcamento) obterPainel(NOVO_ORCAMENTO);
        painelNovo.adicionarAcaoVoltar(e -> mostrarPainel(BOAS_VINDAS));
        painelNovo.adicionarAcaoGerar(e -> {
            try {
                String ids = painelNovo.getIdsServicos();
                // Chamamos o OCaml para obter o orçamento completo
                List<String> linhas = IntegradorOCaml.executar("orcamento_total", ids);
                PainelExibirOrcamento painelExibir = (PainelExibirOrcamento) obterPainel(EXIBIR_ORCAMENTO);
                painelExibir.exibirLinhas(linhas);
                mostrarPainel(EXIBIR_ORCAMENTO);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro gerando orçamento:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 3) Buscar Orçamento
        PainelBuscarOrcamento painelBuscar = (PainelBuscarOrcamento) obterPainel(BUSCAR_ORCAMENTO);
        painelBuscar.adicionarAcaoVoltar(e -> mostrarPainel(BOAS_VINDAS));
        painelBuscar.adicionarAcaoPesquisar(e -> {
            String criterio = painelBuscar.getCriterioSelecionado();
            String valor    = painelBuscar.getTextoPesquisa();
            // TODO: buscar no JSON por criterio/valor
            List<String> resultados = /* RepositorioJson.buscar(criterio, valor) */ List.of();
            PainelResultados painelRes = (PainelResultados) obterPainel(RESULTADOS);
            painelRes.atualizarResultados(resultados);
            mostrarPainel(RESULTADOS);
        });

        // 4) Resultados da Busca
        PainelResultados painelRes = (PainelResultados) obterPainel(RESULTADOS);
        painelRes.adicionarAcaoVoltar(e -> mostrarPainel(BUSCAR_ORCAMENTO));
        painelRes.adicionarAcaoSelecionar(e -> {
            String linhaSelecionada = painelRes.obterValorSelecionado();
            // TODO: extrair ID do orçamento da linha e carregar do JSON
            List<String> linhas = /* RepositorioJson.carregar(id) */ List.of();
            PainelExibirOrcamento painelExibir = (PainelExibirOrcamento) obterPainel(EXIBIR_ORCAMENTO);
            painelExibir.exibirLinhas(linhas);
            mostrarPainel(EXIBIR_ORCAMENTO);
        });

        // 5) Exibir Orçamento
        PainelExibirOrcamento painelExibir = (PainelExibirOrcamento) obterPainel(EXIBIR_ORCAMENTO);
        painelExibir.adicionarAcaoVoltar(e -> mostrarPainel(BOAS_VINDAS));
        painelExibir.adicionarAcaoExcluir(e -> {
            // simplesmente descarta e volta ao início
            mostrarPainel(BOAS_VINDAS);
        });
        painelExibir.adicionarAcaoSalvar(e -> {
            // TODO: salvar JSON usando cliente, data e linhas
            JOptionPane.showMessageDialog(this, "Orçamento salvo com sucesso!");
            mostrarPainel(BOAS_VINDAS);
        });
        painelExibir.adicionarAcaoGerarPdf(e -> {
            // TODO: gerar PDF via iText ou similar
            JOptionPane.showMessageDialog(this, "PDF gerado com sucesso!");
        });
    }

    public void adicionarPainel(JPanel painel, String nome) {
        painelConteudo.add(painel, nome);
        mapaPaineis.put(nome, painel);
    }

    public void mostrarPainel(String nome) {
        gerenciadorCartoes.show(painelConteudo, nome);
    }

    public JPanel obterPainel(String nome) {
        return mapaPaineis.get(nome);
    }
}
