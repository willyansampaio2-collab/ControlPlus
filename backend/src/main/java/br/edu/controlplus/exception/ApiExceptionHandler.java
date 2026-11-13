package br.edu.controlplus.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import java.util.LinkedHashMap;
import java.util.Map;

// Centraliza erros para que o cliente receba JSON sem detalhes internos do banco.
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> regra(ResponseStatusException erro) {
        return resposta(erro.getStatusCode(), erro.getReason());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validacao(MethodArgumentNotValidException erro) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Confira os campos enviados");
        Map<String, String> campos = new LinkedHashMap<>();
        erro.getBindingResult().getFieldErrors().forEach(campo ->
            campos.putIfAbsent(campo.getField(), campo.getDefaultMessage()));
        problema.setProperty("campos", campos);
        return ResponseEntity.badRequest().body(problema);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ProblemDetail> formato(Exception erro) {
        return resposta(HttpStatus.BAD_REQUEST, "JSON, data ou identificador inválido. Use data no formato AAAA-MM-DD");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> integridade(DataIntegrityViolationException erro) {
        return resposta(HttpStatus.CONFLICT, "Operação em conflito: registro duplicado ou vínculo com outro registro");
    }

    private ResponseEntity<ProblemDetail> resposta(HttpStatusCode status, String detalhe) {
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detalhe));
    }
}
