package com.fiap.hackaton.diagram_api.infrastructure.persistence;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;
import com.fiap.hackaton.diagram_api.domain.model.Diagram;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "diagrams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagramEntity {

    @Id
    private UUID id;

    private String fileName;

    @JdbcTypeCode(Types.BINARY)
    @Column(name = "file_data")
    private byte[] fileData;

    private String status;

    private LocalDateTime createdAt;

    private String reportResult;

    private String notes;

    public static DiagramEntity fromDomain(Diagram diagram) {
        return new DiagramEntity(
                diagram.getId(),
                diagram.getFileName(),
                diagram.getFileData(),
                diagram.getStatus().name(),
                diagram.getCreatedAt(),
                diagram.getReportResult(),
                diagram.getNotes()
        );
    }

}
