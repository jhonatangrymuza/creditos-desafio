package com.credit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.credit.dto.CreditoDTO;
import com.credit.entity.Credito;
import com.credit.exception.CreditoNotFoundException;
import com.credit.mapper.CreditoMapper;
import com.credit.repository.CreditoRepository;

@Service
public class CreditoService {

    @Autowired
    private CreditoRepository creditoRepository;

    @Autowired
    private CreditoMapper creditoMapper;

    @Autowired
    private EventPublisherService eventPublisherService;

    public List<CreditoDTO> buscarPorNumeroNfse(String numeroNfse) {
        List<Credito> creditos = creditoRepository.findByNumeroNfse(numeroNfse);

        if (creditos.isEmpty()) {
            throw new CreditoNotFoundException("Nenhum crédito encontrado para a NFS-e: " + numeroNfse);
        }

        // Publicar evento de consulta
        eventPublisherService.publishConsultaEvent("CONSULTA_POR_NFSE", numeroNfse);

        return creditoMapper.toDTOList(creditos);
    }

    public CreditoDTO buscarPorNumeroCredito(String numeroCredito) {
        Credito credito = creditoRepository.findByNumeroCredito(numeroCredito)
                .orElseThrow(() -> new CreditoNotFoundException("Crédito não encontrado: " + numeroCredito));

        // Publicar evento de consulta
        eventPublisherService.publishConsultaEvent("CONSULTA_POR_CREDITO", numeroCredito);

        return creditoMapper.toDTO(credito);
    }
}