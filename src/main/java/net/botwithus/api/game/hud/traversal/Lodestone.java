package net.botwithus.api.game.hud.traversal;

import com.google.common.flogger.FluentLogger;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.util.RandomGenerator;
import net.botwithus.rs3.game.js5.types.configs.ConfigManager;
import net.botwithus.rs3.game.vars.VarManager;

public enum Lodestone {
    AL_KHARID(71565322, 28, 93961),
    ANACHRONIA(71565336, 44270, 194564),
    ARDOUGNE(71565323, 29, 94212),
    ASHDALE(71565345, 22430, 194560),
    BANDIT_CAMP(71565320, 9482, 194326),
    BURTHORPE(71565324, 30, 34838),
    CANIFIS(71565338, 18523, 194327),
    CATHERBY(71565325, 31, 119575),
    CITY_OF_UM(71565347, 53270, 386308),
    DRAYNOR_VILLAGE(71565326, 32, 193546),
    EDGEVILLE(71565327, 33, 193793),
    EAGLES_PEAK(71565339, 18524, 194328),
    FALADOR(71565328, 34, 194066),
    FORT_FORINTHRY(71565334, 52518, 318742),
    FREMENNIK_PROVINCE(71565340, 18525, 194329),
    KARAMJA(71565341, 18526, 194330),
    LUMBRIDGE(71565329, 35, 194070),
    LUNAR_ISLE(71565321, 10236, 194325),
    MENAPHOS(71565335, 36173, 194563),
    OOGLOG(71565342, 18527, 194331),
    PORT_SARIM(71565330, 36, 194314),
    PRIFDDINAS(71565346, 24967, 194561),
    SEERS_VILLAGE(71565331, 37, 194315),
    TAVERLY(71565332, 38, 194316),
    TIRANNWN(71565343, 18528, 194332),
    VARROCK(71565333, 39, 194318),
    WENDELWICK(71565353, 60739, 423949),
    WILDERNESS_CRATER(71565344, 18529, 194333),
    YANILLE(71565337, 40, 194324);

    private static final int HIDDEN_LODESTONE_TELEPORTS_VARBIT = 50990;
    private static final int TELEPORT_LIST_COMPONENT = 1461 << 16 | 1;
    private static final int TELEPORT_INDEX_PARAM = 2793;

    private final int interactId;
    private final int varbitId;
    private final int structId;
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    Lodestone(int interactId, int varbitId, int structId) {
        this.interactId = interactId;
        this.varbitId = varbitId;
        this.structId = structId;
    }

    //TODO: Update to no longer use MiniMenu.doAction
    /**
     * Teleports to this lodestone.
     *
     * <p>Prefers the spellbook teleport list, whose index is read from the lodestone's struct
     * ({@value #TELEPORT_INDEX_PARAM}) at runtime and therefore stays correct across game updates.
     * The lodestone map (interface 1092) is only used when varbit
     * {@value #HIDDEN_LODESTONE_TELEPORTS_VARBIT} says lodestone teleports are hidden from the
     * spellbook, because the map's component ids are hardcoded and have to be re-captured whenever
     * that interface is reworked.
     */
    public boolean teleport() {
        var player = Client.getLocalPlayer();
        if (player == null) {
            return false;
        }
        if (VarManager.getVarbitValue(HIDDEN_LODESTONE_TELEPORTS_VARBIT) != 1 && teleportFromSpellbook()) {
            awaitTeleport();
            return true;
        }
        boolean validate = !LodestoneNetwork.isOpen();
        log.atInfo().log("[Lodestone] LodestoneNetworkIsNotOpen: " + validate);
        if (validate) {
            LodestoneNetwork.open();
            Execution.delayUntil(3000, LodestoneNetwork::isOpen);
            if (!LodestoneNetwork.isOpen()) {
                log.atWarning().log("[Lodestone] lodestone network did not open for " + name());
                return false;
            }
        }
        if (MiniMenu.interact(ComponentAction.COMPONENT.getType(), 1, -1, interactId)) {
            awaitTeleport();
            return true;
        }
        return false;
    }

    private boolean teleportFromSpellbook() {
        var struct = ConfigManager.getStructType(structId);
        if (struct == null) {
            log.atWarning().log("[Lodestone] no struct " + structId + " for " + name());
            return false;
        }
        var index = struct.getParams().get(TELEPORT_INDEX_PARAM);
        if (!(index instanceof Integer)) {
            log.atWarning().log("[Lodestone] struct " + structId + " has no param "
                    + TELEPORT_INDEX_PARAM + " for " + name());
            return false;
        }
        log.atInfo().log("[Lodestone] " + name() + " via spellbook index " + index);
        return MiniMenu.interact(ComponentAction.COMPONENT.getType(), 1, (Integer) index,
                TELEPORT_LIST_COMPONENT);
    }

    private void awaitTeleport() {
        int wax = VarManager.getVarbitValue(28623);
        int quick = VarManager.getVarbitValue(28622);
        if (quick == 1 && wax > 0) {
            Execution.delay(RandomGenerator.nextInt(4500, 6500));
        } else {
            Execution.delay(RandomGenerator.nextInt(12000, 14000));
        }
    }

    public boolean isAvailable() {
        var result = VarManager.getVarbitValue(varbitId);
        switch (this) {
            case LUNAR_ISLE -> {
                return result >= 100;
            }
            case BANDIT_CAMP -> {
                return result >= 15;
            }
        }
        return result == 1;
    }
}
