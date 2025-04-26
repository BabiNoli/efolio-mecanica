package com.ganancio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para invocar o executável OCaml (main.exe) via ProcessBuilder
 * e capturar sua saída.
 */
public class IntegradorOCaml {
    // Caminho relativo do executável OCaml (ajusta conforme o teu projeto)
    private static final String OCAML_EXE = "../ocaml/_build/default/src/main.exe";

    /**
     * Executa o comando OCaml e devolve cada linha da saída padrão.
     * @param comando nome do comando (ex: "orcamento_items", "valor_mao_obra", etc.)
     * @param args    argumento único a passar (ids separados por vírgula), ou null se não houver.
     * @return lista de linhas impressas pelo OCaml
     * @throws IOException          em caso de erro de I/O
     * @throws InterruptedException se o processo for interrompido
     */
    public static List<String> executar(String comando, String args)
            throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add(OCAML_EXE);
        cmd.add(comando);
        if (args != null && !args.isBlank()) {
            cmd.add(args);
        }

        ProcessBuilder pb = new ProcessBuilder(cmd);
        // Agora o cwd será ../ocaml, de dentro de java/, para encontrar database.pl
        pb.directory(new File("..", "ocaml"));
        Process proc = pb.start();

        List<String> linhas = new ArrayList<>();
        try (BufferedReader reader =
                 new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                linhas.add(line);
            }
        }

        int exitCode = proc.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("OCaml retornou código de erro: " + exitCode);
        }
        return linhas;
    }
}
