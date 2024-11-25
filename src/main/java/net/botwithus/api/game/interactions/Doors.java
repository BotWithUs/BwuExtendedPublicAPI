package net.botwithus.api.game.interactions;

import net.botwithus.rs3.game.Coordinate;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;

/**
 * A utility class for interacting with doors in the game.
 */
public class Doors {

    /**
     * Attempts to interact with a door by its name within a specified radius.
     *
     * @param doorName          The name of the door.
     * @param interactionOption The option to interact with the door (e.g., "Open", "Quick-pay(100)").
     * @param playerPosition    The player's current position.
     * @param radius            The maximum radius to search for the door.
     * @return True if the interaction was successful, false otherwise.
     */
    public boolean interactWithDoor(String doorName, String interactionOption, Coordinate playerPosition, int radius) {
        return interactWithDoorByName(doorName, interactionOption, playerPosition, radius, 3);
    }

    /**
     * Attempts to interact with a door by its ID within a specified radius.
     *
     * @param doorId            The ID of the door.
     * @param interactionOption The option to interact with the door (e.g., "Open", "Quick-pay(100)").
     * @param playerPosition    The player's current position.
     * @param radius            The maximum radius to search for the door.
     * @param maxRetries        The maximum number of retries for the interaction.
     * @return True if the interaction was successful, false otherwise.
     */
    public boolean interactWithDoor(int doorId, String interactionOption, Coordinate playerPosition, int radius, int maxRetries) {
        int retries = 0;

        while (retries < maxRetries) {
            // Query for the door by ID
            SceneObject door = SceneObjectQuery.newQuery()
                    .id(doorId)
                    .option(interactionOption)
                    .hidden(false)
                    .results()
                    .nearestTo(playerPosition);

            if (door != null && isWithinRadius(door, playerPosition, radius)) {
                if (attemptInteraction(door, interactionOption)) {
                    return true;
                }
            }

            retries++;
            Execution.delay(300); // Short delay before retrying
        }

        return false;
    }

    /**
     * Attempts to interact with a door by its name within a specified radius.
     *
     * @param doorName          The name of the door.
     * @param interactionOption The option to interact with the door (e.g., "Open", "Quick-pay(100)").
     * @param playerPosition    The player's current position.
     * @param radius            The maximum radius to search for the door.
     * @param maxRetries        The maximum number of retries for the interaction.
     * @return True if the interaction was successful, false otherwise.
     */
    private boolean interactWithDoorByName(String doorName, String interactionOption, Coordinate playerPosition, int radius, int maxRetries) {
        int retries = 0;

        while (retries < maxRetries) {
            // Query for the door by name
            SceneObject door = SceneObjectQuery.newQuery()
                    .name(doorName)
                    .option(interactionOption)
                    .hidden(false)
                    .results()
                    .nearestTo(playerPosition);

            if (door != null && isWithinRadius(door, playerPosition, radius)) {
                if (attemptInteraction(door, interactionOption)) {
                    return true;
                }
            }

            retries++;
            Execution.delay(300); // Short delay before retrying
        }

        return false;
    }

    /**
     * Helper method to attempt an interaction with a door.
     *
     * @param door              The door object to interact with.
     * @param interactionOption The option to interact with the door.
     * @return True if the interaction was successful, false otherwise.
     */
    private boolean attemptInteraction(SceneObject door, String interactionOption) {
        if (door.interact(interactionOption)) {
            Execution.delay(500); // Simulate delay after interaction

            // Validate if the door state has changed
            SceneObject updatedDoor = SceneObjectQuery.newQuery()
                    .id(door.getId())
                    .results()
                    .nearest();

            return updatedDoor == null || updatedDoor.isHidden();
        }
        return false;
    }

    /**
     * Checks if a door is within a specified radius of the player's position.
     *
     * @param door           The door object to check.
     * @param playerPosition The player's current position.
     * @param radius         The maximum radius to check.
     * @return True if the door is within the radius, false otherwise.
     */
    private boolean isWithinRadius(SceneObject door, Coordinate playerPosition, int radius) {
        Double distance = door.getCoordinate() != null
                ? door.getCoordinate().distanceTo(playerPosition)
                : null;
        return distance != null && distance <= radius;
    }
}
