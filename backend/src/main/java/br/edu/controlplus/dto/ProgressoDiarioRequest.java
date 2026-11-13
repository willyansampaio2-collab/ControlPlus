package br.edu.controlplus.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

// O usuário vem da URL: o cliente não pode trocar o dono pelo corpo JSON.
public record ProgressoDiarioRequest(
    @NotNull(message = "Informe a data")
    @PastOrPresent(message = "A data não pode estar no futuro") LocalDate data,
    @NotNull(message = "Informe se a meta foi cumprida") Boolean metaCumprida,
    @Size(max = 1000, message = "As observações devem ter até 1000 caracteres") String observacoes
) { }
