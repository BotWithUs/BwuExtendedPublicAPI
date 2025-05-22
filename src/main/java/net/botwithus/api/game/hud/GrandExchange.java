package net.botwithus.api.game.hud;

import java.util.Map;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.cs2.ScriptBuilder;
import net.botwithus.rs3.game.cs2.layouts.Layout;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;
import net.botwithus.rs3.game.queries.results.ResultSet;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.game.vars.VarManager;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.util.RandomGenerator;

public class GrandExchange {
    public static final ScriptBuilder GRAND_EXCHANGE_SELECT;
    public static final ScriptBuilder SEND_INPUT;
    public static final ScriptBuilder CANCEL_INPUT;
    private static final int GE_INTERFACE = 105;
    private static final int CONFIRM_BUTTON_UID = 6881605;
    private static final Map<Integer, Integer> SLOT_QUERY_MAP;

    public GrandExchange() {
    }

    public static int getCurrentItemID() {
        return VarManager.getVarpValue(139) != -1
                ? VarManager.getVarpValue(135)
                : -1;
    }

    public static int getCurrentQuantity() {
        return VarManager.getVarpValue(139) != -1
                ? VarManager.getVarpValue(136)
                : -1;
    }

    public static int getCurrentPrice() {
        return VarManager.getVarpValue(139) != -1
                ? VarManager.getVarpValue(137)
                : -1;
    }

    public static int getSelectedSlot() {
        return VarManager.getVarpValue(139) != -1
                ? VarManager.getVarpValue(138) + 1
                : -1;
    }

    public static int getTotalPrice() {
        return VarManager.getVarpValue(139) != -1
                ? getCurrentQuantity() * getCurrentPrice()
                : -1;
    }

    public static boolean isBuyOffer() {
        return VarManager.getVarpValue(139) == 0;
    }

    public static boolean isSellOffer() {
        return VarManager.getVarpValue(139) == 1;
    }

    public static boolean collectToInventory() {
        return MiniMenu.interact(
                ComponentAction.COMPONENT.getType(),
                1, -1,
                42663942
        );
    }

    public static boolean collectToBank() {
        return MiniMenu.interact(
                ComponentAction.COMPONENT.getType(),
                1, -1,
                42663950
        );
    }

    public static boolean isGeOpen() {
        return Interfaces.isOpen(GE_INTERFACE);
    }

    public static boolean isInsideOffer() {
        return VarManager.getVarpValue(139) != -1;
    }

    /**
     * Replaced the while‐loop with an enhanced for‐loop
     */
    public static long getMoneyPouchValue() {
        ResultSet<Item> items = InventoryItemQuery
                .newQuery(new int[]{623})
                .results();
        long amount995 = 0L;
        long amount54830 = 0L;
        for (Item item : items) {
            if (item == null || item.getId() == -1) continue;
            if (item.getId() == 995) {
                amount995 = item.getStackSize();
            } else if (item.getId() == 54830) {
                amount54830 = item.getStackSize();
            }
        }
        return amount54830 * 1_000_000_000L + amount995;
    }

    public static ResultSet<Item> getItemsInSlot(int slot) {
        Integer query = SLOT_QUERY_MAP.get(slot);
        return query != null
                ? InventoryItemQuery.newQuery(new int[]{query}).results()
                : null;
    }

    /**
     * Replaced do‐while loop with a simple for‐each
     */
    public static SlotStatus getSlotStatus(int slot) {
        int[] baseIndices = {7, 28, 49, 70, 94, 118, 142, 166};
        if (slot < 1 || slot > baseIndices.length) {
            return SlotStatus.EMPTY_OFFER;
        }

        int idx = baseIndices[slot - 1];
        Component comp = ComponentQuery
                .newQuery(new int[]{GE_INTERFACE})
                .componentIndex(new int[]{idx})
                .subComponentIndex(new int[]{-1})
                .results().first();
        Component textComp = ComponentQuery
                .newQuery(new int[]{GE_INTERFACE})
                .componentIndex(new int[]{idx})
                .subComponentIndex(new int[]{5})
                .results().first();

        if (comp == null || textComp == null) {
            return SlotStatus.EMPTY_OFFER;
        }

        for (String option : comp.getOptions()) {
            if (option.contains("repeat")) {
                return textComp.getText().isEmpty()
                        ? SlotStatus.SUCCESSFUL
                        : SlotStatus.FAILED;
            }
            if (option.contains("Abort")) {
                return SlotStatus.PENDING;
            }
        }
        return SlotStatus.EMPTY_OFFER;
    }

    public static boolean setItemByID(int itemID) {
        if (!isInsideOffer() || itemID == -1) return false;
        if (getCurrentItemID() == itemID) return true;

        GRAND_EXCHANGE_SELECT.invokeExact(new Object[]{itemID});
        Execution.delay(RandomGenerator.nextInt(1200, 2400));
        return getCurrentItemID() == itemID;
    }

    public static boolean setQuantity(long amount) {
        ScriptConsole.println("Setting Quantity: " + amount);
        if (!isInsideOffer()) {
            ScriptConsole.println("Not inside an offer..");
            return false;
        }
        if (getCurrentQuantity() == amount) {
            ScriptConsole.println("Quantity == " + amount);
            return true;
        }
        if (interactWithInputComponent(6881517, 6881514, 6881515, amount)) {
            ScriptConsole.println("Quantity set.");
            return true;
        }
        ScriptConsole.println("Failed to set quantity.");
        return false;
    }

