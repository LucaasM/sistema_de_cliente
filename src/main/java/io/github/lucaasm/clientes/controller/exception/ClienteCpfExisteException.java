package io.github.lucaasm.clientes.controller.exception;

import lombok.Getter;

public class ClienteCpfExisteException extends Exception{

    @Getter
    String message;

    public ClienteCpfExisteException(String message) {
        this.message = message;
    }
}
