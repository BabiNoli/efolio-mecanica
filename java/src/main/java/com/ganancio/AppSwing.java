package com.ganancio;

import javax.swing.SwingUtilities;

import com.ganancio.controller.ControladorOrcamento;

public class AppSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ControladorOrcamento().iniciar();
        });
    }
}
