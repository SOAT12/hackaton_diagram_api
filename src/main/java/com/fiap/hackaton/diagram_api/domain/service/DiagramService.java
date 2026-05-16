package com.fiap.hackaton.diagram_api.domain.service;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;
import com.fiap.hackaton.diagram_api.domain.exception.ResourceNotFoundException;
import com.fiap.hackaton.diagram_api.domain.gateway.DiagramGateway;
import com.fiap.hackaton.diagram_api.domain.model.Diagram;
import com.fiap.hackaton.diagram_api.domain.usecase.DiagramUseCase;
import com.fiap.hackaton.diagram_api.infrastructure.messaging.dto.DiagramStatusUpdateDto;
import com.fiap.hackaton.diagram_api.infrastructure.messaging.publisher.DiagramProcessPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiagramService implements DiagramUseCase {

    private final DiagramGateway diagramGateway;
    private final DiagramProcessPublisher diagramProcessPublisher;

    @Override
    public UUID upload(String fileName, String contentType, byte[] data) {
        Diagram diagram = new Diagram(fileName, contentType, data);

        diagramGateway.save(diagram);
        diagramProcessPublisher.enqueue(diagram);

        return diagram.getId();
    }

    @Override
    public Diagram getStatus(UUID id) {
        return this.findByIdOrElseThrow(id);
    }

    @Override
    public void updateStatus(DiagramStatusUpdateDto diagramStatusUpdateDto) {
        var diagram = this.findByIdOrElseThrow(diagramStatusUpdateDto.diagramId());

        if(DiagramStatus.PROCESSED.equals(diagramStatusUpdateDto.status())) {
            // TODO - Implementar chamada à Report API
        }

        diagram.setStatus(diagramStatusUpdateDto.status());
        diagram.setReportResult(diagramStatusUpdateDto.reportLink());
        diagram.setNotes(diagramStatusUpdateDto.notes());
        diagramGateway.save(diagram);
    }

    private Diagram findByIdOrElseThrow(UUID id) {
        return diagramGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diagrama não encontrado com o ID: " + id));
    }

}
