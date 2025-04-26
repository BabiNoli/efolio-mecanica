// RepositorioJSON.java
package com.ganancio.repositorio;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ganancio.model.ResumoOrcamento;

public class RepositorioJSON {
    private static final Path ARQUIVO = Paths.get(System.getProperty("user.dir"), "java", "orcamentos.json");
     
    private final ObjectMapper mapper;

    public RepositorioJSON() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());            // para datas (LocalDate)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // datas no formato ISO
    }

    public void salvar(ResumoOrcamento resumo) throws Exception {
        List<ResumoOrcamento> todos = carregarTodos();
        todos.add(resumo);
        mapper.writerWithDefaultPrettyPrinter()
             .writeValue(ARQUIVO.toFile(), todos);
    }

    public List<ResumoOrcamento> carregarTodos() {
        try {
            // converte o Path em File
            File f = ARQUIVO.toFile();
            if (!f.exists()) {
                // opcional: garante que o diretório exista
                f.getParentFile().mkdirs();
                return new ArrayList<>();
            }
            return mapper.readValue(f, new TypeReference<List<ResumoOrcamento>>() {});
        } catch (Exception e) {
            e.printStackTrace(); // para ajudar a identificar erros
            return new ArrayList<>();
        }
    }

    public List<ResumoOrcamento> buscar(String criterio, String valor) {
        return carregarTodos().stream()
            .filter(r -> {
                switch (criterio) {
                    case "ID do Orçamento":
                        return r.getIdOrcamento().equalsIgnoreCase(valor);
                    case "Nome do Cliente":
                        return r.getNomeCliente().toLowerCase().contains(valor.toLowerCase());
                    case "Data de Nascimento":
                        return r.getDataNascimento().equals(valor);
                    case "Data do Orçamento":
                        return r.getDataOrcamento().toString().equals(valor);
                    default:
                        return false;
                }
            })
            .collect(Collectors.toList());
    }

    public ResumoOrcamento carregar(String id) {
        return carregarTodos().stream()
            .filter(r -> r.getIdOrcamento().equals(id))
            .findFirst().orElse(null);
    }
}
