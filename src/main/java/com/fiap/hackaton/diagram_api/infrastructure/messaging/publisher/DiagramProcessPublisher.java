package com.fiap.hackaton.diagram_api.infrastructure.messaging.publisher;

import com.fiap.hackaton.diagram_api.domain.model.Diagram;
import com.fiap.hackaton.diagram_api.infrastructure.messaging.dto.DiagramProcessDto;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiagramProcessPublisher {

    private final SqsTemplate sqsTemplate;

    @Value("${spring.cloud.aws.sqs.queue-diagram-process}")
    private String queueName;

    public void enqueue(Diagram diagram) {
        log.info("Enviando diagrama {} para a fila SQS: {}", diagram.getId(), queueName);

        DiagramProcessDto dto = new DiagramProcessDto(
                diagram.getId(),
                diagram.getFileName(),
                diagram.getContentType(),
                Base64.getEncoder().encodeToString(diagram.getFileData())
        );

        try {
            sqsTemplate.send(queueName, dto);
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem para o SQS", e);
            throw new RuntimeException("Falha na integração com sistema de mensageria");
        }
    }
}
