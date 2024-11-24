package com.uberith.api.game.script.treescript.permissive;

public class Result<T> {
    private final T result;
    private int expirationTime;
    private final long resultTime;

    public Result(T result) {
        this.result = result;
        expirationTime = 2000;
        resultTime = System.currentTimeMillis();
    }

    public Result(T result, int expirationTime) {
        this.result = result;
        this.expirationTime = expirationTime;
        resultTime = System.currentTimeMillis();
    }

    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean isValidResult() {
        return System.currentTimeMillis() - resultTime <= expirationTime;
    }

    public T getResult() {
        return result;
    }

    public ResultType getResultType() {
        return !isValidResult() ? ResultType.EXPIRED : ResultType.getResult((boolean)result);
    }
}

