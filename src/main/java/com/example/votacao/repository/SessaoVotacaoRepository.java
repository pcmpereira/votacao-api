package com.example.votacao.repository;

import com.example.votacao.entity.SessaoVotacao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {
    Optional<SessaoVotacao> findByPautaId(Long pautaId);
    boolean existsByPautaId(Long pautaId);
}
