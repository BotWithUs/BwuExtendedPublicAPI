package com.uberith.api.game.hud;

import com.uberith.api.game.hud.inventories.Backpack;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.util.RandomGenerator;

public class ConfirmItemDestruction {
    public static final int NO_ID = 77529094, YES_ID = 77529093;

    /**
     * Checks if the confirmation interface for item destruction is open.
     *
     * @return true if the interface is open, false otherwise.
     */
    public static boolean isOpen() {
        return Interfaces.isOpen(1183);
    }

    /**
     * Destroys an item in the player's backpack.
     *
     * @param name The name of the item to be destroyed.
     */
    public static void confirm(String name) {
        // Ensure the item exists in the backpack and the confirmation interface is not already open
        if (Backpack.contains(name) && !isOpen()) {
            // Use the correct interact method with a string option
            Backpack.interact(name, "Destroy");
            Execution.delayUntil(RandomGenerator.nextInt(800, 1400), ConfirmItemDestruction::isOpen);
        }

        // Confirm the destruction if the interface is open
        if (isOpen()) {
            MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, YES_ID);
        }
    }
}
