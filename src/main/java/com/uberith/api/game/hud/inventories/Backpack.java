package com.uberith.api.game.hud.inventories;

import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;

import java.util.List;
import java.util.regex.Pattern;

/**
 * A class that provides methods to interact with the player's backpack.
 */
public final class Backpack {

    private static final BackpackInventory BACKPACK = new BackpackInventory();

    private Backpack() {
    }

    /**
     * Checks if the backpack is full.
     *
     * @return true if the backpack is full, false otherwise
     */
    public static boolean isFull() {
        return BACKPACK.isFull();
    }

    /**
     * Checks if the backpack is empty.
     *
     * @return true if the backpack is empty, false otherwise
     */
    public static boolean isEmpty() {
        return BACKPACK.isEmpty();
    }

    /**
     * Checks if the backpack contains an item by its name(s).
     *
     * @param names The names of the items to check.
     * @return true if any of the items are found, false otherwise.
     */
    public static boolean contains(String... names) {
        return BACKPACK.contains(names);
    }

    /**
     * Checks if the backpack contains an item by its ID(s).
     *
     * @param ids The IDs of the items to check.
     * @return true if any of the items are found, false otherwise.
     */
    public static boolean contains(int... ids) {
        return BACKPACK.contains(ids);
    }

    /**
     * Gets a list of all items in the backpack.
     *
     * @return A list of items in the backpack.
     */
    public static List<Item> getItems() {
        return BACKPACK.getItems();
    }

    /**
     * Gets the quantity of an item in the backpack by its name(s).
     *
     * @param names The names of the items to count.
     * @return The quantity of the specified items.
     */
    public static int getQuantity(String... names) {
        return BACKPACK.getQuantity(names);
    }

    /**
     * Gets the quantity of an item in the backpack by its ID(s).
     *
     * @param ids The IDs of the items to count.
     * @return The quantity of the specified items.
     */
    public static int getQuantity(int... ids) {
        return BACKPACK.getQuantity(ids);
    }

    /**
     * Gets an item from the backpack by its name.
     *
     * @param name The name of the item to retrieve.
     * @return The item if it exists, or null if not found.
     */
    public static Item getItem(String name) {
        return BACKPACK.getItem(name);
    }

    /**
     * Gets an item from the backpack that matches a pattern.
     *
     * @param pattern The pattern to match the item's name against.
     * @return The item if it exists, or null if not found.
     */
    public static Item getItem(Pattern pattern) {
        return BACKPACK.getItem(pattern);
    }

    /**
     * Gets a list of items in the backpack with a specific interaction option.
     *
     * @param option The option to match.
     * @return A list of items with the specified option.
     */
    public static List<Item> getItemsWithOption(String option) {
        return BACKPACK.getItemsWithOptions(option);
    }

    /**
     * Interacts with an item in the backpack by its name and an interaction option.
     *
     * @param name   The name of the item.
     * @param option The interaction option.
     * @return true if the interaction was successful, false otherwise.
     */
    public static boolean interact(String name, String option) {
        return BACKPACK.interact(name, option);
    }

    /**
     * Interacts with an item in the backpack by its ID and an interaction option.
     *
     * @param id     The ID of the item.
     * @param option The interaction option.
     * @return true if the interaction was successful, false otherwise.
     */
    public static boolean interact(int id, String option) {
        return BACKPACK.interact(id, option);
    }

    /**
     * Checks if the backpack contains all specified items by their names.
     *
     * @param names The names of the items to check.
     * @return true if all specified items are found, false otherwise.
     */
    public static boolean containsAllOf(String... names) {
        return BACKPACK.containsAllOf(names);
    }

    /**
     * Checks if the backpack contains any items except the specified names.
     *
     * @param names The names of the items to exclude.
     * @return true if any items are found that do not match the specified names.
     */
    public static boolean containsAnyExcept(String... names) {
        return BACKPACK.containsAnyExcept(names);
    }

    /**
     * Retrieves the selected item in the backpack.
     *
     * @return The selected item, or null if none is selected.
     */
    public static Item getSelectedItem() {
        return InventoryItemQuery.newQuery(93).results().first();
    }

    /**
     * Retrieves an item's varbit value from the backpack.
     *
     * @param slot     The slot of the item.
     * @param varbitId The ID of the varbit.
     * @return The varbit value.
     */
    public static int getVarbitValue(int slot, int varbitId) {
        return BACKPACK.getVarbitValue(slot, varbitId);
    }

    /**
     * Retrieves an item's count by a pattern matching its name.
     *
     * @param pattern The pattern to match against item names.
     * @return The total count of items matching the pattern.
     */
    public static int getCount(Pattern pattern) {
        return BACKPACK.getCount(pattern);
    }

    /**
     * Retrieves the item located in a specific slot in the backpack.
     *
     * @param slot The slot index to retrieve the item from.
     * @return The {@link Item} located in the specified slot, or {@code null} if the slot is empty.
     */

    public static Item getSlot(int slot) {
        return BACKPACK.getItems().stream()
                .filter(item -> item.getSlot() == slot)
                .findFirst()
                .orElse(null);
    }

}
