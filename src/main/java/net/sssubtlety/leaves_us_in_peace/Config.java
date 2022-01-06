package net.sssubtlety.leaves_us_in_peace;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import static net.sssubtlety.leaves_us_in_peace.LeavesUsInPeace.NAMESPACE;

@me.shedaniel.autoconfig.annotation.Config(name = NAMESPACE)
public class Config implements ConfigData {
    private static Config INSTANCE;

    static {
        AutoConfig.register(Config.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(Config.class).getConfig();
    }

    public static void init() { }

    public static boolean isMatchLeavesTypes() {
        return INSTANCE.matchLeavesTypes;
    }

    public static boolean isMatchLogsToLeaves() {
        return INSTANCE.matchLogsToLeaves;
    }

    public static boolean isIgnorePersistentLaves() {
        return INSTANCE.ignorePersistentLaves;
    }

    public static boolean isAccelerateLeavesDecay() {
        return INSTANCE.accelerateLeavesDecay;
    }

    public static int getMinDecayDelay() {
        return INSTANCE.minDecayDelay;
    }

    public static int getMaxDecayDelay() {
        return INSTANCE.maxDecayDelay;
    }

    public static boolean isUpdateDiagonalLeaves() {
        return INSTANCE.updateDiagonalLeaves;
    }

    public static boolean isDoDecayingLeavesEffects() {
        return INSTANCE.doDecayingLeavesEffects;
    }

    public boolean matchLeavesTypes = true;
    public boolean matchLogsToLeaves = true;
    public boolean ignorePersistentLaves = true;
    public boolean accelerateLeavesDecay = true;
    public int minDecayDelay = 10;
    public int maxDecayDelay = 100;
    public boolean updateDiagonalLeaves = true;
    public boolean doDecayingLeavesEffects = false;



//    @ConfigEntry.Gui.Tooltip()
//    boolean simpleMode = true;
//
//    @ConfigEntry.Gui.Tooltip()
//    boolean quasiConnectivity = false;
//
//    @ConfigEntry.Gui.Tooltip()
//    boolean redirectsRedstone = false;
//
//    @ConfigEntry.Gui.Tooltip()
//    boolean craftContinuously = false;
//
//    @ConfigEntry.Gui.Tooltip()
//    boolean comparatorReadsOutput = false;
//
//    public static boolean isSimpleMode() {
//        return INSTANCE.simpleMode;
//    }
//
//    public static boolean isQuasiConnected() {
//        return INSTANCE.quasiConnectivity;
//    }
//
//    public static boolean doesCraftContinuously() {
//        return INSTANCE.craftContinuously;
//    }
//
//    public static boolean doesRedirectRedstone() {
//        return INSTANCE.redirectsRedstone;
//    }
//
//    public static boolean doesComparatorReadOutput() {
//        return INSTANCE.comparatorReadsOutput;
//    }
}
