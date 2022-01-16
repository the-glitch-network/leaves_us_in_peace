package net.sssubtlety.leaves_us_in_peace;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

import java.util.Optional;
import java.util.Random;

public final class FeatureControl {
    private static final Config CONFIG_INSTANCE;

    static {
        boolean shouldLoadConfig = false;

        final Optional<ModContainer> optModContainer = FabricLoader.getInstance().getModContainer("cloth-config");
        if (optModContainer.isPresent()) {
            try {
                shouldLoadConfig = VersionPredicate.parse(">=6.1.48").test(optModContainer.get().getMetadata().getVersion());
            } catch (VersionParsingException e) {
                e.printStackTrace();
            }
        }
        
        CONFIG_INSTANCE = shouldLoadConfig ?
                AutoConfig.register(Config.class, GsonConfigSerializer::new).getConfig() : null;

    }
    
    public static class Defaults {
        public static final boolean matchLeavesTypes = true;
        public static final boolean matchLogsToLeaves = true;
        public static final boolean ignorePersistentLeaves = true;
        public static final boolean accelerateLeavesDecay = true;
        public static final int minDecayDelay = 10;
        public static final int maxDecayDelay = 60;
        public static final boolean updateDiagonalLeaves = true;
        public static final boolean doDecayingLeavesEffects = false;
        public static final boolean fetchTranslationUpdates = true;

        private Defaults() { }
    }

    public static boolean isConfigLoaded() {
        return CONFIG_INSTANCE != null;
    }

    public static boolean shouldMatchLeavesTypes() {
        return CONFIG_INSTANCE == null ? Defaults.matchLeavesTypes : CONFIG_INSTANCE.matchLeavesTypes;
    }

    public static boolean shouldMatchLogsToLeaves() {
        return CONFIG_INSTANCE == null ? Defaults.matchLogsToLeaves : CONFIG_INSTANCE.matchLogsToLeaves;
    }

    public static boolean shouldIgnorePersistentLeaves() {
        return CONFIG_INSTANCE == null ? Defaults.ignorePersistentLeaves : CONFIG_INSTANCE.ignorePersistentLeaves;
    }

    public static boolean shouldAccelerateLeavesDecay() {
        return CONFIG_INSTANCE == null ? Defaults.accelerateLeavesDecay : CONFIG_INSTANCE.accelerateLeavesDecay;
    }

    public static int getDecayDelay(Random random) {
        final int minDecayDelay;
        final int maxDecayDelay;
        if (CONFIG_INSTANCE == null) {
            minDecayDelay = Defaults.minDecayDelay;
            maxDecayDelay = Defaults.maxDecayDelay;
        } else {
            minDecayDelay = CONFIG_INSTANCE.decayDelay.minimum;
            maxDecayDelay = CONFIG_INSTANCE.decayDelay.maximum;
        }

        return minDecayDelay < maxDecayDelay ?
             random.nextInt(minDecayDelay, maxDecayDelay + 1) : maxDecayDelay;
    }

    public static boolean shouldUpdateDiagonalLeaves() {
        return CONFIG_INSTANCE == null ? Defaults.updateDiagonalLeaves : CONFIG_INSTANCE.updateDiagonalLeaves;
    }

    public static boolean shouldDoDecayingLeavesEffects() {
        return CONFIG_INSTANCE == null ? Defaults.doDecayingLeavesEffects : CONFIG_INSTANCE.doDecayingLeavesEffects;
    }

    public static boolean shouldFetchTranslationUpdates() {
        return CONFIG_INSTANCE == null ? Defaults.fetchTranslationUpdates : CONFIG_INSTANCE.fetchTranslationUpdates;
    }
    
    public static void init() { }

    private FeatureControl() { }
}
