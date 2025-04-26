// GerenciadorIDOrcamento.java
package com.ganancio.util;

import java.util.UUID;

public class GerenciadorIDOrcamento {
    /** Gera um ID único para cada novo orçamento */
    public static String gerarNovoID() {
        return UUID.randomUUID().toString();
    }
}
