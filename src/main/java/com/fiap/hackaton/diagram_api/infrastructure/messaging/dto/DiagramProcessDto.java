package com.fiap.hackaton.diagram_api.infrastructure.messaging.dto;

import java.util.UUID;

public record DiagramProcessDto(
        UUID diagramId,
        String fileName,
        String contentType,
        String data
) {
}
