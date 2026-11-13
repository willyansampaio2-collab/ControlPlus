package br.edu.controlplus.controller;

import br.edu.controlplus.dto.*;
import br.edu.controlplus.service.ProgressoDiarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

// Controller cuida do HTTP; as regras e transações ficam no Service.
@RestController
@RequestMapping("/usuarios/{usuarioId}/progressos")
public class ProgressoDiarioController {
    private final ProgressoDiarioService service;
    public ProgressoDiarioController(ProgressoDiarioService service) { this.service = service; }

    @GetMapping
    public List<ProgressoDiarioResponse> listar(@PathVariable Long usuarioId) {
        return service.listar(usuarioId);
    }

    @GetMapping("/{id}")
    public ProgressoDiarioResponse buscar(@PathVariable Long usuarioId, @PathVariable Long id) {
        return service.buscar(usuarioId, id);
    }

    @PostMapping
    public ResponseEntity<ProgressoDiarioResponse> criar(@PathVariable Long usuarioId,
            @Valid @RequestBody ProgressoDiarioRequest dados) {
        ProgressoDiarioResponse resposta = service.criar(usuarioId, dados);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(local).body(resposta);
    }

    @PutMapping("/{id}")
    public ProgressoDiarioResponse atualizar(@PathVariable Long usuarioId, @PathVariable Long id,
            @Valid @RequestBody ProgressoDiarioRequest dados) {
        return service.atualizar(usuarioId, id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long usuarioId, @PathVariable Long id) {
        service.excluir(usuarioId, id);
        return ResponseEntity.noContent().build();
    }
}
