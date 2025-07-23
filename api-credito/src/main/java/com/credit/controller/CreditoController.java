package com.credit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.credit.dto.CreditoDTO;
import com.credit.service.CreditoService;

@RestController
@RequestMapping("/api/creditos")
@CrossOrigin(origins = "*")
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    @GetMapping("/{numeroNfse}")
    public ResponseEntity<List<CreditoDTO>> buscarPorNumeroNfse(@PathVariable String numeroNfse) {
        List<CreditoDTO> creditos = creditoService.buscarPorNumeroNfse(numeroNfse);
        return ResponseEntity.ok(creditos);
    }

    @GetMapping("/credito/{numeroCredito}")
    public ResponseEntity<CreditoDTO> buscarPorNumeroCredito(@PathVariable String numeroCredito) {
        CreditoDTO credito = creditoService.buscarPorNumeroCredito(numeroCredito);
        return ResponseEntity.ok(credito);
    }
}