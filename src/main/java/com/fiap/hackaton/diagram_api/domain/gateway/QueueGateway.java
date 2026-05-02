package com.fiap.hackaton.diagram_api.domain.gateway;

import java.util.UUID;

public interface QueueGateway {

    void enqueue(UUID diagramId);

}
