package com.github.dgzt.mundus.plugin.joltphysics.runtime;

public class InitResult {

    private boolean success;

    private Exception exception;

    public InitResult() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(final Exception exception) {
        this.exception = exception;
    }
}
