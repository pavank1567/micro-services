package com.stalwart.orderservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServerNRException extends RuntimeException{

    public ServerNRException(String message){
        super(message);
    }
}
