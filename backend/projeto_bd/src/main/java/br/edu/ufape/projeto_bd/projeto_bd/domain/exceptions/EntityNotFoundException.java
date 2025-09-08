package br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // HTTP 404
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructor to create an exception for a not-found entity.
     * 
     * @param entityClass The class of the entity that was not found.
     * @param entityId    The identifier of the entity that was not found.
     */
    public EntityNotFoundException(Class<?> entityClass, Object entityId) {
        super(String.format("%s com ID %s não foi encontrado.", entityClass.getSimpleName(), entityId.toString()));
    }

    /**
     * Constructor to create an exception for a not-found entity.
     * 
     * @param entityClass   The class of the entity that was not found.
     * @param entityId      The identifier of the entity that was not found.
     * @param resourceName  The name of the resource.
     * @param resourceValue The value of the resource that was not found.
     */
    public EntityNotFoundException(Class<?> entityClass, String resourceName, String resourceValue) {
        super(String.format("%s com %s '%s' não foi encontrado.", entityClass.getSimpleName(), resourceName,
                resourceValue));
    }

    /**
     * Constructor with a custom message.
     * 
     * @param message The detail message.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
