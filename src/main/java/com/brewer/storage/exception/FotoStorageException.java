package com.brewer.storage.exception;

public class FotoStorageException extends RuntimeException {
    public FotoStorageException(String msg, Exception exception) {
        super(msg, exception);
    }
}
