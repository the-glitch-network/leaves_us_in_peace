package tfar.leafmealone.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

import static tfar.leafmealone.MixinHooks.onReload;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void postReloadResources(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        onReload();
    }
}
