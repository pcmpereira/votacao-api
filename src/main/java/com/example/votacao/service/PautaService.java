package com.example.votacao.service;

import com.example.votacao.dto.CriarPautaRequest;
import com.example.votacao.dto.PautaResponse;
import com.example.votacao.entity.Pauta;
import com.example.votacao.exception.RecursoNaoEncontradoException;
import com.example.votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;

    @Transactional
    public PautaResponse criar(CriarPautaRequest request) {
        Pauta pauta = Pauta.builder()
                .titulo(request.titulo())
                .descricao(request.descricao())
                .build();

        Pauta salva = pautaRepository.save(pauta);
        return toResponse(salva);
    }

    @Transactional(readOnly = true)
    public Pauta buscarEntidade(Long pautaId) {
        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada para o id " + pautaId));
    }

    @Transactional(readOnly = true)
    public PautaResponse buscarPorId(Long pautaId) {
        return toResponse(buscarEntidade(pautaId));
    }

    private PautaResponse toResponse(Pauta pauta) {
        return new PautaResponse(pauta.getId(), pauta.getTitulo(), pauta.getDescricao());
    }
}
