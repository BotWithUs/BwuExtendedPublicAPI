package net.botwithus.api.game.hud.inventories;

import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.script.Execution;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for interacting with deposit boxes in the game.
 */
public final class DepositBox {

    private DepositBox() {
    }

    /**
     * Opens the nearest deposit box and deposits all items.
     *
     * @return true if the deposit box was opened and all items were deposited, false otherwise.
     */
    public static boolean open() {
        var obj = SceneObjectQuery.newQuery()
                .option("Deposit")
                .option("Deposit-All", "Deposit all")
                .results()
                .nearest();
        return obj != null && obj.interact("Deposit");
    }

    /**
     * Checks if the deposit box interface is open.
     *
     * @return true if the interface is open, false otherwise.
     */
    public static boolean isOpen() {
        return Interfaces.isOpen(11);
    }

    /**
     * Closes the deposit box interface.
     *
     * @return true if the interface was closed, false otherwise.
     */
    public static boolean close() {
        var result = ComponentQuery.newQuery(11)
                .option("Close")
                .results()
                .first();
        return result != null && result.interact();
    }

    /**
     * Deposits all items in the backpack.
     *
     * @return true if all items were deposited, false otherwise.
     */
    public static boolean depositAll() {
        var result = ComponentQuery.newQuery(11)
                .option("Deposit Carried Items")
                .results()
                .first();
        return result != null && result.interact();
    }

    /**
     * Deposits all items matching the specified patterns.
     *
     * @param patterns The patterns to match item names.
     * @return true if the items were deposited, false otherwise.
     */
    public static boolean depositAll(Pattern... patterns) {
        if (!DepositBox.isOpen() && !DepositBox.open()) {
            return false;
        }
        var itemsToDeposit = InventoryItemQuery.newQuery(new int[]{93})
                .name(patterns)
                .results();
        if (itemsToDeposit.isEmpty()) {
            return true;
        }

        for (var item : itemsToDeposit) {
            if (!Backpack.interact(item.getSlot(), "Deposit-All")) {
                return false;
            }
            Execution.delayUntil(1000, () -> Backpack.getSlot(item.getSlot()) == null);
        }
        DepositBox.close();
        return true;
    }

    /**
     * Deposits all items except those with the specified names.
     *
     * @param itemNames The names of items to exclude.
     * @return true if the action was successful, false otherwise.
     */
    public static boolean depositAllExcept(String... itemNames) {
        var items = Backpack.getItems().stream()
                .filter(item -> Arrays.stream(itemNames).noneMatch(name -> Objects.equals(item.getName(), name)))
                .collect(Collectors.toList());
        for (var item : items) {
            if (!Backpack.interact(item.getSlot(), "Deposit-All")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deposits all items except those with the specified IDs.
     *
     * @param ids The IDs of items to exclude.
     * @return true if the action was successful, false otherwise.
     */
    public static boolean depositAllExcept(int... ids) {
        var items = Backpack.getItems().stream()
                .filter(item -> Arrays.stream(ids).noneMatch(id -> item.getId() == id))
                .collect(Collectors.toList());
        for (var item : items) {
            if (!Backpack.interact(item.getSlot(), "Deposit-All")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deposits all items except those matching the specified patterns.
     *
     * @param patterns The patterns to match item names to exclude.
     * @return true if the action was successful, false otherwise.
     */
    public static boolean depositAllExcept(Pattern... patterns) {
        var items = Backpack.getItems().stream()
                .filter(item -> Arrays.stream(patterns).noneMatch(pattern ->
                        item.getName() != null && pattern.matcher(item.getName()).matches()))
                .collect(Collectors.toList());
        for (var item : items) {
            if (!Backpack.interact(item.getSlot(), "Deposit-All")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deposits all worn items.
     *
     * @return true if the action was successful, false otherwise.
     */
    public static boolean depositWornItems() {
        var result = ComponentQuery.newQuery(11)
                .option("Deposit Worn Items")
                .results()
                .first();
        return result != null && result.interact();
    }

    /**
     * Deposits the familiar's items.
     *
     * @return true if the action was successful, false otherwise.
     */
    public static boolean depositFamiliarItems() {
        var result = ComponentQuery.newQuery(11)
                .option("Deposit Familiar's Items")
                .results()
                .first();
        return result != null && result.interact();
    }

    /**
     * Deposits the money pouch.
     *
     * @return true if the action was successful, false otherwise.
     */
    public static boolean depositMoneyPouch() {
        var result = ComponentQuery.newQuery(11)
                .option("Deposit Money Pouch")
                .results()
                .first();
        return result != null && result.interact();
    }
}
