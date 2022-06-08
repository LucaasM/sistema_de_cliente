package io.github.lucaasm.clientes.controller;

import io.github.lucaasm.clientes.controller.exception.ApiErrors;
import io.github.lucaasm.clientes.controller.exception.ClienteCpfExisteException;
import io.github.lucaasm.clientes.controller.exception.NotFindByIdExcepetion;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationErrors(MethodArgumentNotValidException ex) {
        // BindingResult é o resultado da validação
        BindingResult bindingResult = ex.getBindingResult();

        List<String> messages = bindingResult.getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return new ApiErrors(messages);
    }

    @ExceptionHandler(NotFindByIdExcepetion.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleNotFindByIdExcepetion(NotFindByIdExcepetion ex) {
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(ClienteCpfExisteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleClienteCpfExisteException(ClienteCpfExisteException ex) {
        return new ApiErrors(ex.getMessage());
    }
}
