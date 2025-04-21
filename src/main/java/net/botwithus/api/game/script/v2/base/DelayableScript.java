package net.botwithus.api.game.script.v2.base;

import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.script.TickingScript;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.rs3.util.RandomGenerator;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;

import java.util.concurrent.Callable;

public abstract class DelayableScript extends TickingScript {

    public DelayableScript(String scriptName, ScriptConfig scriptConfig, ScriptDefinition scriptDef) {
        super(scriptName, scriptConfig, scriptDef);
    }


    private Callable<Boolean> delayUntil = null,
            delayWhile = null;
    private int delay = -1;

    @Override
    public void onTick(LocalPlayer localPlayer) {
        try {
            if (delayUntil != null) {
                if (delayUntil.call() || delay <= 1) {
                    delayUntil = null;
                    doRun();
                } else {
                    delay--;
                }
            } else if (delayWhile != null) {
                if (!delayWhile.call() || delay <= 1) {
                    delayWhile = null;
                    doRun();
                } else {
                    delay--;
                }
            } else {
                if (delay > 0)
                    delay--;

                if (delay <= 0)
                    doRun();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void doRun();

    public void delayUntil(Callable<Boolean> condition, int timeoutTicks) {
        delayUntil = condition;
        delay = timeoutTicks;
    }
    public void delayWhile(Callable<Boolean> condition, int timeoutTicks) {
        delayWhile = condition;
        delay = timeoutTicks;
    }
    public void delayTicks(int ticks) {
        delay = ticks;
    }
    public void delayTicks(int min, int max) {
        delay = RandomGenerator.nextInt(min, max);
    }
}
