package net.botwithus.api.game.script.v2.permissive;

import net.botwithus.rs3.script.ScriptConsole;

import java.util.function.Supplier;

public class Permissive implements Supplier<Boolean> {
    private final String name;
    private final Supplier<Boolean> predicate;
    private EvaluationResult<Boolean> lastResult = new EvaluationResult<>(false);

    public Permissive(String name, Supplier<Boolean> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    @Override
    public Boolean get() {
        try {
            boolean result = predicate.get();
//            script.getLogger().info("[" + Thread.currentThread().getName() + "]: " + "[Permissive] " + name + ": " + result);
            lastResult = new EvaluationResult<>(result);
            return result;
        } catch (Exception e) {
//            script.getLogger().severe(e.getMessage() + "\nException thrown in permissive predicate: " + name);
            ScriptConsole.println("[" + Thread.currentThread().getName() + "]: " + "[Permissive] " + name + ": Exception thrown in permissive predicate: " + e.getMessage());
            lastResult = new EvaluationResult<>(false);
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public EvaluationResult<Boolean> getLastResult() {
        return lastResult;
    }
}
