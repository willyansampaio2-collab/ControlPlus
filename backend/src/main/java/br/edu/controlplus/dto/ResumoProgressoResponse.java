package br.edu.controlplus.dto;

import java.math.BigDecimal;

// Resumo calculado a partir do histórico; não cria outra tabela no banco.
public record ResumoProgressoResponse(Long usuarioId, int totalRegistros, long diasCumpridos,
    int sequenciaAtual, int melhorSequencia, int metaDias, BigDecimal percentualMeta,
    boolean metaAtingida) { }
