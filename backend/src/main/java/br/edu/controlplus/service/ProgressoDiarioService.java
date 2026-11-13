package br.edu.controlplus.service;

import br.edu.controlplus.dto.*;
import br.edu.controlplus.model.*;
import br.edu.controlplus.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProgressoDiarioService {
    private final ProgressoDiarioRepository repository;
    private final UsuarioRepository usuarios;

    public ProgressoDiarioService(ProgressoDiarioRepository repository, UsuarioRepository usuarios) {
        this.repository = repository;
        this.usuarios = usuarios;
    }

    public List<ProgressoDiarioResponse> listar(Long usuarioId) {
        usuario(usuarioId);
        return repository.findByUsuarioIdOrderByDataDesc(usuarioId).stream().map(this::converter).toList();
    }

    public ProgressoDiarioResponse buscar(Long usuarioId, Long id) {
        usuario(usuarioId);
        return converter(encontrar(usuarioId, id));
    }

    @Transactional
    public ProgressoDiarioResponse criar(Long usuarioId, ProgressoDiarioRequest dados) {
        Usuario usuario = usuario(usuarioId);
        validarData(usuario, dados);
        if (repository.existsByUsuarioIdAndData(usuarioId, dados.data())) {
            throw duplicado();
        }
        ProgressoDiario progresso = new ProgressoDiario();
        progresso.setUsuario(usuario);
        preencher(progresso, dados);
        // Flush executa o SQL agora, incluindo a restrição de unicidade do PostgreSQL.
        return converter(repository.saveAndFlush(progresso));
    }

    @Transactional
    public ProgressoDiarioResponse atualizar(Long usuarioId, Long id, ProgressoDiarioRequest dados) {
        Usuario usuario = usuario(usuarioId);
        ProgressoDiario progresso = encontrar(usuarioId, id);
        validarData(usuario, dados);
        if (repository.existsByUsuarioIdAndDataAndIdNot(usuarioId, dados.data(), id)) {
            throw duplicado();
        }
        preencher(progresso, dados);
        return converter(repository.saveAndFlush(progresso));
    }

    @Transactional
    public void excluir(Long usuarioId, Long id) {
        usuario(usuarioId);
        repository.delete(encontrar(usuarioId, id));
        repository.flush();
    }

    private Usuario usuario(Long id) {
        return usuarios.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private ProgressoDiario encontrar(Long usuarioId, Long id) {
        // Consultar pelos dois IDs impede acessar um registro de outro usuário.
        return repository.findByIdAndUsuarioId(id, usuarioId).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Progresso não encontrado para este usuário"));
    }

    private void validarData(Usuario usuario, ProgressoDiarioRequest dados) {
        if (dados.data().isBefore(usuario.getDataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data não pode ser anterior ao início do usuário");
        }
    }

    private ResponseStatusException duplicado() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Já existe progresso para este usuário nesta data");
    }

    private void preencher(ProgressoDiario progresso, ProgressoDiarioRequest dados) {
        progresso.setData(dados.data());
        progresso.setMetaCumprida(dados.metaCumprida());
        progresso.setObservacoes(dados.observacoes());
    }

    private ProgressoDiarioResponse converter(ProgressoDiario progresso) {
        return new ProgressoDiarioResponse(progresso.getId(), progresso.getUsuario().getId(),
            progresso.getData(), progresso.getMetaCumprida(), progresso.getObservacoes());
    }
}
