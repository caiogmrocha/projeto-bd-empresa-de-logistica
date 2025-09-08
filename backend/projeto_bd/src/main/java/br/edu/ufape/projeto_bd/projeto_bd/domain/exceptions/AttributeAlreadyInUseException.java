package br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // HTTP 409
public class AttributeAlreadyInUseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor to create an exception when a unique attribute is already in use.
     * 
     * @param attributeName The name of the attribute (e.g., "Email", "CPF").
     * @param entityClass   The class of the entity where the violation occurred.
     */
    public AttributeAlreadyInUseException(String attributeName, Class<?> entityClass) {
        super(String.format("%s is already in use for entity %s.", attributeName, entityClass.getSimpleName()));
    }

    /**
     * Overloaded constructor to provide a more detailed message, including the
     * value.
     * 
     * @param attributeName  The name of the attribute.
     * @param attributeValue The value of the attribute that is already in use.
     * @param entityClass    The class of the entity.
     */
    public AttributeAlreadyInUseException(String attributeName, String attributeValue, Class<?> entityClass) {
        super(String.format("%s '%s' is already in use for entity %s.",
                attributeName, attributeValue, entityClass.getSimpleName()));
    }
}
