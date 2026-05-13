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
        // Arrange
        String fileName = "projeto.png";
        byte[] fileData = "conteudo-binario".getBytes();

        // Act
        Diagram diagram = new Diagram(fileName, fileData);

        // Assert
        assertNotNull(diagram.getId());
        assertEquals(fileName, diagram.getFileName());
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

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Diagram(invalidFileName, fileData);
        });

        assertEquals("O nome do arquivo é obrigatório.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar diagrama com dados do arquivo nulos")
    void shouldThrowExceptionWhenFileDataIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Diagram("teste.jpg", null);
        }, "O conteúdo do arquivo não pode estar vazio.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar diagrama com dados do arquivo vazios")
    void shouldThrowExceptionWhenFileDataIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Diagram("teste.jpg", new byte[0]);
        });
    }

    @Test
    @DisplayName("Deve instanciar corretamente através do construtor completo")
    void shouldInstantiateWithFullConstructor() {
        // Arrange
        UUID id = UUID.randomUUID();
        String fileName = "completo.pdf";
        byte[] data = {10, 20};
        DiagramStatus status = DiagramStatus.PENDING;
        LocalDateTime now = LocalDateTime.now();
        String report = "Sucesso";
        String notes = "Observação";

        // Act
        Diagram diagram = new Diagram(id, fileName, data, status, now, report, notes);

        // Assert
        assertEquals(id, diagram.getId());
        assertEquals(fileName, diagram.getFileName());
        assertEquals(status, diagram.getStatus());
        assertEquals(report, diagram.getReportResult());
        assertEquals(notes, diagram.getNotes());
        assertEquals(now, diagram.getCreatedAt());
    }

}
