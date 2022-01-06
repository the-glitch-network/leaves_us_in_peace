package net.sssubtlety.leaves_us_in_peace;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

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
            addDrawable(new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 200, 200, NO_CONFIG_SCREEN_MESSAGE));
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            renderBackground(matrices);
            super.render(matrices, mouseX, mouseY, delta);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void onClose() {
            this.client.setScreen(parent);
        }
    }
}
