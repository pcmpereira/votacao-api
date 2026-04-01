package com.example.votacao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.votacao.dto.RegistrarVotoRequest;
import com.example.votacao.dto.ResultadoVotacaoResponse;
import com.example.votacao.entity.Pauta;
import com.example.votacao.entity.TipoVoto;
import com.example.votacao.entity.Voto;
import com.example.votacao.exception.RegraNegocioException;
import com.example.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaService pautaService;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @InjectMocks
    private VotoService votoService;

    private Pauta pauta;

    @BeforeEach
    void setUp() {
        pauta = Pauta.builder().id(1L).titulo("Pauta de teste").descricao("Descrição").build();
    }

    @Test
    void deveImpedirVotoDuplicado() {
        RegistrarVotoRequest request = new RegistrarVotoRequest(10L, TipoVoto.SIM);

        when(votoRepository.existsByPautaIdAndAssociadoId(1L, 10L)).thenReturn(true);

        RegraNegocioException exception = assertThrows(RegraNegocioException.class,
                () -> votoService.registrar(1L, request));

        assertEquals("O associado já votou nesta pauta", exception.getMessage());
    }

    @Test
    void deveRegistrarVotoComSucesso() {
        RegistrarVotoRequest request = new RegistrarVotoRequest(10L, TipoVoto.SIM);

        when(votoRepository.existsByPautaIdAndAssociadoId(1L, 10L)).thenReturn(false);
        when(pautaService.buscarEntidade(1L)).thenReturn(pauta);
        when(votoRepository.save(any(Voto.class))).thenAnswer(invocation -> {
            Voto voto = invocation.getArgument(0);
            voto.setId(99L);
            return voto;
        });

        var response = votoService.registrar(1L, request);

        assertEquals(99L, response.id());
        assertEquals(10L, response.associadoId());
        assertEquals(TipoVoto.SIM, response.voto());
    }

    @Test
    void deveRetornarResultadoDaVotacao() {
        when(pautaService.buscarEntidade(1L)).thenReturn(pauta);
        when(votoRepository.countByPautaIdAndVoto(1L, TipoVoto.SIM)).thenReturn(7L);
        when(votoRepository.countByPautaIdAndVoto(1L, TipoVoto.NAO)).thenReturn(4L);

        ResultadoVotacaoResponse response = votoService.obterResultado(1L);

        assertEquals(7L, response.votosSim());
        assertEquals(4L, response.votosNao());
        assertEquals(11L, response.totalVotos());
        assertEquals("SIM venceu", response.resultado());
    }
}
