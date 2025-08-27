package net.botwithus.api.game.script.v2.inventories;

import net.botwithus.api.game.hud.inventories.Inventory;
import net.botwithus.api.game.script.v2.base.DelayableScript;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.js5.types.EnumType;
import net.botwithus.rs3.game.js5.types.configs.ConfigManager;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.vars.VarManager;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.util.RandomGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public final class LootInventory {
    private static final int LOOT_INTERFACE = 1622;
    private static final int LOOT_VARP = 5413;
    private static final int INVENTORY_ID = 773;
    private static final Pattern ALL_PATTERN = Pattern.compile("^(.*)$");
    static final Inventory LOOT_INVENTORY = new Inventory(INVENTORY_ID, LOOT_INTERFACE, 5, i -> i + 1);

    private LootInventory() {
    }

    /**
     * Checks if any items match the given id
     *
     * @param ids the id to check the items against
     * @return true if an item matches the id
     */
    public static boolean contains(int... ids) {
        return LOOT_INVENTORY.contains(ids);
    }

    /**
     * Checks if any items match the given name
     *
     * @param names the name to check the items against
     * @return true if an item matches the name
     */
    public static boolean contains(String... names) {
        return LOOT_INVENTORY.contains(names);
    }

    /**
     * Checks if any items match the given name
     *
     * @param name the name to check the items against
     * @return true if an item matches the name
     */
    public static boolean contains(Pattern name) {
        return LOOT_INVENTORY.contains(name);
    }

    /**
     * Checks if all of the supplied ids match at least one item each
     *
     * @param ids the ids to check the items against
     * @return true if all of the ids have a match
     */
    public static boolean containsAllOf(final int... ids) {
        return LOOT_INVENTORY.containsAllOf(ids);
    }

    /**
     * Checks if all of the supplied names match at least one item each
     *
     * @param names the names to check the items against
     * @return true if all of the names have a match
     */
    public static boolean containsAllOf(final String... names) {
        return LOOT_INVENTORY.containsAllOf(names);
    }

    /**
     * Checks if all of the supplied names match at least one item each
     *
     * @param names the names to check the items against
     * @return true if all of the names have a match
     */
    public static boolean containsAllOf(final Pattern... names) {
        return LOOT_INVENTORY.containsAllOf(names);
    }

    /**
     * Checks if any items don't match the given ids
     *
     * @param ids the ids to check the items against
     * @return true if at least one item doesn't match the ids
     */
//    public static boolean containsAnyExcept(final int... ids) {
//        return LOOT_INVENTORY.containsAnyExcept(ids);
//    }

    /**
     * Checks if any items don't match the given names
     *
     * @param names the names to check the items against
     * @return true if at least one item doesn't match the names
     */
    public static boolean containsAnyExcept(final String... names) {
        return LOOT_INVENTORY.containsAnyExcept(names);
    }

    /**
     * Checks if any items don't match the given names
     *
     * @param names the names to check the items against
     * @return true if at least one item doesn't match the names
     */
    public static boolean containsAnyExcept(final Pattern... names) {
        return LOOT_INVENTORY.containsAnyExcept(names);
    }


    /**
     * Gets the total quantity of items
     *
     * @return the total quantity of items
     */
    public static int getQuantity() {
        return LOOT_INVENTORY.getQuantity(ALL_PATTERN);
    }

    /**
     * Gets the total quantity of items matching the ids
     *
     * @param ids the ids to check the items against
     * @return the total quantity of items matching the ids
     */
    public static int getQuantity(final int... ids) {
        return LOOT_INVENTORY.getQuantity(ids);
    }

    /**
     * Gets the total quantity of items matching the names
     *
     * @param names the ids to check the items against
     * @return the total quantity of items matching the names
     */
    public static int getQuantity(final String... names) {
        return LOOT_INVENTORY.getQuantity(names);
    }

    public static boolean close(DelayableScript script) {
        if (!isOpen()) {
            return true;
        }
        final var button = ComponentQuery.newQuery(LOOT_INTERFACE).option("Close").results().first();
        var result = button != null && button.interact("Close");
        if (result)
            script.delayUntil(LootInventory::isOpen, 2);
        return result;
    }

    public static Item getItemIn(final int index) {
        return LOOT_INVENTORY.getItems().get(index);
    }

    public static List<Item> getItems() {
        return LOOT_INVENTORY.getItems();
    }

    public static List<Item> getItems(Pattern namePattern) {
        return getItems().stream().filter(item -> namePattern.matcher(item.getName()).find()).toList();
    }

    public static List<Item> getItems(String... names) {
        return getItems().stream().filter(item -> Arrays.stream(names)
                .anyMatch(name -> Objects.equals(name, item.getName()))).toList();
    }


    public static boolean isEnabled() {
        return isMultipleItemsOpenEnabled() && VarManager.getVarbitValue(27943) == 1;
    }
    public static boolean isMultipleItemsOpenEnabled() {
        return VarManager.getVarbitValue(27942) == 1;
    }
    public static boolean isAreaLootEnabled() {
        return VarManager.getVarbitValue(27943) == 1;
    }
    public static boolean isDecideByMonetaryValueEnabled() {
        return VarManager.getVarbitValue(27945) == 1;
    }
    public static int getMonetaryValue() {
        int value = VarManager.getVarbitValue(27946);
        EnumType values = ConfigManager.getEnumType(375);
        if (values != null) {
            return (int) values.getOutput(value);
        }
        return -1;
    }

    public static boolean open() {
        return isEnabled() && MiniMenu.interact(ComponentAction.COMPONENT.getType(), 7, -1, 96796680);
    }

    public static boolean isOpen() {
        return Interfaces.isOpen(LOOT_INTERFACE);
    }

    public static boolean take(DelayableScript script, String... names) {
        var items = getItems(names);
        var result = items != null && items.size() > 0 ? getItems(names).get(0) : null;
        return take(script, result);
    }

    public static boolean take(DelayableScript script, Pattern namePattern) {
        var items = getItems(namePattern);
        var result = items != null && items.size() > 0 ? getItems(namePattern).get(0) : null;
        return take(script, result);
    }

    public static boolean take(DelayableScript script, Item item) {
        if (item == null || !isOpen()) {
            return false;
        }
        int quantity = getQuantity(item.getId());
        var itemComp = ComponentQuery.newQuery(LOOT_INTERFACE).item(item.getId()).results().first();
        var result = itemComp != null && itemComp.interact(1);
        if (result)
            script.delayUntil(() -> quantity != getQuantity(item.getId()), 3);
        return result;
    }

    public static boolean lootAll() {
        var component = ComponentQuery.newQuery(LOOT_INTERFACE).type(0).componentIndex(22).results().first();
        return isOpen() && component != null && component.interact(1) && Execution.delay(RandomGenerator.nextInt(1500, 3000));
    }

    public static boolean lootCustom(DelayableScript script) {
        var component = ComponentQuery.newQuery(LOOT_INTERFACE).type(Component.Type.TEXT.getOpcode()).text("Loot Custom", String::contentEquals).results().first();
        if (component != null && isOpen()) {
            int quantity = getQuantity();
            var result = component.interact("Select");
            if (result)
                script.delayUntil(() -> getQuantity() == quantity, 4);
            return result;
        }
        return false;
    }
}
