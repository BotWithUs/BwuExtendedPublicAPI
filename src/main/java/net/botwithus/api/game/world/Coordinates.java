package net.botwithus.api.game.world;

import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Coordinate;

import java.util.Optional;

/**
 * A utility class for handling player and coordinate-based operations.
 * Provides methods to check distances, proximity, and positional alignment within the game world.
 */
public class Coordinates {

    private final boolean debugEnabled;

    /**
     * Constructor to initialize the Coordinates class with optional debug logging.
     *
     * @param debugEnabled Enables or disables debug logging.
     */
    public Coordinates(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    /**
     * Checks if the player is currently at the specified coordinate.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @param z The z-coordinate to check.
     * @return True if the player is at the specified coordinate, false otherwise.
     */
    public boolean isPlayerAtCoordinate(int x, int y, int z) {
        Coordinate targetCoordinate = new Coordinate(x, y, z);
        Coordinate playerCoordinate = getPlayerCoordinate().orElse(null);

        boolean result = targetCoordinate.equals(playerCoordinate);

        if (debugEnabled) {
            logDebug("Checking if player is at coordinates: (" + x + ", " + y + ", " + z + ")");
            if (result) {
                logDebug("Player is at the target coordinates.");
            } else {
                logDebug("Player is not at the target coordinates.");
            }
        }

        return result;
    }

    /**
     * Checks if the player is within a specified radius of a center coordinate.
     *
     * @param centerX The x-coordinate of the center point.
     * @param centerY The y-coordinate of the center point.
     * @param centerZ The z-coordinate of the center point.
     * @param radius  The radius within which to check.
     * @return True if the player is within the radius, false otherwise.
     */
    public boolean isPlayerWithinRadius(int centerX, int centerY, int centerZ, int radius) {
        Coordinate center = new Coordinate(centerX, centerY, centerZ);
        Coordinate playerCoordinate = getPlayerCoordinate().orElse(null);

        if (playerCoordinate == null) {
            logWarning("Player's coordinate is unavailable. Cannot determine proximity.");
            return false;
        }

        double distance = calculateDistance(playerCoordinate, center);
        boolean result = distance <= radius;

        if (debugEnabled) {
            logDebug("Checking if player is within radius " + radius + " of (" + centerX + ", " + centerY + ", " + centerZ + ")");
            if (result) {
                logDebug("Player is within " + radius + " units of the center.");
            } else {
                logDebug("Player is outside the " + radius + " unit radius.");
            }
        }

        return result;
    }

    /**
     * Checks if the player is within a rectangular boundary defined by minimum and maximum coordinates.
     *
     * @param playerX The player's x-coordinate.
     * @param playerY The player's y-coordinate.
     * @param minX    The minimum x-boundary.
     * @param minY    The minimum y-boundary.
     * @param maxX    The maximum x-boundary.
     * @param maxY    The maximum y-boundary.
     * @return True if the player is within the rectangle, false otherwise.
     */
    public boolean isPlayerWithinSquare(int playerX, int playerY, int minX, int minY, int maxX, int maxY) {
        boolean result = playerX >= minX && playerX <= maxX && playerY >= minY && playerY <= maxY;

        if (debugEnabled) {
            logDebug("Checking if player is within square boundaries:");
            logDebug("Min: (" + minX + ", " + minY + "), Max: (" + maxX + ", " + maxY + ")");
            logDebug(result ? "Player is within the boundaries." : "Player is outside the boundaries.");
        }

        return result;
    }

    /**
     * Retrieves the current player's coordinates.
     *
     * @return An Optional containing the player's coordinate, or an empty Optional if unavailable.
     */
    public Optional<Coordinate> getPlayerCoordinate() {
        return Optional.ofNullable(Client.getLocalPlayer()).map(player -> player.getCoordinate());
    }

    /**
     * Calculates the Euclidean distance between two coordinates.
     *
     * @param coord1 The first coordinate.
     * @param coord2 The second coordinate.
     * @return The Euclidean distance between the two coordinates.
     */
    public double calculateDistance(Coordinate coord1, Coordinate coord2) {
        int dx = coord2.getX() - coord1.getX();
        int dy = coord2.getY() - coord1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Validates a coordinate to ensure it is not null.
     *
     * @param coordinate   The coordinate to validate.
     * @param errorMessage The error message to log if validation fails.
     * @return True if the coordinate is valid, false otherwise.
     */
    public boolean validateCoordinate(Coordinate coordinate, String errorMessage) {
        if (coordinate == null) {
            logWarning(errorMessage);
            return false;
        }
        return true;
    }

    /**
     * Logs debug messages if debug is enabled.
     *
     * @param message The debug message.
     */
    private void logDebug(String message) {
        if (debugEnabled) {
            System.out.println("[DEBUG] " + message); // Replace with your logger as necessary
        }
    }

    /**
     * Logs warnings regardless of the debug mode.
     *
     * @param message The warning message.
     */
    private void logWarning(String message) {
        System.out.println("[WARNING] " + message); // Replace with your logger as necessary
    }
}
