package com.fiap.hackaton.diagram_api.domain.usecase;

import com.fiap.hackaton.diagram_api.domain.model.Diagram;
import com.fiap.hackaton.diagram_api.infrastructure.messaging.dto.DiagramStatusUpdateDto;

import java.util.UUID;

public interface DiagramUseCase {
    UUID upload(String fileName, byte[] data);
    Diagram getStatus(UUID id);
    void updateStatus(DiagramStatusUpdateDto diagramStatusUpdateDto);
}
