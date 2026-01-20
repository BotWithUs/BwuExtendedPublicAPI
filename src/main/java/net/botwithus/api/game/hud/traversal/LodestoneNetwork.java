package net.botwithus.api.game.hud.traversal;

import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;

public class LodestoneNetwork {
    private static final String LODESTONE_OPTION = "Lodestone Network";
    private static final String LODESTONE_OPTION_LEGACY = "Lodestone network";

    public static boolean isOpen() {
        return Interfaces.isOpen(1092);
    }

    /**
     * Opens the Lodestone Network interface.
     *
     * @return {@code true} if the interface was opened, {@code false} otherwise.
     */
    public static boolean open() {
        var result = ComponentQuery.newQuery(1465).option(LODESTONE_OPTION).results().first();
        if (result != null) {
            return result.interact(LODESTONE_OPTION);
        }

        result = ComponentQuery.newQuery(1465).option(LODESTONE_OPTION_LEGACY).results().first();
        return result != null && result.interact(LODESTONE_OPTION_LEGACY);
    }

    /**
     * Teleports the player to their previous destination.
     *
     * @return true if the player was successfully teleported, false otherwise
     */
    public static boolean teleportToPreviousDestination() {
        var result = ComponentQuery.newQuery(1465).option("Previous Destination").results().first();
        return result != null && result.interact("Previous Destination");
    }
}
