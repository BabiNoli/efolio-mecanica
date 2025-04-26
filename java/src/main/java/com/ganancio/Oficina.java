package com.ganancio;

import java.io.IOException;
import java.util.List;

/**
 * Ponto de entrada da aplicação Java. Recebe como argumento
 * uma lista de IDs de serviço (ex: "1,2,5") e exibe:
 *  - peças selecionadas
 *  - descontos em peças
 *  - mão-de-obra
 *  - custo fixo
 *  - total (usando comando orcamento_total)
 */
public class Oficina {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java -jar oficina.jar <ids_servicos>");
            System.out.println("Exemplo: java -jar oficina.jar 1,2,5");
            return;
        }
        String ids = args[0];
        try {
            System.out.println("=== Peças mais lucrativas ===");
            List<String> pecas = IntegradorOCaml.executar("orcamento_items", ids);
            pecas.forEach(System.out::println);

            System.out.println("\n=== Desconto em peças ===");
            List<String> descPecas = IntegradorOCaml.executar("orcamento_desconto_items", ids);
            descPecas.forEach(System.out::println);

            System.out.println("\n=== Mão-de-obra ===");
            List<String> mo = IntegradorOCaml.executar("orcamento_mecanico", ids);
            mo.forEach(System.out::println);

            System.out.println("\n=== Preço Fixo do Serviço ===");
            List<String> fixo = IntegradorOCaml.executar("orcamento_preco_fixo", ids);
            fixo.forEach(System.out::println);

            System.out.println("\n=== Orçamento Total ===");
            List<String> total = IntegradorOCaml.executar("orcamento_total", ids);
            total.forEach(System.out::println);

        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao gerar orçamento: " + e.getMessage());
        }
    }
}
