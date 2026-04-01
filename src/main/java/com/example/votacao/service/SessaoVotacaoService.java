package com.example.votacao.service;

import com.example.votacao.dto.AbrirSessaoRequest;
import com.example.votacao.dto.SessaoResponse;
import com.example.votacao.entity.Pauta;
import com.example.votacao.entity.SessaoVotacao;
import com.example.votacao.exception.RegraNegocioException;
import com.example.votacao.exception.RecursoNaoEncontradoException;
import com.example.votacao.repository.SessaoVotacaoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessaoVotacaoService {

    private static final long DURACAO_PADRAO_MINUTOS = 1L;

    private final SessaoVotacaoRepository sessaoRepository;
    private final PautaService pautaService;

    @Transactional
    public SessaoResponse abrirSessao(Long pautaId, AbrirSessaoRequest request) {
        if (sessaoRepository.existsByPautaId(pautaId)) {
            throw new RegraNegocioException("Já existe uma sessão cadastrada para esta pauta");
        }

        Pauta pauta = pautaService.buscarEntidade(pautaId);
        long duracao = request != null && request.duracaoMinutos() != null
                ? request.duracaoMinutos()
                : DURACAO_PADRAO_MINUTOS;

        LocalDateTime inicio = LocalDateTime.now();
        SessaoVotacao sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .inicio(inicio)
                .fim(inicio.plusMinutes(duracao))
                .build();

        SessaoVotacao salva = sessaoRepository.save(sessao);
        return toResponse(salva);
    }

    @Transactional(readOnly = true)
    public SessaoVotacao buscarEntidadePorPauta(Long pautaId) {
        pautaService.buscarEntidade(pautaId);
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada para a pauta " + pautaId));
    }

    @Transactional(readOnly = true)
    public void validarSessaoAberta(Long pautaId) {
        SessaoVotacao sessao = buscarEntidadePorPauta(pautaId);
        if (!sessao.estaAberta(LocalDateTime.now())) {
            throw new RegraNegocioException("A sessão de votação está encerrada");
        }
    }

    @Transactional(readOnly = true)
    public SessaoResponse buscarPorPauta(Long pautaId) {
        return toResponse(buscarEntidadePorPauta(pautaId));
    }

    private SessaoResponse toResponse(SessaoVotacao sessao) {
        return new SessaoResponse(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getInicio(),
                sessao.getFim(),
                sessao.estaAberta(LocalDateTime.now())
        );
    }
}
