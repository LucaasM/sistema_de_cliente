package io.github.lucaasm.clientes.controller.exception;

import lombok.Getter;

public class NotFindByIdExcepetion extends Exception{

    @Getter
    String message;

    public NotFindByIdExcepetion(String message) {
        this.message = message;
    }
}
