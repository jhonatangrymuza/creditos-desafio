package com.credit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.credit.dto.CreditoDTO;
import com.credit.exception.CreditoNotFoundException;
import com.credit.service.CreditoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CreditoController.class)
public class CreditoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditoService creditoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void buscarPorNumeroNfse_quandoEncontrado_retorna200() throws Exception {
        List<CreditoDTO> creditos = Arrays.asList(
                criarCreditoDTO("123456", "7891011"),
                criarCreditoDTO("789012", "7891011")
        );
        when(creditoService.buscarPorNumeroNfse("7891011")).thenReturn(creditos);

        mockMvc.perform(get("/api/creditos/{numeroNfse}", "7891011"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].numeroCredito").value("123456"))
                .andExpect(jsonPath("$[1].numeroCredito").value("789012"))
                .andExpect(jsonPath("$[0].numeroNfse").value("7891011"))
                .andExpect(jsonPath("$[1].numeroNfse").value("7891011"))
                .andExpect(jsonPath("$[0].valorIssqn").value(1500.75));
    }

    @Test
    public void buscarPorNumeroNfse_quandoNaoEncontrado_retornaListaVazia() throws Exception {
        when(creditoService.buscarPorNumeroNfse("999999")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/creditos/{numeroNfse}", "999999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }



    @Test
    public void buscarPorNumeroCredito_quandoEncontrado_retorna200() throws Exception {
        CreditoDTO credito = criarCreditoDTO("123456", "7891011");
        when(creditoService.buscarPorNumeroCredito("123456")).thenReturn(credito);

        mockMvc.perform(get("/api/creditos/credito/{numeroCredito}", "123456"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numeroCredito").value("123456"))
                .andExpect(jsonPath("$.numeroNfse").value("7891011"))
                .andExpect(jsonPath("$.valorIssqn").value(1500.75))
                .andExpect(jsonPath("$.tipoCredito").value("ISSQN"));
    }

    @Test
    public void buscarPorNumeroCredito_quandoNaoEncontrado_retorna404() throws Exception {
        when(creditoService.buscarPorNumeroCredito("999999"))
                .thenThrow(new CreditoNotFoundException("Crédito não encontrado com o número: 999999"));

        mockMvc.perform(get("/api/creditos/credito/{numeroCredito}", "999999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Não encontrado"))
                .andExpect(jsonPath("$.message").value("Crédito não encontrado com o número: 999999"))
                .andExpect(jsonPath("$.status").value(404));
    }



    @Test
    public void buscarPorNumeroCredito_comParametroComEspacos_retorna200() throws Exception {
        CreditoDTO credito = criarCreditoDTO("ABC 123", "7891011");
        when(creditoService.buscarPorNumeroCredito("ABC 123")).thenReturn(credito);

        mockMvc.perform(get("/api/creditos/credito/{numeroCredito}", "ABC 123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCredito").value("ABC 123"));
    }

    @Test
    public void verificarCorsHeaders() throws Exception {
        when(creditoService.buscarPorNumeroNfse("7891011")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/creditos/7891011")
                        .header("Origin", "http://localhost:3000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

    private CreditoDTO criarCreditoDTO(String numeroCredito, String numeroNfse) {
        CreditoDTO dto = new CreditoDTO();
        dto.setNumeroCredito(numeroCredito);
        dto.setNumeroNfse(numeroNfse);
        dto.setDataConstituicao(LocalDate.of(2024, 2, 25));
        dto.setValorIssqn(new BigDecimal("1500.75"));
        dto.setTipoCredito("ISSQN");
        dto.setAliquota(new BigDecimal("5.0"));
        dto.setValorFaturado(new BigDecimal("30000.00"));
        dto.setValorDeducao(new BigDecimal("5000.00"));
        dto.setBaseCalculo(new BigDecimal("25000.00"));
        return dto;
    }

}