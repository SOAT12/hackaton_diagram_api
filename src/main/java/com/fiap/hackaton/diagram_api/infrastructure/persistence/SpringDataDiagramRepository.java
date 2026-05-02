package com.fiap.hackaton.diagram_api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataDiagramRepository extends JpaRepository<DiagramEntity, UUID> {
}
