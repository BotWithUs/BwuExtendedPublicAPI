package com.uberith.api.game.hud;

import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dialog {
    public static boolean isOpen() {
        return Interfaces.areAnyOpen(1184, 1186,  1188, 1189, 1191);
    }

    public static boolean select() {
        if (Interfaces.isOpen(1184)) {
            return MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, 77594639);
        } else if (Interfaces.isOpen(1186)) {
            return MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, 77725700);
        } else if (Interfaces.isOpen(1189)) {
            return MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, 77922323);
        } else if (Interfaces.isOpen(1191)) {
            return MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, 78053391);
        }
        return false;
    }

    @NotNull
    public static List<String> getOptions() {
        if (Interfaces.isOpen(1188)) {
            List<String> options = new ArrayList<>(ComponentQuery.newQuery(1188).componentIndex(6,33,35,37,39).type(4).results().stream().map(Component::getText).toList());
            options.removeIf(result -> result.getBytes(StandardCharsets.UTF_8).length < 3);
            return options;
        }
        return Collections.emptyList();
    }
    public static boolean hasOption(String string) {
        return getOptions().stream().anyMatch(i -> i.contentEquals(string));
    }

    public static boolean interact(String optionText) {
        if (Interfaces.isOpen(1188)) {
            var result = ComponentQuery.newQuery(1188).type(4).text(optionText, String::contentEquals).results().first();
            if (result != null) {
                int slot = -1;
                var options = getOptions();
                int size = options.size();
                for (int i = 0; i < size; i++) {
                    if (options.get(i).contains(optionText)) {
                        slot = i;
                    }
                }
                return interact(slot);
            }
        }
        return false;
    }

    /**
     * Interacts with the dialogue interface with the given index. The index is the position of the option in the dialogue, starting at 0.
     *
     * @param index The index of the option to interact with.
     *              If the index is -1, the first option will be selected.
     * @return True if the interaction was successful, false otherwise.
     */
    public static boolean interact(int index) {
        if (Interfaces.isOpen(1188)) {
            if (index != -1) {
                int[] opcode = new int[]{77856776, 77856781, 77856786, 77856791, 77856796};
                return MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, opcode[index]);
            }
        }
        return false;
    }

    /**
     * Retrieves the text from a component if the interface is open.
     *
     * @return The text from the component, or an empty string if the interface is not open.
     */
    @Nullable
    public static String getText() {
        if (Interfaces.isOpen(1184)) {
            var result = ComponentQuery.newQuery(1184).componentIndex(10).results().first();
            if (result != null && result.getText() != null) {
                return result.getText();
            }
        } else if (Interfaces.isOpen(1189)) {
            var result = ComponentQuery.newQuery(1189).componentIndex(3).results().first();
            if (result != null && result.getText() != null) {
                return result.getText();

            }
        } else if (Interfaces.isOpen(1186)) {
            var result = ComponentQuery.newQuery(1186).componentIndex(3).results().first();
            if (result != null && result.getText() != null) {
                return result.getText();
            }
        }
        return null;
    }

    @Nullable
    public static String getTitle() {
        Component result = null;
        if (Interfaces.isOpen(1184)) {
            result = ComponentQuery.newQuery(1184).type(4).results().first();
        } else if (Interfaces.isOpen(1188)) {
            result = ComponentQuery.newQuery(1188).type(4).results().first();
        } else if (Interfaces.isOpen(1189)) {
            result = ComponentQuery.newQuery(1189).type(4).results().first();
        } else if (Interfaces.isOpen(1191)) {
            result = ComponentQuery.newQuery(1191).type(4).results().first();
        }
        return result != null ? result.getText() : null;
    }
}
