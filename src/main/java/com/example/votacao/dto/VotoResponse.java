package com.example.votacao.dto;

import com.example.votacao.entity.TipoVoto;
import java.time.LocalDateTime;

public record VotoResponse(
        Long id,
        Long pautaId,
        Long associadoId,
        TipoVoto voto,
        LocalDateTime dataHora
) {
}
