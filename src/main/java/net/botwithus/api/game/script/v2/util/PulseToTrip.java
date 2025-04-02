package net.botwithus.api.game.script.v2.util;

import net.botwithus.api.game.script.treescript.permissive.Result;

public class PulseToTrip {
    private Result<Integer> tripCounter;
    private int totalTripCount, timeout;
    private boolean hasTripped = false;
    private Runnable runnable;

    public PulseToTrip(int totalTripCount, int timeout, Runnable runnable) {
        tripCounter = new Result<>(0, timeout);
        this.totalTripCount = totalTripCount;
        this.timeout = timeout;
        this.runnable = runnable;
    }

    public void sendPulse() {
        if (tripCounter.isValidResult() && tripCounter.getResult() < totalTripCount) {
            tripCounter = new Result<>(tripCounter.getResult() + 1, timeout);
        }

        if (tripCounter.isValidResult() && tripCounter.getResult() >= totalTripCount) {
            hasTripped = true;
        } else {
            tripCounter = new Result<>(1, 5000);
        }
    }

    public void runTripAction() {
        runnable.run();
    }

    public boolean hasTripped() {
        return hasTripped;
    }
}
