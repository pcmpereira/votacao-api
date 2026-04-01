package com.example.votacao.service;

import com.example.votacao.dto.RegistrarVotoRequest;
import com.example.votacao.dto.ResultadoVotacaoResponse;
import com.example.votacao.dto.VotoResponse;
import com.example.votacao.entity.Pauta;
import com.example.votacao.entity.TipoVoto;
import com.example.votacao.entity.Voto;
import com.example.votacao.exception.RegraNegocioException;
import com.example.votacao.repository.VotoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final PautaService pautaService;
    private final SessaoVotacaoService sessaoVotacaoService;

    @Transactional
    public VotoResponse registrar(Long pautaId, RegistrarVotoRequest request) {
        sessaoVotacaoService.validarSessaoAberta(pautaId);

        if (votoRepository.existsByPautaIdAndAssociadoId(pautaId, request.associadoId())) {
            throw new RegraNegocioException("O associado já votou nesta pauta");
        }

        Pauta pauta = pautaService.buscarEntidade(pautaId);
        Voto voto = Voto.builder()
                .pauta(pauta)
                .associadoId(request.associadoId())
                .voto(request.voto())
                .dataHora(LocalDateTime.now())
                .build();

        Voto salvo = votoRepository.save(voto);
        return new VotoResponse(salvo.getId(), pautaId, salvo.getAssociadoId(), salvo.getVoto(), salvo.getDataHora());
    }

    @Transactional(readOnly = true)
    public ResultadoVotacaoResponse obterResultado(Long pautaId) {
        Pauta pauta = pautaService.buscarEntidade(pautaId);

        long votosSim = votoRepository.countByPautaIdAndVoto(pautaId, TipoVoto.SIM);
        long votosNao = votoRepository.countByPautaIdAndVoto(pautaId, TipoVoto.NAO);
        long total = votosSim + votosNao;

        String resultado = votosSim > votosNao
                ? "SIM venceu"
                : votosNao > votosSim
                ? "NÃO venceu"
                : "Empate";

        return new ResultadoVotacaoResponse(
                pauta.getId(),
                pauta.getTitulo(),
                votosSim,
                votosNao,
                total,
                resultado
        );
    }
}
