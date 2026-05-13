package com.fiap.hackaton.diagram_api.domain.model;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Diagram {

    private UUID id;
    private String fileName;
    private DiagramStatus status;
    private byte[] fileData;
    private String reportResult;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Diagram(String fileName, byte[] fileData) {
        validate(fileName, fileData);

        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.fileData = fileData;
        this.status = DiagramStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Diagram(UUID id, String fileName, byte[] fileData, DiagramStatus status, LocalDateTime createdAt, String reportResult, String notes) {
        this.id = id;
        this.fileName = fileName;
        this.fileData = fileData;
        this.status = status;
        this.createdAt = createdAt;
        this.reportResult = reportResult;
        this.notes = notes;
    }

    private void validate(String fileName, byte[] fileData) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("O nome do arquivo é obrigatório.");
        }
        if (fileData == null || fileData.length == 0) {
            throw new IllegalArgumentException("O conteúdo do arquivo não pode estar vazio.");
        }
    }

}
