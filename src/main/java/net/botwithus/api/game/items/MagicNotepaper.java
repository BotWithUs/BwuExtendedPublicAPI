package net.botwithus.api.game.items;

import net.botwithus.rs3.game.inventories.Backpack;
import net.botwithus.rs3.script.Execution;

import java.util.Random;
import java.util.logging.Logger;

/**
 * API for managing item interactions involving Magic Notepaper.
 */
public final class MagicNotepaper {

    private static final Logger logger = Logger.getLogger(MagicNotepaper.class.getName());
    private static final int MAGIC_NOTEPAPER_ID = 30372; // Magic Notepaper item ID

    // Private constructor to prevent instantiation
    private MagicNotepaper() {
    }

    /**
     * Uses Magic Notepaper on a specified item in the player's backpack.
     *
     * @param itemName The name of the item to note.
     * @return True if the interaction was successful, false otherwise.
     */
    public static boolean useMagicNotepaper(String itemName) {
        // Check if Magic Notepaper is in the backpack
        if (!Backpack.contains(MAGIC_NOTEPAPER_ID)) {
            logger.warning("[MagicNotepaper] No Magic Notepaper found in the backpack.");
            return false;
        }

        // Check if the target item (e.g., logs) is in the backpack
        if (!Backpack.contains(itemName)) {
            logger.warning("[MagicNotepaper] Target item '" + itemName + "' not found in the backpack.");
            return false;
        }

        // Attempt to use Magic Notepaper on the item
        boolean interactionSuccessful = Backpack.use(MAGIC_NOTEPAPER_ID) && Backpack.interact(itemName, "Use");

        if (interactionSuccessful) {
            logger.info("[MagicNotepaper] Successfully used Magic Notepaper on " + itemName + ".");
            Execution.delay(getRandomDelay()); // Add a random delay to simulate natural behavior
            return true;
        } else {
            logger.severe("[MagicNotepaper] Failed to use Magic Notepaper on " + itemName + ".");
            return false;
        }
    }

    /**
     * Checks if Magic Notepaper is available in the player's backpack.
     *
     * @return True if Magic Notepaper is present; false otherwise.
     */
    public static boolean hasMagicNotepaper() {
        boolean hasNotepaper = Backpack.contains(MAGIC_NOTEPAPER_ID);
        if (hasNotepaper) {
            logger.info("[MagicNotepaper] Magic Notepaper is available in the backpack.");
        } else {
            logger.warning("[MagicNotepaper] Magic Notepaper is not available in the backpack.");
        }
        return hasNotepaper;
    }

    /**
     * Generates a random delay between 500ms and 1000ms.
     *
     * @return A random delay in milliseconds.
     */
    private static long getRandomDelay() {
        return 500 + new Random().nextInt(501); // Random number between 500 and 1000
    }
}
