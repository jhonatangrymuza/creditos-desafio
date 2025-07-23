package com.credit.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.credit.entity.Credito;
import com.credit.repository.CreditoRepository;
import com.credit.service.EventPublisherService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CreditoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreditoRepository creditoRepository;

    @MockBean
    private EventPublisherService eventPublisherService;

    @BeforeEach
    void setUp() {
        creditoRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/creditos/credito/{numeroCredito} - Deve retornar 200 OK e o crédito quando encontrado")
    void buscarPorNumeroCredito_quandoEncontrado_retorna200() throws Exception {
        Credito credito = createValidCredito();
        creditoRepository.save(credito);

        mockMvc.perform(get("/api/creditos/credito/12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numeroCredito", is("12345")))
                .andExpect(jsonPath("$.numeroNfse", is("NFSE-001")))
                .andExpect(jsonPath("$.valorDeducao", is(234.50)));

        verify(eventPublisherService).publishConsultaEvent("CONSULTA_POR_CREDITO", "12345");
    }

    @Test
    @DisplayName("GET /api/creditos/credito/{numeroCredito} - Deve retornar 404 Not Found quando não encontrado")
    void buscarPorNumeroCredito_quandoNaoEncontrado_retorna404() throws Exception {
        mockMvc.perform(get("/api/creditos/credito/99999"))
                .andExpect(status().isNotFound());
    }

    private Credito createValidCredito() {
        Credito credito = new Credito();
        credito.setNumeroCredito("12345");
        credito.setNumeroNfse("NFSE-001");
        credito.setValorDeducao(new BigDecimal("234.50"));

        credito.setAliquota(new BigDecimal("5.00"));
        credito.setBaseCalculo(new BigDecimal("1000.00"));
        credito.setDataConstituicao(LocalDate.now());
        credito.setSimplesNacional(false);
        credito.setTipoCredito("TIPO_A");
        credito.setValorFaturado(new BigDecimal("1234.50"));
        credito.setValorIssqn(new BigDecimal("50.00"));
        return credito;
    }
}