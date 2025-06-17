package net.botwithus.api.game.script.v2.util;

import net.botwithus.api.game.hud.Hud;
import net.botwithus.api.util.time.Timer;
import net.botwithus.rs3.game.login.LoginManager;
import net.botwithus.rs3.game.Client;

import com.google.gson.JsonObject;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import net.botwithus.rs3.script.ScriptConsole;

public class BreakScheduler {
    private static final Random RANDOM = new Random();
    
    // configuration
    private boolean enabled = false;
    private int minBreakDurationMinutes = 5;
    private int maxBreakDurationMinutes = 15;
    private int minRunDurationMinutes = 60;
    private int maxRunDurationMinutes = 120;
    
    // tracking
    private Timer runTimer;
    private Timer breakTimer;
    private boolean inBreak = false;
    private long nextBreakDurationMs;
    private long nextRunDurationMs;
    private CompletableFuture<Void> breakTask;
    private Thread breakThread;
    
    // stats
    private int totalBreaksTaken = 0;
    private long totalBreakTimeMs = 0;
    
    public BreakScheduler() {
        generateNextRunDuration();
        this.runTimer = new Timer(nextRunDurationMs, nextRunDurationMs);
        this.runTimer.start();
    }
    
    /**
     * Updates the break scheduler state. Should be called in the main loop.
     * @return true if the script should continue running, false if in break
     */
    public boolean update() {
        if (!enabled) {
            return true;
        }
        
        if (!inBreak) {
            if (runTimer.hasExpired()) {
                startBreak();
                return false;
            }
            return true;
        } else {
            if (breakThread != null && breakThread.isAlive()) {
                return false;
            } else {
                endBreak();
                return true; 
            }
        }
    }
    
    private void startBreak() {
        inBreak = true;
        generateNextBreakDuration();
        breakTimer = new Timer(nextBreakDurationMs, nextBreakDurationMs);
        totalBreaksTaken++;
        totalBreakTimeMs += nextBreakDurationMs;
        
        ScriptConsole.println("[BreakScheduler] Starting break for " + (nextBreakDurationMs / 60000) + " minutes");
        
        // virtual break thread
        breakThread = Thread.ofVirtual().start(() -> {
            try {
                LoginManager.setAutoLogin(false);
                ScriptConsole.println("[BreakScheduler] Auto-login disabled");
                
                if (Client.getGameState() == Client.GameState.LOGGED_IN) {
                    while (Client.getLocalPlayer() != null && Client.getLocalPlayer().inCombat()) {
                        ScriptConsole.println("[BreakScheduler] Player is in combat, waiting before logout...");
                        Thread.sleep(5000);
                        if (!inBreak) {
                            ScriptConsole.println("[BreakScheduler] Break cancelled while waiting for combat to end");
                            return;
                        }
                    }
                    
                    Hud.logout();
                    ScriptConsole.println("[BreakScheduler] Logged out");
                }
                
                long breakStartTime = System.currentTimeMillis();
                long breakEndTime = breakStartTime + nextBreakDurationMs;
                
                while (System.currentTimeMillis() < breakEndTime && inBreak) {
                    Thread.sleep(5000); 
                    
                    long remainingMs = breakEndTime - System.currentTimeMillis();
                    if (remainingMs <= 0) {
                        break;
                    }
                    
                    ScriptConsole.println("[BreakScheduler] Break in progress - " + (remainingMs / 60000) + " minutes remaining");
                }
                
                if (inBreak) {
                    ScriptConsole.println("[BreakScheduler] Break completed, enabling auto-login");
                    LoginManager.setAutoLogin(true);
                }
                
            } catch (InterruptedException e) {
                ScriptConsole.println("[BreakScheduler] Break thread interrupted");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                ScriptConsole.println("[BreakScheduler] Error during break: " + e.getMessage());
                try {
                    LoginManager.setAutoLogin(true);
                } catch (Exception ex) {
                    ScriptConsole.println("[BreakScheduler] Failed to re-enable auto-login after error: " + ex.getMessage());
                }
            }
        });
    }
    
