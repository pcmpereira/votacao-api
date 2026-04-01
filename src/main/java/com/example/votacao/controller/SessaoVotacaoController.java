package com.example.votacao.controller;

import com.example.votacao.dto.AbrirSessaoRequest;
import com.example.votacao.dto.SessaoResponse;
import com.example.votacao.service.SessaoVotacaoService;
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
@RequestMapping("/api/pautas/{pautaId}/sessao")
@RequiredArgsConstructor
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessaoResponse abrir(@PathVariable Long pautaId,
                                @Valid @RequestBody(required = false) AbrirSessaoRequest request) {
        return sessaoVotacaoService.abrirSessao(pautaId, request);
    }

    @GetMapping
    public SessaoResponse buscar(@PathVariable Long pautaId) {
        return sessaoVotacaoService.buscarPorPauta(pautaId);
    }
}
