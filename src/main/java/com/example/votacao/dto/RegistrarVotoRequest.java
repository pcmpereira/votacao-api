package com.example.votacao.dto;

import com.example.votacao.entity.TipoVoto;
import jakarta.validation.constraints.NotNull;

public record RegistrarVotoRequest(
        @NotNull(message = "O associadoId é obrigatório")
        Long associadoId,

        @NotNull(message = "O voto é obrigatório")
        TipoVoto voto
) {
}
