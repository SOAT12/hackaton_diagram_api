package com.fiap.hackaton.diagram_api.infrastructure.client;

import com.fiap.hackaton.diagram_api.infrastructure.messaging.dto.DiagramStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class DiagramReportClient {

    private final RestClient restClient;

    public void sendReport(DiagramStatusUpdateDto dto) {
        restClient.post()
                .uri("/api/reports")
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

}
