package net.cavoj.servertick.mixin.client;

import net.cavoj.servertick.ServerTickClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.MetricsData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
    @Shadow protected abstract void drawMetricsData(DrawContext context, MetricsData metricsData, int x, int width, boolean showFps);

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(DrawContext context, CallbackInfo ci) {
        if (this.client.options.debugTpsEnabled && this.client.getServer() == null) {
            MetricsData metrics = ServerTickClient.getInstance().getMetricsData();
            if (metrics == null) return;

            context.draw(() -> {
                int i = this.client.getWindow().getScaledWidth();
                this.drawMetricsData(context, metrics, i - Math.min(i / 2, 240), i / 2, false);
            });
        }
    }
}
