package com.example.votacao.controller;

import com.example.votacao.dto.CriarPautaRequest;
import com.example.votacao.dto.PautaResponse;
import com.example.votacao.dto.ResultadoVotacaoResponse;
import com.example.votacao.service.PautaService;
import com.example.votacao.service.VotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;
    private final VotoService votoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PautaResponse criar(@Valid @RequestBody CriarPautaRequest request) {
        return pautaService.criar(request);
    }

    @GetMapping("/{pautaId}")
    public PautaResponse buscar(@PathVariable Long pautaId) {
        return pautaService.buscarPorId(pautaId);
    }

    @GetMapping("/{pautaId}/resultado")
    public ResultadoVotacaoResponse resultado(@PathVariable Long pautaId) {
        return votoService.obterResultado(pautaId);
    }
}
