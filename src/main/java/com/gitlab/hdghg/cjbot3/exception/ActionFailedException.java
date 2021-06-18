package com.gitlab.hdghg.cjbot3.exception;

public class ActionFailedException extends Exception {

    public ActionFailedException() {
        super();
    }

    public ActionFailedException(String message) {
        super(message);
    }

    public ActionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionFailedException(Throwable cause) {
        super(cause);
    }

}
