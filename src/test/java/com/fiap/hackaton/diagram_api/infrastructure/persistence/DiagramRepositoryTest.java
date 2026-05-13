package com.fiap.hackaton.diagram_api.infrastructure.persistence;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;
import com.fiap.hackaton.diagram_api.domain.model.Diagram;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiagramRepositoryTest {

    @Mock
    private SpringDataDiagramRepository jpaRepository;

    @InjectMocks
    private DiagramRepository diagramRepository;

    @Test
    @DisplayName("Deve converter domínio para entidade e salvar")
    void shouldSaveDiagramSuccessfully() {
        // Arrange
        Diagram diagram = new Diagram("teste.png", new byte[]{1, 2, 3});

        // Act
        diagramRepository.save(diagram);

        // Assert
        ArgumentCaptor<DiagramEntity> entityCaptor = ArgumentCaptor.forClass(DiagramEntity.class);
        verify(jpaRepository).save(entityCaptor.capture());

        DiagramEntity savedEntity = entityCaptor.getValue();
        assertEquals(diagram.getId(), savedEntity.getId());
        assertEquals(diagram.getFileName(), savedEntity.getFileName());
        assertEquals(diagram.getStatus().name(), savedEntity.getStatus());
    }

    @Test
    @DisplayName("Deve buscar por ID e converter entidade para domínio")
    void shouldFindByIdAndConvertToDomain() {
        // Arrange
        UUID id = UUID.randomUUID();
        DiagramEntity entity = createMockEntity(id);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        Optional<Diagram> result = diagramRepository.findById(id);

        // Assert
        assertTrue(result.isPresent());
        Diagram diagram = result.get();
        assertEquals(entity.getId(), diagram.getId());
        assertEquals(entity.getFileName(), diagram.getFileName());
        assertEquals(DiagramStatus.PENDING, diagram.getStatus());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando ID não existir")
    void shouldReturnEmptyOptionalWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Diagram> result = diagramRepository.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar lista de diagramas convertidos")
    void shouldFindAllAndConvertList() {
        // Arrange
        DiagramEntity entity1 = createMockEntity(UUID.randomUUID());
        DiagramEntity entity2 = createMockEntity(UUID.randomUUID());
        when(jpaRepository.findAll()).thenReturn(List.of(entity1, entity2));

        // Act
        List<Diagram> result = diagramRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve deletar apenas se o registro existir")
    void shouldDeleteOnlyIfExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(jpaRepository.existsById(id)).thenReturn(true);

        // Act
        diagramRepository.delete(id);

        // Assert
        verify(jpaRepository).deleteById(id);
    }

    @Test
    @DisplayName("Não deve chamar delete se o ID não existir")
    void shouldNotDeleteIfDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(jpaRepository.existsById(id)).thenReturn(false);

        // Act
        diagramRepository.delete(id);

        // Assert
        verify(jpaRepository, never()).deleteById(any());
    }

    // Helper para criar entidade de teste
    private DiagramEntity createMockEntity(UUID id) {
        DiagramEntity entity = new DiagramEntity();
        entity.setId(id);
        entity.setFileName("arquivo.png");
        entity.setFileData(new byte[]{0});
        entity.setStatus("PENDING");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setNotes("Nota");
        return entity;
    }

}
