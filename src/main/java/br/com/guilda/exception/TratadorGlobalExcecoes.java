package br.com.guilda.exception;

import br.com.guilda.dto.RespostaErroApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorGlobalExcecoes {

    @ExceptionHandler(ExcecaoRecursoNaoEncontrado.class)
    public ResponseEntity<RespostaErroApi> handleNotFound(ExcecaoRecursoNaoEncontrado ex) {
        RespostaErroApi respostaErro = new RespostaErroApi(ex.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respostaErro);
    }

    @ExceptionHandler(ExcecaoRequisicaoInvalida.class)
    public ResponseEntity<RespostaErroApi> handleInvalid(ExcecaoRequisicaoInvalida ex) {
        RespostaErroApi respostaErro = new RespostaErroApi(ex.getMessage(), ex.getDetails());
        return ResponseEntity.badRequest().body(respostaErro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaErroApi> handleGeneric(Exception ex) {
        RespostaErroApi respostaErro = new RespostaErroApi("Erro interno do servidor", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respostaErro);
    }
}

