package net.botwithus.api.game;

import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.login.LoginManager;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.script.Execution;

/**
 * Provides functionality for managing player sessions, including login and logout.
 */
public class Session {

    /**
     * Attempts to log out the player.
     *
     * @return True if the logout was successful, false otherwise.
     */
    public boolean logout() {
        if (Client.getGameState() != Client.GameState.LOGGED_IN) {
            return true; // Already logged out
        }

        disableAutoLogin();

        // Open settings menu
        if (!openSettings()) {
            return false;
        }

        // Interact with the logout button
        if (!selectLogout()) {
            return false;
        }

        // Wait for logout confirmation or state change
        return confirmLogout();
    }

    /**
     * Disables auto-login to prevent automatic re-login.
     */
    private void disableAutoLogin() {
        try {
            LoginManager.setAutoLogin(false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to disable auto-login: " + e.getMessage(), e);
        }
    }

    /**
     * Opens the settings menu.
     *
     * @return True if the settings menu was opened successfully, false otherwise.
     */
    private boolean openSettings() {
        var settingsButton = ComponentQuery.newQuery(1431)
                .componentIndex(0)
                .text("Settings")
                .option("Open")
                .results()
                .first();

        if (settingsButton == null) {
            return false;
        }

        return settingsButton.interact("Open") &&
                Execution.delayUntil(5000, () -> isInterfaceOpen(1433));
    }

    /**
     * Selects the logout option.
     *
     * @return True if the logout button was successfully interacted with, false otherwise.
     */
    private boolean selectLogout() {
        var logoutButton = ComponentQuery.newQuery(1433)
                .componentIndex(71)
                .results()
                .first();

        if (logoutButton == null) {
            return false;
        }

        return logoutButton.interact("Select");
    }

    /**
     * Confirms the logout operation.
     *
     * @return True if logout was confirmed successfully or no confirmation was required.
     */
    private boolean confirmLogout() {
        // Optional confirmation dialog
        var confirmationDialog = ComponentQuery.newQuery(1433)
                .componentIndex(85)
                .results()
                .first();

        if (confirmationDialog != null) {
            confirmationDialog.interact("Select");
        }

        return Execution.delayUntil(5000, () -> Client.getGameState() != Client.GameState.LOGGED_IN);
    }

    /**
     * Checks if a specific interface is open by its ID.
     *
     * @param interfaceId The ID of the interface to check.
     * @return True if the interface is open, false otherwise.
     */
    private boolean isInterfaceOpen(int interfaceId) {
        return !ComponentQuery.newQuery(interfaceId).results().isEmpty();
    }

    /**
     * Placeholder for future login functionality.
     * To be implemented as required.
     *
     * @param username The player's username.
     * @param password The player's password.
     * @return True if login was successful, false otherwise.
     */
    public boolean login(String username, String password) {
        throw new UnsupportedOperationException("Login functionality is not implemented yet.");
    }
}
