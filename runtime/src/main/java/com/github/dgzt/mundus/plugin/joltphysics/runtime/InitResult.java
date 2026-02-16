package com.github.dgzt.mundus.plugin.joltphysics.runtime;

public class InitResult {

    private boolean success;

    private Throwable exception;

    public InitResult() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(final Throwable exception) {
        this.exception = exception;
    }
}
