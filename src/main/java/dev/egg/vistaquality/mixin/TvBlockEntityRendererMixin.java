package dev.egg.vistaquality.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.moonlight.api.util.math.Vec2i;
import net.mehvahdjukaar.vista.client.renderer.TvBlockEntityRenderer;
import net.mehvahdjukaar.vista.common.tv.TVBlock;
import net.mehvahdjukaar.vista.common.tv.TVBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static org.joml.Math.lerp;

@Mixin(TvBlockEntityRenderer.class)
public class TvBlockEntityRendererMixin {
    @ModifyArg(method = "render(Lnet/mehvahdjukaar/vista/common/tv/TVBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lnet/mehvahdjukaar/vista/client/video_source/IVideoSource;getVideoFrameBuilder(FLnet/minecraft/client/renderer/MultiBufferSource;ZLnet/mehvahdjukaar/moonlight/api/util/math/Vec2i;Lnet/mehvahdjukaar/moonlight/api/util/math/Vec2i;IZLnet/mehvahdjukaar/vista/common/tv/IntAnimationState;Lnet/mehvahdjukaar/vista/common/tv/IntAnimationState;Z)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), index = 4)
    private Vec2i vistaquality$modifyEffectResolution(Vec2i originalResolution, @Local(argsOnly = true) TVBlockEntity blockEntity) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return originalResolution;

        if (!level.getBlockState(blockEntity.getBlockPos()).getValue(TVBlock.POWER_STATE).isStrong())
            return originalResolution;

        int power = level.getBestNeighborSignal(blockEntity.getBlockPos());

        if (power == 0) return new Vec2i(1, 1);
        int x = (int) lerp(1, originalResolution.x(), (power-1)/14f);
        int y = (int) lerp(1, originalResolution.y(), (power-1)/14f);
        return new Vec2i(x, y);
    }
}