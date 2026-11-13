package br.edu.controlplus.service;

import br.edu.controlplus.dto.*;
import br.edu.controlplus.model.Usuario;
import br.edu.controlplus.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.util.List;

// Service: concentra as regras do cadastro, sem receber requisicoes HTTP.
@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    // Injeção de dependencia por construtor.
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<UsuarioResponse> listar() {
        return repository.findAll().stream().map(this::converter).toList();
    }

    public UsuarioResponse buscar(Long id) {
        return converter(encontrar(id));
    }

    public UsuarioResponse criar(UsuarioRequest dados) {
        if (repository.existsByEmailIgnoreCase(dados.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail ja cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());
        usuario.setMetaDias(dados.metaDias());
        usuario.setDataInicio(LocalDate.now());
        return converter(repository.save(usuario));
    }

    public UsuarioResponse atualizar(Long id, UsuarioRequest dados) {
        Usuario usuario = encontrar(id);
        // Nao permite que a atualizacao use o e-mail de outro usuario.
        if (!usuario.getEmail().equalsIgnoreCase(dados.email())
                && repository.existsByEmailIgnoreCase(dados.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail ja cadastrado");
        }
        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());
        usuario.setMetaDias(dados.metaDias());
        return converter(repository.save(usuario));
    }

    public void excluir(Long id) {
        Usuario usuario = encontrar(id);
        repository.delete(usuario);
    }

    private Usuario encontrar(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));
    }

    private UsuarioResponse converter(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(),
            usuario.getEmail(), usuario.getMetaDias(), usuario.getDataInicio());
    }
}
