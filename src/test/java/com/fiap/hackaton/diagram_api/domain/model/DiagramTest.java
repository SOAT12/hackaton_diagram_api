package com.fiap.hackaton.diagram_api.domain.model;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DiagramTest {

    @Test
    @DisplayName("Deve criar um diagrama com sucesso quando os dados forem válidos")
    void shouldCreateDiagramSuccessfully() {
        String fileName = "projeto.png";
        String contentType = "image/png";
        byte[] fileData = "conteudo-binario".getBytes();

        Diagram diagram = new Diagram(fileName, contentType, fileData);

        assertNotNull(diagram.getId());
        assertEquals(fileName, diagram.getFileName());
        assertEquals(contentType, diagram.getContentType());
        assertArrayEquals(fileData, diagram.getFileData());
        assertEquals(DiagramStatus.PENDING, diagram.getStatus());
        assertNotNull(diagram.getCreatedAt());
        assertNull(diagram.getUpdatedAt());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t"})
    @DisplayName("Deve lançar exceção ao tentar criar diagrama com nome de arquivo inválido")
    void shouldThrowExceptionWhenFileNameIsInvalid(String invalidFileName) {
        byte[] fileData = {1, 2, 3};

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Diagram(invalidFileName, "image/png", fileData)
        );

        assertEquals("O nome do arquivo é obrigatório.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar diagrama com dados do arquivo nulos")
    void shouldThrowExceptionWhenFileDataIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                        new Diagram("teste.jpg", "image/jpeg", null),
                "O conteúdo do arquivo não pode estar vazio.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar diagrama com dados do arquivo vazios")
    void shouldThrowExceptionWhenFileDataIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                new Diagram("teste.jpg", "image/jpeg", new byte[0])
        );
    }

    @Test
    @DisplayName("Deve instanciar corretamente através do construtor completo")
    void shouldInstantiateWithFullConstructor() {
        UUID id = UUID.randomUUID();
        String fileName = "completo.pdf";
        String contentType = "application/pdf";
        byte[] data = {10, 20};
        DiagramStatus status = DiagramStatus.PENDING;
        LocalDateTime now = LocalDateTime.now();
        String report = "Sucesso";
        String notes = "Observação";

        Diagram diagram = new Diagram(id, fileName, contentType, data, status, now, report, notes);

        assertEquals(id, diagram.getId());
        assertEquals(fileName, diagram.getFileName());
        assertEquals(contentType, diagram.getContentType());
        assertEquals(status, diagram.getStatus());
        assertEquals(report, diagram.getReportResult());
        assertEquals(notes, diagram.getNotes());
        assertEquals(now, diagram.getCreatedAt());
    }

}
