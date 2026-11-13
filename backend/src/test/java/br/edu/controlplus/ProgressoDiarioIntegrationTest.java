package br.edu.controlplus;

import br.edu.controlplus.model.*;
import br.edu.controlplus.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

// Exercita HTTP, validação, Service e persistência juntos em banco descartável.
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProgressoDiarioIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UsuarioRepository usuarios;
    @Autowired ProgressoDiarioRepository progressos;
    Usuario usuario;
    LocalDate hoje;

    @BeforeEach
    void preparar() {
        progressos.deleteAll();
        usuarios.deleteAll();
        hoje = LocalDate.now();
        usuario = novoUsuario("aluno@example.com");
    }

    Usuario novoUsuario(String email) {
        Usuario novo = new Usuario();
        novo.setNome("Aluno");
        novo.setEmail(email);
        novo.setMetaDias(30);
        novo.setDataInicio(hoje.minusDays(10));
        return usuarios.saveAndFlush(novo);
    }

    String url() { return "/usuarios/" + usuario.getId() + "/progressos"; }
    String corpo(LocalDate data, boolean cumprida) throws Exception {
        return json.writeValueAsString(Map.of("data", data.toString(), "metaCumprida", cumprida,
            "observacoes", "Estudei Java"));
    }
    long criar(LocalDate data) throws Exception {
        String resposta = mvc.perform(post(url()).contentType(APPLICATION_JSON).content(corpo(data, true)))
            .andExpect(status().isCreated()).andExpect(header().exists("Location"))
            .andReturn().getResponse().getContentAsString();
        return json.readTree(resposta).get("id").asLong();
    }

    @Test void resumoSemHistorico() throws Exception {
        mvc.perform(get(url() + "/resumo")).andExpect(status().isOk())
            .andExpect(jsonPath("$.totalRegistros").value(0))
            .andExpect(jsonPath("$.diasCumpridos").value(0))
            .andExpect(jsonPath("$.sequenciaAtual").value(0))
            .andExpect(jsonPath("$.melhorSequencia").value(0))
            .andExpect(jsonPath("$.percentualMeta").value(0))
            .andExpect(jsonPath("$.metaAtingida").value(false));
    }

    @Test void resumoContaSequenciasLacunasEArredondaPercentual() throws Exception {
        for (int dias : new int[]{7, 6, 5, 2, 1, 0}) criar(hoje.minusDays(dias));
        mvc.perform(get(url() + "/resumo")).andExpect(status().isOk())
            .andExpect(jsonPath("$.totalRegistros").value(6))
            .andExpect(jsonPath("$.diasCumpridos").value(6))
            .andExpect(jsonPath("$.sequenciaAtual").value(3))
            .andExpect(jsonPath("$.melhorSequencia").value(3))
            .andExpect(jsonPath("$.percentualMeta").value(20.0));
        usuario.setMetaDias(7);
        usuarios.saveAndFlush(usuario);
        mvc.perform(get(url() + "/resumo")).andExpect(status().isOk())
            .andExpect(jsonPath("$.percentualMeta").value(85.71));
    }

    @Test void resumoMantemSequenciaDeOntemSemRegistroHoje() throws Exception {
        criar(hoje.minusDays(2));
        criar(hoje.minusDays(1));
        mvc.perform(get(url() + "/resumo")).andExpect(status().isOk())
            .andExpect(jsonPath("$.sequenciaAtual").value(2));
        long id = criar(hoje);
        mvc.perform(put(url() + "/" + id).contentType(APPLICATION_JSON).content(corpo(hoje, false)))
            .andExpect(status().isOk());
        mvc.perform(get(url() + "/resumo")).andExpect(status().isOk())
            .andExpect(jsonPath("$.sequenciaAtual").value(0))
            .andExpect(jsonPath("$.melhorSequencia").value(2))
            .andExpect(jsonPath("$.diasCumpridos").value(2))
            .andExpect(jsonPath("$.totalRegistros").value(3));
    }

    @Test void resumoZeraSequenciaAntigaELimitaMetaACemPorCento() throws Exception {
        criar(hoje.minusDays(5));
        criar(hoje.minusDays(4));
        usuario.setMetaDias(1);
        usuarios.saveAndFlush(usuario);
        mvc.perform(get(url() + "/resumo")).andExpect(status().isOk())
            .andExpect(jsonPath("$.sequenciaAtual").value(0))
            .andExpect(jsonPath("$.melhorSequencia").value(2))
            .andExpect(jsonPath("$.metaAtingida").value(true))
            .andExpect(jsonPath("$.percentualMeta").value(100.0));
    }

    @Test void resumoRespeitaUsuarioERecalculaAposExclusao() throws Exception {
        long id = criar(hoje);
        Usuario outro = novoUsuario("resumo@example.com");
        mvc.perform(get("/usuarios/" + outro.getId() + "/progressos/resumo"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.diasCumpridos").value(0));
        mvc.perform(delete(url() + "/" + id)).andExpect(status().isNoContent());
        mvc.perform(get(url() + "/resumo")).andExpect(status().isOk())
            .andExpect(jsonPath("$.diasCumpridos").value(0));
        mvc.perform(get("/usuarios/9223372036854775807/progressos/resumo"))
            .andExpect(status().isNotFound());
    }

    @Test void fluxoCompleto() throws Exception {
        long id = criar(hoje);
        mvc.perform(get(url() + "/" + id)).andExpect(status().isOk())
            .andExpect(jsonPath("$.usuarioId").value(usuario.getId()))
            .andExpect(jsonPath("$.metaCumprida").value(true));
        mvc.perform(put(url() + "/" + id).contentType(APPLICATION_JSON).content(corpo(hoje, false)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.metaCumprida").value(false));
        mvc.perform(delete(url() + "/" + id)).andExpect(status().isNoContent());
        mvc.perform(get(url() + "/" + id)).andExpect(status().isNotFound());
        assertThat(progressos.count()).isZero();
    }

    @Test void listaEmOrdemDecrescente() throws Exception {
        criar(hoje.minusDays(1));
        criar(hoje);
        mvc.perform(get(url())).andExpect(status().isOk())
            .andExpect(jsonPath("$[0].data").value(hoje.toString()))
            .andExpect(jsonPath("$[1].data").value(hoje.minusDays(1).toString()));
    }

    @Test void rejeitaDuplicidadeNaCriacaoEAtualizacao() throws Exception {
        criar(hoje);
        mvc.perform(post(url()).contentType(APPLICATION_JSON).content(corpo(hoje, true)))
            .andExpect(status().isConflict());
        long outro = criar(hoje.minusDays(1));
        mvc.perform(put(url() + "/" + outro).contentType(APPLICATION_JSON).content(corpo(hoje, false)))
            .andExpect(status().isConflict());
        assertThat(progressos.count()).isEqualTo(2);
    }

    @Test void rejeitaDatasFuturasEAnterioresAoInicio() throws Exception {
        for (LocalDate data : new LocalDate[]{hoje.plusDays(1), hoje.minusDays(11)}) {
            mvc.perform(post(url()).contentType(APPLICATION_JSON).content(corpo(data, true)))
                .andExpect(status().isBadRequest());
        }
        assertThat(progressos.count()).isZero();
    }

    @Test void exigeDataEMetaCumprida() throws Exception {
        mvc.perform(post(url()).contentType(APPLICATION_JSON).content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.campos.data").exists())
            .andExpect(jsonPath("$.campos.metaCumprida").exists());
    }

    @Test void limitaObservacoes() throws Exception {
        String corpo = json.writeValueAsString(Map.of("data", hoje.toString(), "metaCumprida", true,
            "observacoes", "a".repeat(1001)));
        mvc.perform(post(url()).contentType(APPLICATION_JSON).content(corpo))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.campos.observacoes").exists());
    }

    @Test void rejeitaJsonDataEIdInvalidos() throws Exception {
        for (String corpo : new String[]{"{", "{\"data\":\"invalida\",\"metaCumprida\":true}"}) {
            mvc.perform(post(url()).contentType(APPLICATION_JSON).content(corpo))
                .andExpect(status().isBadRequest());
        }
        mvc.perform(get(url() + "/abc")).andExpect(status().isBadRequest());
    }

    @Test void usuarioInexistenteRetorna404() throws Exception {
        String rota = "/usuarios/9223372036854775807/progressos";
        mvc.perform(get(rota)).andExpect(status().isNotFound());
        mvc.perform(post(rota).contentType(APPLICATION_JSON).content(corpo(hoje, true)))
            .andExpect(status().isNotFound());
    }

    @Test void isolaRegistrosDeOutroUsuario() throws Exception {
        long id = criar(hoje);
        Usuario outro = novoUsuario("outro@example.com");
        String rota = "/usuarios/" + outro.getId() + "/progressos";
        mvc.perform(get(rota)).andExpect(status().isOk()).andExpect(content().json("[]"));
        mvc.perform(get(rota + "/" + id)).andExpect(status().isNotFound());
        mvc.perform(put(rota + "/" + id).contentType(APPLICATION_JSON).content(corpo(hoje, false)))
            .andExpect(status().isNotFound());
        mvc.perform(delete(rota + "/" + id)).andExpect(status().isNotFound());
        assertThat(progressos.count()).isEqualTo(1);
    }

    @Test void bancoTambemImpedeDuplicidade() throws Exception {
        criar(hoje);
        ProgressoDiario repetido = new ProgressoDiario();
        repetido.setUsuario(usuario);
        repetido.setData(hoje);
        repetido.setMetaCumprida(true);
        assertThatThrownBy(() -> progressos.saveAndFlush(repetido))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test void permiteMesmoDiaParaUsuariosDiferentes() throws Exception {
        criar(hoje);
        usuario = novoUsuario("segundo@example.com");
        criar(hoje);
        assertThat(progressos.count()).isEqualTo(2);
    }

    @Test void impedeExcluirUsuarioComHistorico() throws Exception {
        criar(hoje);
        mvc.perform(delete("/usuarios/" + usuario.getId())).andExpect(status().isConflict());
        assertThat(usuarios.existsById(usuario.getId())).isTrue();
    }
}
