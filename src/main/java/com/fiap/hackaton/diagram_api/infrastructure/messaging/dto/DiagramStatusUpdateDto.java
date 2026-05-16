package com.fiap.hackaton.diagram_api.infrastructure.messaging.dto;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DiagramStatusUpdateDto(
        UUID diagramId,
        String title,
        DiagramStatus status,
        DiagramReportDto report,
        String reportLink,
        LocalDateTime createdAt,
        String notes
) {}