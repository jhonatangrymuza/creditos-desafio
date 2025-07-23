package com.credit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class EventPublisherService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_CONSULTAS = "creditos-consultas";

    public void publishConsultaEvent(String tipoConsulta, String parametro) {
        Map<String, Object> evento = new HashMap<>();
        evento.put("tipoConsulta", tipoConsulta);
        evento.put("parametro", parametro);
        evento.put("timestamp", LocalDateTime.now().toString());
        evento.put("usuario", "sistema");

        kafkaTemplate.send(TOPIC_CONSULTAS, evento);
    }
}