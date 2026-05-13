package com.fiap.hackaton.diagram_api.infrastructure.messaging.listener;

import com.fiap.hackaton.diagram_api.domain.enumeration.DiagramStatus;
import com.fiap.hackaton.diagram_api.domain.usecase.DiagramUseCase;
import com.fiap.hackaton.diagram_api.infrastructure.messaging.dto.DiagramStatusUpdateDto;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiagramStatusUpdateListener {

    private final DiagramUseCase diagramUseCase;

    @SqsListener("diagram.status.update")
    public void onDiagramUpdate(@Valid DiagramStatusUpdateDto message) {
        log.info("Mensagem recebida para o Diagrama ID: {}", message.diagramId());
        diagramUseCase.updateStatus(message);
    }

}