    private void endBreak() {
        inBreak = false;
        generateNextRunDuration();
        runTimer = new Timer(nextRunDurationMs, nextRunDurationMs);
        runTimer.start();
        
        if (breakThread != null && breakThread.isAlive()) {
            breakThread.interrupt();
        }
        
        if (breakTask != null && !breakTask.isDone()) {
            breakTask.cancel(true);
        }
        
        try {
            ScriptConsole.println("[BreakScheduler] Break ended, ensuring auto-login is enabled");
            ScriptConsole.println("[BreakScheduler] Next break scheduled in " + (nextRunDurationMs / 60000) + " minutes");
            LoginManager.setAutoLogin(true);
        } catch (Exception e) {
            ScriptConsole.println("[BreakScheduler] Failed to enable auto-login: " + e.getMessage());
        }
    }
    
    private void generateNextRunDuration() {
        nextRunDurationMs = (minRunDurationMinutes + RANDOM.nextInt(maxRunDurationMinutes - minRunDurationMinutes + 1)) * 60 * 1000L;
    }
    
    private void generateNextBreakDuration() {
        nextBreakDurationMs = (minBreakDurationMinutes + RANDOM.nextInt(maxBreakDurationMinutes - minBreakDurationMinutes + 1)) * 60 * 1000L;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled && runTimer == null) {
            generateNextRunDuration();
            this.runTimer = new Timer(nextRunDurationMs, nextRunDurationMs);
            this.runTimer.start();
        }
    }
    
    public boolean isInBreak() {
        return inBreak;
    }
    
    public int getMinBreakDurationMinutes() {
        return minBreakDurationMinutes;
    }
    
    public void setMinBreakDurationMinutes(int minBreakDurationMinutes) {
        this.minBreakDurationMinutes = Math.max(1, minBreakDurationMinutes);
        if (this.minBreakDurationMinutes > maxBreakDurationMinutes) {
            this.maxBreakDurationMinutes = this.minBreakDurationMinutes;
        }
    }
    
    public int getMaxBreakDurationMinutes() {
        return maxBreakDurationMinutes;
    }
    
    public void setMaxBreakDurationMinutes(int maxBreakDurationMinutes) {
        this.maxBreakDurationMinutes = Math.max(minBreakDurationMinutes, maxBreakDurationMinutes);
    }
    
    public int getMinRunDurationMinutes() {
        return minRunDurationMinutes;
    }
    
    public void setMinRunDurationMinutes(int minRunDurationMinutes) {
        this.minRunDurationMinutes = Math.max(1, minRunDurationMinutes);
        if (this.minRunDurationMinutes > maxRunDurationMinutes) {
            this.maxRunDurationMinutes = this.minRunDurationMinutes;
        }
    }
    
    public int getMaxRunDurationMinutes() {
        return maxRunDurationMinutes;
    }
    
    public void setMaxRunDurationMinutes(int maxRunDurationMinutes) {
        this.maxRunDurationMinutes = Math.max(minRunDurationMinutes, maxRunDurationMinutes);
    }
    
    public long getRemainingRunTimeMs() {
        return inBreak ? 0 : (runTimer != null ? runTimer.getRemainingTime() : 0);
    }
    
    public long getRemainingBreakTimeMs() {
        return inBreak ? (breakTimer != null ? breakTimer.getRemainingTime() : 0) : 0;
    }
    
    public int getTotalBreaksTaken() {
        return totalBreaksTaken;
    }
    
    public long getTotalBreakTimeMs() {
        return totalBreakTimeMs;
    }
    
    public String getStatus() {
        if (!enabled) {
            return "Disabled";
        }
        
        if (inBreak) {
            if (breakTimer != null) {
                long remainingMs = breakTimer.getRemainingTime();
                long remainingMinutes = remainingMs / (60 * 1000);
                long remainingSeconds = (remainingMs % (60 * 1000)) / 1000;
                return String.format("On Break (%dm %ds remaining)", remainingMinutes, remainingSeconds);
            } else {
                return "On Break (calculating time...)";
            }
        } else {
            if (runTimer != null) {
                long remainingMs = runTimer.getRemainingTime();
                long remainingMinutes = remainingMs / (60 * 1000);
                long remainingSeconds = (remainingMs % (60 * 1000)) / 1000;
                return String.format("Running (%dm %ds until break)", remainingMinutes, remainingSeconds);
            } else {
                return "Running (initializing timer...)";
            }
        }
    }
    
    public void saveToJson(JsonObject obj) {
        JsonObject breakScheduler = new JsonObject();
        breakScheduler.addProperty("enabled", enabled);
        breakScheduler.addProperty("minBreakDurationMinutes", minBreakDurationMinutes);
        breakScheduler.addProperty("maxBreakDurationMinutes", maxBreakDurationMinutes);
        breakScheduler.addProperty("minRunDurationMinutes", minRunDurationMinutes);
        breakScheduler.addProperty("maxRunDurationMinutes", maxRunDurationMinutes);
        breakScheduler.addProperty("totalBreaksTaken", totalBreaksTaken);
        breakScheduler.addProperty("totalBreakTimeMs", totalBreakTimeMs);
        obj.add("breakScheduler", breakScheduler);
    }
    
    public void loadFromJson(JsonObject obj) {
        if (obj.has("breakScheduler")) {
            JsonObject breakScheduler = obj.getAsJsonObject("breakScheduler");
            
            if (breakScheduler.has("enabled")) {
                enabled = breakScheduler.get("enabled").getAsBoolean();
            }
            if (breakScheduler.has("minBreakDurationMinutes")) {
                setMinBreakDurationMinutes(breakScheduler.get("minBreakDurationMinutes").getAsInt());
            }
            if (breakScheduler.has("maxBreakDurationMinutes")) {
                setMaxBreakDurationMinutes(breakScheduler.get("maxBreakDurationMinutes").getAsInt());
            }
            if (breakScheduler.has("minRunDurationMinutes")) {
                setMinRunDurationMinutes(breakScheduler.get("minRunDurationMinutes").getAsInt());
            }
            if (breakScheduler.has("maxRunDurationMinutes")) {
                setMaxRunDurationMinutes(breakScheduler.get("maxRunDurationMinutes").getAsInt());
            }
            if (breakScheduler.has("totalBreaksTaken")) {
                totalBreaksTaken = breakScheduler.get("totalBreaksTaken").getAsInt();
            }
            if (breakScheduler.has("totalBreakTimeMs")) {
                totalBreakTimeMs = breakScheduler.get("totalBreakTimeMs").getAsLong();
            }
            
            validateAndUpdateCurrentTimer();
        }
    }
    
    /**
     * Validates the current timer against the loaded settings and updates if necessary
     */
    private void validateAndUpdateCurrentTimer() {
        if (enabled && !inBreak && runTimer != null) {
            long currentDurationMs = runTimer.getTimerDuration();
            long minRunMs = minRunDurationMinutes * 60 * 1000L;
            long maxRunMs = maxRunDurationMinutes * 60 * 1000L;
            
            if (currentDurationMs < minRunMs || currentDurationMs > maxRunMs) {
                ScriptConsole.println("[BreakScheduler] Current break time (" + (currentDurationMs / 60000) + " minutes) is outside the new range (" + 
                    minRunDurationMinutes + "-" + maxRunDurationMinutes + " minutes). Generating new break time.");
                generateNewBreakTime();
            }
        }
    }
    
    /**
     * Generates a new break time within the current min/max range and updates the timer
     */
    public void generateNewBreakTime() {
        if (!inBreak) {
            generateNextRunDuration();
            runTimer = new Timer(nextRunDurationMs, nextRunDurationMs);
            runTimer.start();
            ScriptConsole.println("[BreakScheduler] Generated new break time: " + (nextRunDurationMs / 60000) + " minutes");
        } else {
            ScriptConsole.println("[BreakScheduler] Cannot generate new break time while in break");
        }
    }
    
    /**
     * Forces the break scheduler to take a break immediately
     */
    public void forceBreak() {
        if (!inBreak && enabled) {
            runTimer.setExpired();
            startBreak();
        }
    }
    
    /**
     * Forces the break scheduler to end the current break
     */
    public void endBreakEarly() {
        if (inBreak) {
            endBreak();
        }
    }
    
    /**
     * Resets all break statistics
     */
    public void resetStats() {
        totalBreaksTaken = 0;
        totalBreakTimeMs = 0;
    }
    
    /**
     * Cleanup method to stop any running threads
     */
    public void shutdown() {
        if (breakThread != null && breakThread.isAlive()) {
            breakThread.interrupt();
        }
        if (breakTask != null && !breakTask.isDone()) {
            breakTask.cancel(true);
        }
    }
} 