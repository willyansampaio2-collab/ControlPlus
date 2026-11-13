package br.edu.controlplus.dto;

import jakarta.validation.constraints.*;

// DTO de entrada: define quais dados o cliente pode enviar.
public record UsuarioRequest(
    @NotBlank(message = "Informe o nome") String nome,
    @NotBlank @Email(message = "Informe um e-mail valido") String email,
    @NotNull @Positive(message = "A meta deve ser positiva") Integer metaDias
) { }
