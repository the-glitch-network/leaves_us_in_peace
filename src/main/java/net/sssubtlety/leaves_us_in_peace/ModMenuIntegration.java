package net.sssubtlety.leaves_us_in_peace;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import static net.sssubtlety.leaves_us_in_peace.FeatureControl.isConfigLoaded;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    private static final TranslatableText NO_CONFIG_SCREEN_TITLE = new TranslatableText("text.leaves_us_in_peace.no_config_screen.title");
    private static final TranslatableText NO_CONFIG_SCREEN_MESSAGE = new TranslatableText("text.leaves_us_in_peace.no_config_screen.message");

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return isConfigLoaded() ?
                parent -> AutoConfig.getConfigScreen(Config.class, parent).get() :
                NoConfigScreen::new;
    }

    public static class NoConfigScreen extends Screen {
        private final Screen parent;
        protected NoConfigScreen(Screen parent) {
            super(NO_CONFIG_SCREEN_TITLE);
            this.parent = parent;
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            renderBackground(matrices);
            super.render(matrices, mouseX, mouseY, delta);
            final int windowHCenter = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2;
            final int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
            DrawableHelper.drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, NO_CONFIG_SCREEN_TITLE, windowHCenter, windowHeight / 10, Formatting.WHITE.getColorValue());
            DrawableHelper.drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, NO_CONFIG_SCREEN_MESSAGE, windowHCenter, windowHeight / 2, Formatting.RED.getColorValue());
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void onClose() {
            this.client.setScreen(parent);
        }
    }
}
