package com.fiap.hackaton.diagram_api.infrastructure.messaging;

import com.fiap.hackaton.diagram_api.domain.gateway.QueueGateway;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsQueueAdapter implements QueueGateway {

    private final SqsTemplate sqsTemplate;

    @Value("${spring.cloud.aws.sqs.queue-name}")
    private String queueName;

    @Override
    public void enqueue(UUID diagramId) {
        log.info("Enviando diagrama {} para a fila SQS: {}", diagramId, queueName);

        try {
            sqsTemplate.send(queueName, diagramId.toString());
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem para o SQS", e);
            throw new RuntimeException("Falha na integração com sistema de mensageria");
        }
    }
}
