package br.edu.ufape.projeto_bd.projeto_bd.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
    private Class<? extends Enum<?>> enumClass;
    private List<String> acceptedValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.acceptedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; 
        }
        return Arrays.asList(enumClass.getEnumConstants()).contains(value);
    }

    public String getAcceptedValues() {
        return acceptedValues.toString();
    }
}