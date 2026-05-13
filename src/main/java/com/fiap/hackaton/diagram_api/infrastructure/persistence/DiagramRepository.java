package com.fiap.hackaton.diagram_api.infrastructure.persistence;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;
import com.fiap.hackaton.diagram_api.domain.gateway.DiagramGateway;
import com.fiap.hackaton.diagram_api.domain.model.Diagram;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DiagramRepository implements DiagramGateway {

    private final SpringDataDiagramRepository jpaRepository;

    @Override
    public void save(Diagram diagram) {
        DiagramEntity entity = DiagramEntity.fromDomain(diagram);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Diagram> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(entity -> new Diagram(
                        entity.getId(),
                        entity.getFileName(),
                        entity.getFileData(),
                        DiagramStatus.valueOf(entity.getStatus()),
                        entity.getCreatedAt(),
                        entity.getReportResult(),
                        entity.getNotes()
                ));
    }

    @Override
    public List<Diagram> findAll() {
        return jpaRepository.findAll().stream()
                .map(entity -> new Diagram(
                        entity.getId(),
                        entity.getFileName(),
                        entity.getFileData(),
                        DiagramStatus.valueOf(entity.getStatus()),
                        entity.getCreatedAt(),
                        entity.getReportResult(),
                        entity.getNotes()
                ))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id);
        }
    }

}
