package br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção a ser lançada quando uma regra de negócio específica é violada.
 * Mapeada para o status HTTP 400 Bad Request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessRuleException extends RuntimeException{

    /**
     * Construtor que aceita uma mensagem de erro detalhada.
     * @param message A mensagem descrevendo a violação da regra de negócio.
     */
    public BusinessRuleException(String message) {
        super(message);
    }
    
}
