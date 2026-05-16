package com.fiap.hackaton.diagram_api.infrastructure.api.controller;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;
import com.fiap.hackaton.diagram_api.domain.exception.ResourceNotFoundException;
import com.fiap.hackaton.diagram_api.domain.model.Diagram;
import com.fiap.hackaton.diagram_api.domain.usecase.DiagramUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiagramController.class)
public class DiagramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DiagramUseCase diagramUseCase;

    @Test
    @DisplayName("Deve realizar upload de arquivo e retornar 200 OK com o ID")
    void shouldUploadFileSuccessfully() throws Exception {
        // Arrange
        UUID generatedId = UUID.randomUUID();
        String fileName = "diagrama.png";
        String contentType = "image/png";
        byte[] content = "conteudo-teste".getBytes();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                MediaType.IMAGE_PNG_VALUE,
                content
        );

        when(diagramUseCase.upload(eq(fileName), eq(contentType), any(byte[].class))).thenReturn(generatedId);

        // Act & Assert
        mockMvc.perform(multipart("/v1/diagrams").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(generatedId.toString()));
    }

    @Test
    @DisplayName("Deve retornar os detalhes do status quando o diagrama existir")
    void shouldGetStatusSuccessfully() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        Diagram mockDiagram = new Diagram("projeto.png", "image/png", new byte[]{1});
        mockDiagram.setId(id);
        mockDiagram.setStatus(DiagramStatus.PENDING);
        mockDiagram.setReportResult("Nenhum risco detectado");

        when(diagramUseCase.getStatus(id)).thenReturn(mockDiagram);

        // Act & Assert
        mockMvc.perform(get("/v1/diagrams/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.fileName").value("projeto.png"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.reportResult").value("Nenhum risco detectado"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando o diagrama não for encontrado")
    void shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        when(diagramUseCase.getStatus(id))
                .thenThrow(new ResourceNotFoundException("Diagrama não encontrado com o ID: " + id));

        // Act & Assert
        mockMvc.perform(get("/v1/diagrams/{id}", id))
                .andExpect(status().isNotFound());
    }

}
