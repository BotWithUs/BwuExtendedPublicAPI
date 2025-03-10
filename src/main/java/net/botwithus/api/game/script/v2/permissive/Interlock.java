package net.botwithus.api.game.script.v2.permissive;

import java.util.Arrays;

public class Interlock {
    private String name;
    private Permissive[] permissives;
    private EvaluationResult<Boolean> status = new EvaluationResult<>(false);

    private Permissive firstOut = null;

    public Interlock(String name, Permissive... permissives) {
        this.name = name;
        this.permissives = permissives;
    }

    public boolean isActive() {
        for (Permissive permissive : permissives) {
            if (!permissive.get()) {
                firstOut = permissive;
                return false;
            }
        }
        firstOut = null;
        return true;
    }

    public String getName() {
        return name;
    }

    public ResultType getResultType() {
        return status.getResultType();
    }

    public void extend(Permissive... permissives) {
        var newPermissives = Arrays.copyOf(this.permissives, this.permissives.length + permissives.length);
        System.arraycopy(permissives, 0, newPermissives, this.permissives.length, permissives.length);
    }

    public Permissive getFirstOut() {
        return firstOut;
    }

    public Permissive[] getPermissives() {
        return permissives;
    }
}
