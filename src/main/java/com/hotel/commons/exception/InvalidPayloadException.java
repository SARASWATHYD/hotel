package com.hotel.commons.exception;
/**
 *
 * Created by saraswathy
on 2020-08-28 19:53 */

public class InvalidPayloadException extends  RuntimeException {

    public InvalidPayloadException(String message){
        super(message);
    }
}
