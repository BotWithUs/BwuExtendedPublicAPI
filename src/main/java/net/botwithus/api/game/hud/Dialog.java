package net.botwithus.api.game.hud;

import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dialog {
    public static boolean isOpen() {
        return Interfaces.areAnyOpen(1184, 1188, 1189, 1191);
    }

    public static boolean select() {
        if (Interfaces.isOpen(1184)) {
            return MiniMenu.interact(ComponentAction.DIALOGUE.getType(), 0, -1, 77594639);
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
            return ComponentQuery.newQuery(1188).componentIndex(8, 13, 18, 23, 28).type(4).results().stream().map(Component::getText).toList();
        }
        return Collections.emptyList();
    }

    public static boolean hasOption(String string) {
        return getOptions().stream().anyMatch(i -> i.contentEquals(string));
    }

    public static boolean interact(String optionText) {
        if (Interfaces.isOpen(1188)) {
            var result = ComponentQuery.newQuery(1188).type(4).text(optionText, String::contentEquals).results().first();
            return result != null && result.interact();
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
        if (isOpen()) {
            var result = ComponentQuery.newQuery(1184).componentIndex(10).results().first();
            if (result != null) {
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
