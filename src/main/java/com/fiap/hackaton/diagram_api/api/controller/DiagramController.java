package com.fiap.hackaton.diagram_api.api.controller;

import com.fiap.hackaton.diagram_api.api.dto.DiagramStatusResponseDto;
import com.fiap.hackaton.diagram_api.api.dto.DiagramUploadResponseDto;
import com.fiap.hackaton.diagram_api.domain.usecase.DiagramUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/v1/diagrams")
@RequiredArgsConstructor
public class DiagramController {

    private final DiagramUseCase diagramUseCase;

    @PostMapping
    public DiagramUploadResponseDto upload(@RequestParam("file") MultipartFile file) throws IOException {
        UUID id = diagramUseCase.upload(file.getOriginalFilename(), file.getBytes());
        return new DiagramUploadResponseDto(id);
    }

    @GetMapping("/{id}")
    public DiagramStatusResponseDto getStatus(@PathVariable UUID id) {
        var diagram = diagramUseCase.getStatus(id);

        return new DiagramStatusResponseDto(
                diagram.getId(),
                diagram.getFileName(),
                diagram.getStatus().name(),
                diagram.getReportResult()
        );
    }

}
