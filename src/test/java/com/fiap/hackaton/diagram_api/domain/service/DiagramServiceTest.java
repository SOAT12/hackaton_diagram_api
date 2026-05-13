package com.fiap.hackaton.diagram_api.domain.service;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;
import com.fiap.hackaton.diagram_api.domain.gateway.DiagramGateway;
import com.fiap.hackaton.diagram_api.domain.model.Diagram;
import com.fiap.hackaton.diagram_api.infrastructure.messaging.dto.DiagramReportDto;
import com.fiap.hackaton.diagram_api.infrastructure.messaging.dto.DiagramStatusUpdateDto;
import com.fiap.hackaton.diagram_api.infrastructure.messaging.publisher.DiagramProcessPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiagramServiceTest {

    @Mock
    private DiagramGateway diagramGateway;

    @Mock
    private DiagramProcessPublisher diagramProcessPublisher;

    @InjectMocks
    private DiagramService diagramService;

    @Test
    @DisplayName("Deve realizar upload, salvar e enfileirar processamento")
    void shouldUploadSuccessfully() {
        // Arrange
        String fileName = "teste.png";
        byte[] data = {1, 2, 3};

        // Act
        UUID resultId = diagramService.upload(fileName, data);

        // Assert
        assertNotNull(resultId);
        verify(diagramGateway, times(1)).save(any(Diagram.class));
        verify(diagramProcessPublisher, times(1)).enqueue(resultId);
    }

    @Test
    @DisplayName("Deve retornar diagrama quando o ID existir")
    void shouldGetStatusSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Diagram mockDiagram = new Diagram("file.png", new byte[]{1});
        when(diagramGateway.findById(id)).thenReturn(Optional.of(mockDiagram));

        // Act
        Diagram result = diagramService.getStatus(id);

        // Assert
        assertEquals(mockDiagram.getFileName(), result.getFileName());
        verify(diagramGateway).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void shouldThrowExceptionWhenIdNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(diagramGateway.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            diagramService.getStatus(id);
        });

        assertTrue(exception.getMessage().contains("Diagrama não encontrado"));
    }

    @Test
    @DisplayName("Deve atualizar status e notas com sucesso")
    void shouldUpdateStatusSuccessfully() {
        // Arrange
        UUID diagramId = UUID.randomUUID();
        Diagram existingDiagram = new Diagram("projeto_original.png", new byte[]{1, 2, 3});
        DiagramReportDto reportDto = new DiagramReportDto(
                List.of("S3 Bucket", "EC2 Instance"),
                List.of("Bucket público"),
                List.of("Fechar acesso público")
        );
        DiagramStatusUpdateDto updateDto = new DiagramStatusUpdateDto(
                diagramId,
                "Novo Título",
                DiagramStatus.COMPLETED,
                reportDto,
                LocalDateTime.now(),
                "Processamento concluído com sucesso"
        );

        when(diagramGateway.findById(diagramId)).thenReturn(Optional.of(existingDiagram));

        // Act
        diagramService.updateStatus(updateDto);

        // Assert
        assertEquals(DiagramStatus.COMPLETED, existingDiagram.getStatus());
        assertEquals("Processamento concluído com sucesso", existingDiagram.getNotes());
        verify(diagramGateway).save(existingDiagram);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar diagrama inexistente")
    void shouldThrowExceptionOnUpdateWhenNotFound() {
        // Arrange
        UUID missingId = UUID.randomUUID();
        DiagramStatusUpdateDto dto = new DiagramStatusUpdateDto(
                missingId, "Title", DiagramStatus.FAILED, null, LocalDateTime.now(), "Error"
        );
        when(diagramGateway.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            diagramService.updateStatus(dto);
        });

        verify(diagramGateway, never()).save(any());
    }

}
