package com.fiap.hackaton.diagram_api.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DiagramStatusResponseDto(
        UUID id,
        String fileName,
        String status,
        String reportResult
) {
}
