package com.cbo.CBO_NFOS_ICMS.exception;

public class UserAlreadyExistsException extends RuntimeException{
    private String message;

    public UserAlreadyExistsException() {}

    public UserAlreadyExistsException(String msg)
    {
        super(msg);
        this.message = msg;
    }
}
