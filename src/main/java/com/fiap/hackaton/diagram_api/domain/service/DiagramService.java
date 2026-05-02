package com.fiap.hackaton.diagram_api.domain.service;

import com.fiap.hackaton.diagram_api.domain.gateway.DiagramGateway;
import com.fiap.hackaton.diagram_api.domain.gateway.QueueGateway;
import com.fiap.hackaton.diagram_api.domain.model.Diagram;
import com.fiap.hackaton.diagram_api.domain.usecase.DiagramUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiagramService implements DiagramUseCase {

    private final DiagramGateway diagramGateway;
    private final QueueGateway queueGateway;

    @Override
    public UUID upload(String fileName, byte[] data) {
        Diagram diagram = new Diagram(fileName, data);

        diagramGateway.save(diagram);
        //queueGateway.enqueue(diagram.getId());

        return diagram.getId();
    }

    @Override
    public Diagram getStatus(UUID id) {
        return diagramGateway.findById(id)
                .orElseThrow();
    }

}
