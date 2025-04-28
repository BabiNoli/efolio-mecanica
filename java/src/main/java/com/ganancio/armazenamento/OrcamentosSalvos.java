package com.ganancio.armazenamento;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import com.ganancio.modelo.Orcamento;

public class OrcamentosSalvos {
    private static final Path PASTA = Paths.get("orcamentos");

    /** Salva um orçamento em arquivo dentro de orcamentos/ */
    public static Path salvar(Orcamento o) throws IOException {
        if (!Files.exists(PASTA)) {
        Files.createDirectories(PASTA);
        }
        Path arq = PASTA.resolve("orcamento_" + o.getId() + ".txt");
        List<String> linhas = o.toLines();
        return Files.write(arq, linhas, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static List<Orcamento> listar() throws IOException {
        if (!Files.exists(PASTA)) return Collections.emptyList();
        try (Stream<Path> stream = Files.list(PASTA)) {
        return stream
            .filter(p -> p.toString().endsWith(".txt"))
            .map(OrcamentosSalvos::ler)
            .collect(Collectors.toList());
        }
    }

    private static Orcamento ler(Path p) {
        try {
        List<String> lines = Files.readAllLines(p);
        String id   = lines.get(0).split(":",2)[1].trim();
        String cli  = lines.get(1).split(":",2)[1].trim();
        String nasc = lines.get(2).split(":",2)[1].trim();
        String data = lines.get(3).split(":",2)[1].trim();
        List<String> itens = lines.subList(5, lines.size());
        return new Orcamento(id, cli, nasc, data, itens);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static List<Orcamento> buscar(String tipo, String valor) throws IOException {
        return listar().stream()
        .filter(o -> {
            switch(tipo) {
            case "ID Orçamento":       return o.getId().equals(valor);
            case "Nome do Cliente":    return o.getCliente().equalsIgnoreCase(valor);
            case "Data do Orçamento":  return o.getDataOrcamento().equals(valor);
            case "Data de Nascimento": return o.getDataNascimento().equals(valor);
            default: return false;
            }
        })
        .collect(Collectors.toList());
    }

}
