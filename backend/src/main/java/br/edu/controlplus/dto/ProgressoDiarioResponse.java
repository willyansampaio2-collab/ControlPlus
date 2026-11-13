package br.edu.controlplus.dto;

import java.time.LocalDate;

// Retornamos o identificador do usuário, evitando serializar a entidade JPA.
public record ProgressoDiarioResponse(Long id, Long usuarioId, LocalDate data,
                                     Boolean metaCumprida, String observacoes) { }
