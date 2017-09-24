package com.ajibigad.erazer.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ajibigad on 13/08/2017.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ResourceNotFound extends ErazerException {
    public ResourceNotFound(String message) {
        super(message);
    }
}
