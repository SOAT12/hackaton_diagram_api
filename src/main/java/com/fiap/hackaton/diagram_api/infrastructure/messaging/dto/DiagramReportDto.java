package com.fiap.hackaton.diagram_api.infrastructure.messaging.dto;

import java.util.List;

public record DiagramReportDto(
        List<String> components,
        List<String> risks,
        List<String> recommendations
) {}
