package com.credit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.credit.entity.Credito;
import com.credit.exception.CreditoNotFoundException;
import com.credit.mapper.CreditoMapper;
import com.credit.repository.CreditoRepository;

@ExtendWith(MockitoExtension.class)
class CreditoServiceTest {

    @Mock
    private CreditoRepository creditoRepository;

    @Mock
    private CreditoMapper creditoMapper;

    @Mock
    private EventPublisherService eventPublisherService;

    @InjectMocks
    private CreditoService creditoService;

    @Test
    void buscarPorNumeroNfse_DeveRetornarCreditos_QuandoEncontrados() {
        String numeroNfse = "7891011";
        Credito credito = criarCreditoTeste();
        when(creditoRepository.findByNumeroNfse(numeroNfse))
                .thenReturn(Arrays.asList(credito));

        creditoService.buscarPorNumeroNfse(numeroNfse);

        verify(creditoRepository).findByNumeroNfse(numeroNfse);
        verify(eventPublisherService).publishConsultaEvent("CONSULTA_POR_NFSE", numeroNfse);
    }

    @Test
    void buscarPorNumeroNfse_DeveLancarExcecao_QuandoNaoEncontrado() {
        String numeroNfse = "inexistente";
        when(creditoRepository.findByNumeroNfse(numeroNfse))
                .thenReturn(Collections.emptyList());

        assertThrows(CreditoNotFoundException.class,
                () -> creditoService.buscarPorNumeroNfse(numeroNfse));
    }

    @Test
    void buscarPorNumeroCredito_DeveRetornarCredito_QuandoEncontrado() {
        String numeroCredito = "123456";
        Credito credito = criarCreditoTeste();
        when(creditoRepository.findByNumeroCredito(numeroCredito))
                .thenReturn(Optional.of(credito));

        creditoService.buscarPorNumeroCredito(numeroCredito);

        verify(creditoRepository).findByNumeroCredito(numeroCredito);
        verify(eventPublisherService).publishConsultaEvent("CONSULTA_POR_CREDITO", numeroCredito);
    }

    private Credito criarCreditoTeste() {
        Credito credito = new Credito();
        credito.setId(1L);
        credito.setNumeroCredito("123456");
        credito.setNumeroNfse("7891011");
        credito.setDataConstituicao(LocalDate.of(2024, 2, 25));
        credito.setValorIssqn(new BigDecimal("1500.75"));
        credito.setTipoCredito("ISSQN");
        credito.setSimplesNacional(true);
        credito.setAliquota(new BigDecimal("5.0"));
        credito.setValorFaturado(new BigDecimal("30000.00"));
        credito.setValorDeducao(new BigDecimal("5000.00"));
        credito.setBaseCalculo(new BigDecimal("25000.00"));
        return credito;
    }
}