package net.sssubtlety.leaves_us_in_peace;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static net.sssubtlety.leaves_us_in_peace.LeavesUsInPeace.NAMESPACE;

@me.shedaniel.autoconfig.annotation.Config(name = NAMESPACE)
public class Config implements ConfigData {
//    private static final Config INSTANCE;
//
//    static {
//        INSTANCE = AutoConfig.register(Config.class, GsonConfigSerializer::new).getConfig();
//    }

//    public static void init() { }
//
//    public static boolean shouldMatchLeavesTypes() {
//        return INSTANCE.matchLeavesTypes;
//    }
//
//    public static boolean shouldMatchLogsToLeaves() {
//        return INSTANCE.matchLogsToLeaves;
//    }
//
//    public static boolean shouldIgnorePersistentLeaves() {
//        return INSTANCE.ignorePersistentLeaves;
//    }
//
//    public static boolean shouldAccelerateLeavesDecay() {
//        return INSTANCE.accelerateLeavesDecay;
//    }
//
//    public static int getMinDecayDelay() {
//        return INSTANCE.minDecayDelay;
//    }
//
//    public static int getMaxDecayDelay() {
//        return INSTANCE.maxDecayDelay;
//    }
//
//    public static boolean shouldUpdateDiagonalLeaves() {
//        return INSTANCE.updateDiagonalLeaves;
//    }
//
//    public static boolean shouldDoDecayingLeavesEffects() {
//        return INSTANCE.doDecayingLeavesEffects;
//    }
//
//    public static boolean shouldFetchTranslationUpdates() {
//        return INSTANCE.fetchTranslationUpdates;
//    }

    @ConfigEntry.Gui.Tooltip
    public boolean matchLeavesTypes = FeatureControl.Defaults.matchLeavesTypes;

    @ConfigEntry.Gui.Tooltip
    public boolean matchLogsToLeaves = FeatureControl.Defaults.matchLogsToLeaves;

    @ConfigEntry.Gui.Tooltip
    public boolean ignorePersistentLeaves = FeatureControl.Defaults.ignorePersistentLeaves;

    @ConfigEntry.Gui.Tooltip
    public boolean accelerateLeavesDecay = FeatureControl.Defaults.accelerateLeavesDecay;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    public DecayDelay decayDelay = new DecayDelay();

    @ConfigEntry.Gui.Tooltip
    public boolean updateDiagonalLeaves = FeatureControl.Defaults.updateDiagonalLeaves;

    @ConfigEntry.Gui.Tooltip
    public boolean doDecayingLeavesEffects = FeatureControl.Defaults.doDecayingLeavesEffects;

    @ConfigEntry.Gui.Tooltip
    public boolean fetchTranslationUpdates = FeatureControl.Defaults.fetchTranslationUpdates;

    public static class DecayDelay {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(max = 100)
        public int minimum = FeatureControl.Defaults.minDecayDelay;

        @ConfigEntry.BoundedDiscrete(max = 100)
        @ConfigEntry.Gui.Tooltip
        public int maximum = FeatureControl.Defaults.maxDecayDelay;
    }
}
