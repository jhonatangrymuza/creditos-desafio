package com.credit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventPublisherServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ListenableFuture<SendResult<String, Object>> sendResultFuture;

    @InjectMocks
    private EventPublisherService eventPublisherService;

    @Test
    public void publishConsultaEvent_comParametrosValidos_deveEnviarEventoParaKafka() {
        String tipoConsulta = "BUSCA_POR_NUMERO_CREDITO";
        String parametro = "123456";

        when(kafkaTemplate.send(anyString(), any(Map.class))).thenReturn(sendResultFuture);

        eventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate, times(1)).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;
            return eventoMap.get("tipoConsulta").equals("BUSCA_POR_NUMERO_CREDITO") &&
                    eventoMap.get("parametro").equals("123456") &&
                    eventoMap.get("usuario").equals("sistema") &&
                    eventoMap.containsKey("timestamp");
        }));
    }

    @Test
    public void publishConsultaEvent_comTipoConsultaNfse_deveEnviarEventoCorreto() {
        String tipoConsulta = "BUSCA_POR_NUMERO_NFSE";
        String parametro = "7891011";

        when(kafkaTemplate.send(anyString(), any(Map.class))).thenReturn(sendResultFuture);

        eventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate, times(1)).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;
            return eventoMap.get("tipoConsulta").equals("BUSCA_POR_NUMERO_NFSE") &&
                    eventoMap.get("parametro").equals("7891011") &&
                    eventoMap.get("usuario").equals("sistema") &&
                    eventoMap.containsKey("timestamp");
        }));
    }

    @Test
    public void publishConsultaEvent_comParametroNulo_deveEnviarEventoComParametroNulo() {
        String tipoConsulta = "BUSCA_GERAL";
        String parametro = null;

        when(kafkaTemplate.send(anyString(), any(Map.class))).thenReturn(sendResultFuture);

        eventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate, times(1)).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;
            return eventoMap.get("tipoConsulta").equals("BUSCA_GERAL") &&
                    eventoMap.get("parametro") == null &&
                    eventoMap.get("usuario").equals("sistema") &&
                    eventoMap.containsKey("timestamp");
        }));
    }

    @Test
    public void publishConsultaEvent_comParametroVazio_deveEnviarEventoComParametroVazio() {
        String tipoConsulta = "BUSCA_VAZIA";
        String parametro = "";

        when(kafkaTemplate.send(anyString(), any(Map.class))).thenReturn(sendResultFuture);

        eventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate, times(1)).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;
            return eventoMap.get("tipoConsulta").equals("BUSCA_VAZIA") &&
                    eventoMap.get("parametro").equals("") &&
                    eventoMap.get("usuario").equals("sistema") &&
                    eventoMap.containsKey("timestamp");
        }));
    }

    @Test
    public void publishConsultaEvent_deveEnviarParaTopicoCorreto() {
        String tipoConsulta = "TESTE";
        String parametro = "VALOR_TESTE";

        when(kafkaTemplate.send(anyString(), any(Map.class))).thenReturn(sendResultFuture);

        eventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate, times(1)).send(eq("creditos-consultas"), any(Map.class));
    }

    @Test
    public void publishConsultaEvent_deveIncluirTimestampNoEvento() {
        String tipoConsulta = "BUSCA_COM_TIMESTAMP";
        String parametro = "12345";

        when(kafkaTemplate.send(anyString(), any(Map.class))).thenReturn(sendResultFuture);

        LocalDateTime antesDoEvento = LocalDateTime.now().minusSeconds(1);
        eventPublisherService.publishConsultaEvent(tipoConsulta, parametro);
        LocalDateTime depoisDoEvento = LocalDateTime.now().plusSeconds(1);

        verify(kafkaTemplate, times(1)).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;
            String timestamp = (String) eventoMap.get("timestamp");

            try {
                LocalDateTime timestampEvento = LocalDateTime.parse(timestamp);
                return timestampEvento.isAfter(antesDoEvento) && timestampEvento.isBefore(depoisDoEvento);
            } catch (Exception e) {
                return false;
            }
        }));
    }

    @Test
    public void publishConsultaEvent_deveDefinirUsuarioComoSistema() {
        String tipoConsulta = "TESTE_USUARIO";
        String parametro = "teste";

        when(kafkaTemplate.send(anyString(), any(Map.class))).thenReturn(sendResultFuture);

        eventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate, times(1)).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;
            return "sistema".equals(eventoMap.get("usuario"));
        }));
    }

    @Test
    public void publishConsultaEvent_multiplisChamadas_deveEnviarMultiplosEventos() {
        when(kafkaTemplate.send(anyString(), any(Map.class))).thenReturn(sendResultFuture);

        eventPublisherService.publishConsultaEvent("TIPO_1", "PARAM_1");
        eventPublisherService.publishConsultaEvent("TIPO_2", "PARAM_2");
        eventPublisherService.publishConsultaEvent("TIPO_3", "PARAM_3");

        verify(kafkaTemplate, times(3)).send(eq("creditos-consultas"), any(Map.class));

        verify(kafkaTemplate).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;
            return "TIPO_1".equals(eventoMap.get("tipoConsulta")) &&
                    "PARAM_1".equals(eventoMap.get("parametro"));
        }));

        verify(kafkaTemplate).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;
            return "TIPO_2".equals(eventoMap.get("tipoConsulta")) &&
                    "PARAM_2".equals(eventoMap.get("parametro"));
        }));

        verify(kafkaTemplate).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;
            return "TIPO_3".equals(eventoMap.get("tipoConsulta")) &&
                    "PARAM_3".equals(eventoMap.get("parametro"));
        }));
    }

    @Test
    public void publishConsultaEvent_verificarEstruturalCompleta() {
        String tipoConsulta = "VERIFICACAO_COMPLETA";
        String parametro = "TESTE_COMPLETO";

        when(kafkaTemplate.send(anyString(), any(Map.class))).thenReturn(sendResultFuture);

        eventPublisherService.publishConsultaEvent(tipoConsulta, parametro);

        verify(kafkaTemplate, times(1)).send(eq("creditos-consultas"), argThat(evento -> {
            Map<String, Object> eventoMap = (Map<String, Object>) evento;

            if (eventoMap.size() != 4) return false;

            return eventoMap.containsKey("tipoConsulta") &&
                    eventoMap.containsKey("parametro") &&
                    eventoMap.containsKey("timestamp") &&
                    eventoMap.containsKey("usuario") &&
                    eventoMap.get("tipoConsulta") instanceof String &&
                    eventoMap.get("parametro") instanceof String &&
                    eventoMap.get("timestamp") instanceof String &&
                    eventoMap.get("usuario") instanceof String;
        }));
    }
}