package com.fiap.hackaton.diagram_api.domain.gateway;

import com.fiap.hackaton.diagram_api.domain.model.Diagram;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiagramGateway {

    void save(Diagram diagram);

    Optional<Diagram> findById(UUID id);

    List<Diagram> findAll();

    void delete(UUID id);

}
