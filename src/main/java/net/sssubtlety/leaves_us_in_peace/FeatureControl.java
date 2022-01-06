package net.sssubtlety.leaves_us_in_peace;

import java.util.Random;

public final class FeatureControl {
    public static class Defaults {
        public static final boolean matchLeavesTypes = true;
        public static final boolean matchLogsToLeaves = true;
        public static final boolean ignorePersistentLaves = true;
        public static final boolean accelerateLeavesDecay = true;
        public static final int minDecayDelay = 10;
        public static final int maxDecayDelay = 100;
        public static final boolean updateDiagonalLeaves = true;
        public static final boolean doDecayingLeavesEffects = false;

        private Defaults() { }
    }

    public static boolean shouldMatchLeavesTypes() {
        return Defaults.matchLeavesTypes;
    }

    public static boolean shouldMatchLogsToLeaves() {
        return Defaults.matchLogsToLeaves;
    }

    public static boolean shouldIgnorePersistentLaves() {
        return Defaults.ignorePersistentLaves;
    }

    public static boolean shouldAccelerateLeavesDecay() {
        return Defaults.accelerateLeavesDecay;
    }

    public static int getDecayDelay(Random random) {
        return Defaults.minDecayDelay < Defaults.maxDecayDelay ?
             random.nextInt(Defaults.minDecayDelay, Defaults.maxDecayDelay) : Defaults.maxDecayDelay;
    }

    public static boolean shouldUpdateDiagonalLeaves() {
        return Defaults.updateDiagonalLeaves;
    }

    public static boolean shouldDoDecayingLeavesEffects() {
        return Defaults.doDecayingLeavesEffects;
    }

    private FeatureControl() { }
}
