package dev.egg.vistaquality.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.vista.client.renderer.TvBlockEntityRenderer;
import net.mehvahdjukaar.vista.common.tv.TVBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static org.joml.Math.lerp;

@Mixin(TvBlockEntityRenderer.class)
public class TvBlockEntityRendererMixin {

    @ModifyArg(method = "render(Lnet/mehvahdjukaar/vista/common/tv/TVBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lnet/mehvahdjukaar/vista/client/video_source/IVideoSource;getVideoFrameBuilder(FLnet/minecraft/client/renderer/MultiBufferSource;ZIIIZLnet/mehvahdjukaar/vista/common/tv/IntAnimationState;Lnet/mehvahdjukaar/vista/common/tv/IntAnimationState;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), index = 3)
    private int vistaquality$modifyResolution(int originalResolution, @Local(argsOnly = true) TVBlockEntity blockEntity) {

        Level level = Minecraft.getInstance().level;
        if (level == null) return originalResolution;

        int power = level.getBestNeighborSignal(((BlockEntity)blockEntity).getBlockPos());

        if (power > 7) return originalResolution;

        return (int) lerp(originalResolution, originalResolution / 4f, (power-1)/14f);
    }
    @ModifyArg(method = "render(Lnet/mehvahdjukaar/vista/common/tv/TVBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lnet/mehvahdjukaar/vista/client/video_source/IVideoSource;getVideoFrameBuilder(FLnet/minecraft/client/renderer/MultiBufferSource;ZIIIZLnet/mehvahdjukaar/vista/common/tv/IntAnimationState;Lnet/mehvahdjukaar/vista/common/tv/IntAnimationState;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), index = 4)
    private int vistaquality$modifyEffectResolution(int originalResolution, @Local(argsOnly = true) TVBlockEntity blockEntity) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return originalResolution;

        int power = level.getBestNeighborSignal(((BlockEntity)blockEntity).getBlockPos());

        if (power <= 7) return 1;

        float t = (power - 9) / 6f;
        t = Mth.clamp(t, 0f, 1f);

        return (int) lerp(1, originalResolution, t);
    }
}