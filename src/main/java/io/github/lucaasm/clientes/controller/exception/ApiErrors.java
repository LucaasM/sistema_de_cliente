package io.github.lucaasm.clientes.controller.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    @Getter
    private final List<String> errors;

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiErrors(String message) {
        this.errors = Arrays.asList(message);
    }
}
