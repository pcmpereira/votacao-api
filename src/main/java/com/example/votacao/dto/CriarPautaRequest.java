package com.example.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarPautaRequest(
        @NotBlank(message = "O título da pauta é obrigatório")
        @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
        String titulo,

        @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
        String descricao
) {
}