    public static boolean setPrice(long amount) {
        ScriptConsole.println("Setting Price: " + amount);
        if (!isInsideOffer()) {
            ScriptConsole.println("Not inside an offer.");
            return false;
        }
        if (getCurrentPrice() == amount) {
            ScriptConsole.println("Price set");
            return true;
        }
        if (interactWithInputComponent(6881558, 6881555, 6881556, amount)) {
            ScriptConsole.println("Price set.");
            return true;
        }
        ScriptConsole.println("Failed to set price.");
        return false;
    }

    private static boolean interactWithInputComponent(
            int interactComponent,
            int expectedValue,
            int inputComponent,
            long amount
    ) {
        if (VarManager.getVarc(2235) != expectedValue) {
            if (!MiniMenu.interact(
                    ComponentAction.COMPONENT.getType(),
                    1, -1,
                    interactComponent
            )) {
                ScriptConsole.println("Failed to interact with input component");
                return false;
            }
            if (!waitForComponent(2235, expectedValue, 3000)) {
                return false;
            }
            Execution.delay(RandomGenerator.nextInt(700, 1200));
        } else {
            Execution.delay(RandomGenerator.nextInt(700, 1200));
        }
        sendInputAndCancel(Long.toString(amount), inputComponent, 4);
        return true;
    }

    private static boolean waitForComponent(
            int varcIndex,
            int expectedValue,
            int timeout
    ) {
        return Execution.delayUntil(
                timeout,
                () -> VarManager.getVarc(varcIndex) == expectedValue
        );
    }

    private static void sendInputAndCancel(
            String input,
            int inputComponent,
            int cancelCode
    ) {
        SEND_INPUT.invokeExact(inputComponent, input);
        Execution.delay(RandomGenerator.nextInt(20, 50));
        CANCEL_INPUT.invokeExact(cancelCode);
        Execution.delay(RandomGenerator.nextInt(1200, 1800));
    }

    public static boolean placeBuyOffer(
            int itemID,
            int quantity,
            int price
    ) {
        if (!isGeOpen()) {
            ScriptConsole.println("Grand Exchange not open. Could not place buy offer.");
            return false;
        }

        long totalPrice = (long) price * quantity;
        long pouch = getMoneyPouchValue();
        if (totalPrice > pouch) {
            ScriptConsole.println(
                    "Can't afford. Total: " + totalPrice + " Pouch: " + pouch
            );
            return false;
        }

        if (!isInsideOffer() && !clickBuyComponent()) {
            ScriptConsole.println("Failed to enter buy offer...");
            return false;
        }
        Execution.delay(RandomGenerator.nextInt(1200, 1800));
        if (!isInsideOffer()) {
            ScriptConsole.println("Failed to enter the offer interface.");
            return false;
        }

        boolean okItem = Execution.delayUntil(10000L, () -> setItemByID(itemID));
        boolean okPrice = Execution.delayUntil(10000L, () -> setPrice(price));
        boolean okQty = Execution.delayUntil(10000L, () -> setQuantity(quantity));

        if (okItem && okPrice && okQty && clickConfirm()) {
            boolean completed = Execution.delayUntil(5000L, () -> !isInsideOffer());
            if (completed) {
                ScriptConsole.println("Buy offer complete");
                return true;
            }
            ScriptConsole.println("Failed to confirm");
        } else {
            ScriptConsole.println("Buy offer setup failed");
        }
        return false;
    }

    public static boolean clickBuyComponent() {
        if (!isGeOpen()) {
            ScriptConsole.println("Grand Exchange is not open.");
            return false;
        }
        if (isInsideOffer()) return true;

        int empty = findEmptySlot();
        if (empty == -1) {
            ScriptConsole.println("No empty slot available for buy offer.");
            return false;
        }

        int[] compIdx = {15, 36, 57, 78, 102, 126, 150, 174};
        int uid = ComponentQuery
                .newQuery(new int[]{GE_INTERFACE})
                .componentIndex(new int[]{compIdx[empty - 1]})
                .results().first()
                .getInterfaceIndex() << 16
                | ComponentQuery
                .newQuery(new int[]{GE_INTERFACE})
                .componentIndex(new int[]{compIdx[empty - 1]})
                .results().first()
                .getComponentIndex();

        if (MiniMenu.interact(
                ComponentAction.COMPONENT.getType(),
                1, -1,
                uid
        )) {
            return Execution.delayUntil(3000L, GrandExchange::isInsideOffer);
        }
        ScriptConsole.println("Failed to interact with buy component.");
        return false;
    }

    public static int findEmptySlot() {
        for (int i = 1; i <= 8; i++) {
            if (getSlotStatus(i) == SlotStatus.EMPTY_OFFER) {
                return i;
            }
        }
        return -1;
    }

    public static boolean clickConfirm() {
        return MiniMenu.interact(
                ComponentAction.COMPONENT.getType(),
                1, -1,
                CONFIRM_BUTTON_UID
        );
    }

    public static boolean interactWithClerk() {
        Npc clerk = NpcQuery.newQuery()
                .option("Exchange")
                .results().first();
        if (clerk != null) {
            return clerk.interact("Exchange");
        }
        ScriptConsole.println("Clerk is null");
        return false;
    }

    static {
        GRAND_EXCHANGE_SELECT = ScriptBuilder.of(11703)
                .args(Layout.INT);
        SEND_INPUT = ScriptBuilder.of(7208)
                .args(Layout.INT, Layout.STRING);
        CANCEL_INPUT = ScriptBuilder.of(15030)
                .args(Layout.INT);
        SLOT_QUERY_MAP = Map.of(
                1, 523, 2, 524, 3, 525, 4, 526,
                5, 527, 6, 528, 7, 783, 8, 784
        );
    }

    public enum SlotStatus {
        SUCCESSFUL,
        FAILED,
        PENDING,
        EMPTY_OFFER;
    }
}