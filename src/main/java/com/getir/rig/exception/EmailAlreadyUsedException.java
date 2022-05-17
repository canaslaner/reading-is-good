package com.getir.rig.exception;

public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException() {
        super("exception.emailAlreadyUsed");
    }
}
