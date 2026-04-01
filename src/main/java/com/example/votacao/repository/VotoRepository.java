package com.example.votacao.repository;

import com.example.votacao.entity.TipoVoto;
import com.example.votacao.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByPautaIdAndAssociadoId(Long pautaId, Long associadoId);
    long countByPautaIdAndVoto(Long pautaId, TipoVoto voto);
}
