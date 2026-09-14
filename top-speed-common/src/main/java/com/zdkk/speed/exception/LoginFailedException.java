package com.zdkk.speed.exception;

public class LoginFailedException extends BaseException {
    public LoginFailedException(String msg) {
        super(msg);
    }

    public LoginFailedException() {
        super();
    }
}
