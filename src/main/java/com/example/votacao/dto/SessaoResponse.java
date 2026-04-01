package com.example.votacao.dto;

import java.time.LocalDateTime;

public record SessaoResponse(
        Long id,
        Long pautaId,
        LocalDateTime inicio,
        LocalDateTime fim,
        boolean aberta
) {
}
