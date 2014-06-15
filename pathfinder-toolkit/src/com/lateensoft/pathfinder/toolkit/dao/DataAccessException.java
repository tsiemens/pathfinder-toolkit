package com.lateensoft.pathfinder.toolkit.dao;

public class DataAccessException extends Exception {
    public DataAccessException() {
        super();
    }

    public DataAccessException(String detailMessage) {
        super(detailMessage);
    }

    public DataAccessException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DataAccessException(Throwable throwable) {
        super(throwable);
    }
}
