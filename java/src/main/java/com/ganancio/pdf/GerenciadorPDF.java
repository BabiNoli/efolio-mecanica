// GerenciadorPDF.java
package com.ganancio.pdf;

import java.io.FileOutputStream;

import com.ganancio.model.ResumoOrcamento;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GerenciadorPDF {
    /** Gera um PDF a partir do ResumoOrcamento e salva em caminhoArquivo */
    public void gerarPDF(ResumoOrcamento resumo, String caminhoArquivo) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(caminhoArquivo));
        doc.open();

        // Título
        Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titulo = new Paragraph("Orçamento nº " + resumo.getIdOrcamento(), fonteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);
        doc.add(new Paragraph("Cliente: " + resumo.getNomeCliente()));
        doc.add(new Paragraph("Data de Orçamento: " + resumo.getDataOrcamento()));
        doc.add(Chunk.NEWLINE);

        // Peças
        PdfPTable tabelaP = new PdfPTable(4);
        tabelaP.addCell("ID");
        tabelaP.addCell("Peça");
        tabelaP.addCell("Bruto");
        tabelaP.addCell("Líquido");
        for (var p : resumo.getPecas()) {
            tabelaP.addCell(String.valueOf(p.getId()));
            tabelaP.addCell(p.getNome());
            tabelaP.addCell(String.format("%.2f", p.getPrecoBruto()));
            tabelaP.addCell(String.format("%.2f", p.getPrecoLiquido()));
        }
        doc.add(tabelaP);
        doc.add(Chunk.NEWLINE);

        // Mão de obra
        PdfPTable tabelaM = new PdfPTable(4);
        tabelaM.addCell("Serviço");
        tabelaM.addCell("Horas");
        tabelaM.addCell("Bruto");
        tabelaM.addCell("Líquido");
        for (var m : resumo.getMaoDeObra()) {
            tabelaM.addCell(String.valueOf(m.getIdServico()));
            tabelaM.addCell(String.format("%.2f", m.getHoras()));
            tabelaM.addCell(String.format("%.2f", m.getCustoBruto()));
            tabelaM.addCell(String.format("%.2f", m.getCustoLiquido()));
        }
        doc.add(tabelaM);
        doc.add(Chunk.NEWLINE);

        // Total
        Paragraph total = new Paragraph("Total: R$ " + String.format("%.2f", resumo.getTotal()), fonteTitulo);
        total.setAlignment(Element.ALIGN_RIGHT);
        doc.add(total);

        doc.close();
    }
}
