package com.hotel.commons.utils;
/**
 *  Created by saraswathy
on 2020-08-28 19:51 */

public class PreConditions {


    private PreConditions(){

    }

    public  static <T> boolean checkNull(T t){
        return t == null;
    }

    public static <T> boolean checkNullAndThrowInvalidPayload(T t, String msg) {
        if (t == null)
            throw new IllegalArgumentException(msg);
        return true;
    }

    public static <T> boolean checkNullAndThrow(T t, String msg) {
        if (t == null)
            throw new IllegalArgumentException(msg);
        return false;
    }
}
