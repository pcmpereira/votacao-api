package com.example.votacao.dto;

public record ResultadoVotacaoResponse(
        Long pautaId,
        String tituloPauta,
        long votosSim,
        long votosNao,
        long totalVotos,
        String resultado
) {
}
