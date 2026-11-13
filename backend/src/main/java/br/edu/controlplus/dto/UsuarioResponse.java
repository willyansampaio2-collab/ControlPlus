package br.edu.controlplus.dto;

import java.time.LocalDate;

// DTO de saida: devolve somente os campos desejados na resposta JSON.
public record UsuarioResponse(Long id, String nome, String email,
                              Integer metaDias, LocalDate dataInicio) { }
