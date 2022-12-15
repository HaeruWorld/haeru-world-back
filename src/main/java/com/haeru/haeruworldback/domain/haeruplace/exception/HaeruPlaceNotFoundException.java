package com.haeru.haeruworldback.domain.haeruplace.exception;

public class HaeruPlaceNotFoundException extends RuntimeException {
    public HaeruPlaceNotFoundException() {
    }

    public HaeruPlaceNotFoundException(String message) {
        super(message);
    }

    public HaeruPlaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public HaeruPlaceNotFoundException(Throwable cause) {
        super(cause);
    }
}
