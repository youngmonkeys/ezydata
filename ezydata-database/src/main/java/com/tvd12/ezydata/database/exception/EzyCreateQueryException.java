package com.tvd12.ezydata.database.exception;

public class EzyCreateQueryException extends IllegalArgumentException {
    private static final long serialVersionUID = -53434355403128205L;

    public EzyCreateQueryException(String msg, Exception e) {
        super(msg, e);
    }

}
