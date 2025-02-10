package net.botwithus.api.game.script.v2.permissive;

public enum ResultType {
    MET,
    NOT_MET,
    EXPIRED;

    public static ResultType getResult(boolean value) {
        return value ? ResultType.MET : ResultType.NOT_MET;
    }
}