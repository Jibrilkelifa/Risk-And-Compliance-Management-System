package com.cbo.CBO_NFOS_ICMS.exception;

public class UserNotFoundException extends RuntimeException{
    public  UserNotFoundException(String message){
        super(message);
    }
}
