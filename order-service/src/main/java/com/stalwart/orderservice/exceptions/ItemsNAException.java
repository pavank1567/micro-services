package com.stalwart.orderservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemsNAException extends RuntimeException {

    public ItemsNAException(String message){
        super(message);
    }
}
