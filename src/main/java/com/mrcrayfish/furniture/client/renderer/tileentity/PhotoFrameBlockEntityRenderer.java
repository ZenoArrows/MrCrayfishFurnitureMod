package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mrcrayfish.furniture.block.FurnitureHorizontalBlock;
import com.mrcrayfish.furniture.block.PhotoFrameBlock;
import com.mrcrayfish.furniture.tileentity.PhotoFrameBlockEntity;
import me.srrapero720.watermedia.api.image.ImageCache;
import me.srrapero720.watermedia.api.image.ImageRenderer;
import me.srrapero720.watermedia.api.math.MathAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class PhotoFrameBlockEntityRenderer implements BlockEntityRenderer<PhotoFrameBlockEntity>
{
    private static final ResourceLocation NOISE = new ResourceLocation("cfm:textures/noise.png");
    private static final Random RAND = new Random();
    private final Font font;

    public PhotoFrameBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.font = context.getFont();
    }

    @Override
    public boolean shouldRender(PhotoFrameBlockEntity entity, Vec3 playerPos)
    {
        if(entity.getPhoto().isEmpty() || !BlockEntityRenderer.super.shouldRender(entity, playerPos))
            return false;

        BlockState state = entity.getBlockState();
        if(state.getBlock() instanceof PhotoFrameBlock block)
        {
            // We should only render if this is the bottom-right corner of the frame
            if(block.isMasterFrame(state))
                return true;
        }
        return false;
    }

    @Override
    public void render(PhotoFrameBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource source, int light, int overlay)
    {
        Level level = tileEntity.getLevel();
        if(level == null)
            return;

        BlockState state = tileEntity.getBlockState();
        if(!state.hasProperty(FurnitureHorizontalBlock.DIRECTION))
            return;

        poseStack.pushPose(); //Push

        float frameWidth = tileEntity.getPhotoWidth();
        float frameHeight = tileEntity.getPhotoHeight();
        double frameYOffset = 1;
        double frameZOffset = 7.49;

        //Setup translations
        poseStack.translate(8 * 0.0625, frameYOffset * 0.0625, 8 * 0.0625);
        Direction direction = state.getValue(FurnitureHorizontalBlock.DIRECTION);
        poseStack.mulPose(direction.getRotation().rotateX(-(float)Math.PI / 2F).rotateY((float)Math.PI));
        poseStack.translate(-7 * 0.0625, 0, 0);
        poseStack.translate(0, 0, frameZOffset * 0.0625);

        try
        {
            ImageCache image = tileEntity.loadPhoto();

            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader(GameRenderer::getRendertypeSolidShader);
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();

            float startX = 0.0F;
            float startY = 0.0F;

            if(image.getStatus() == ImageCache.Status.LOADING)
            {
                RenderSystem.setShaderTexture(0, NOISE);

                float pixelScale = 1.0F / 256F;
                float scaledWidth = frameWidth * 4F;
                float scaledHeight = frameHeight * 4F;
                float u = ((int)((256 - scaledWidth) * RAND.nextFloat()) * pixelScale);
                float v = ((int)((256 - scaledHeight) * RAND.nextFloat()) * pixelScale);

                startX *= 0.0625;
                startY *= 0.0625;
                frameWidth *= 0.0625;
                frameHeight *= 0.0625;

                poseStack.translate(0, 0, -0.01 * 0.0625);
                Matrix4f matrix = poseStack.last().pose();
                Matrix3f rot = new Matrix3f(matrix);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder buffer = tesselator.getBuilder();
                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
                buffer.defaultColor(255, 255, 255, 255);
                buffer.vertex(matrix, startX, startY, 0).uv(u, v)
                        .uv2(light).normal(rot, 1, 0, 0).endVertex();
                buffer.vertex(matrix, startX, startY + frameHeight, 0).uv(u, v + scaledHeight * pixelScale)
                        .uv2(light).normal(rot, 1, 0, 0).endVertex();
                buffer.vertex(matrix, startX + frameWidth, startY + frameHeight, 0).uv(u + scaledWidth * pixelScale, v + scaledHeight * pixelScale)
                        .uv2(light).normal(rot, 1, 0, 0).endVertex();
                buffer.vertex(matrix, startX + frameWidth, startY, 0).uv(u + scaledWidth * pixelScale, v)
                        .uv2(light).normal(rot, 1, 0, 0).endVertex();
                buffer.unsetDefaultColor();
                tesselator.end();
            }
            else if(image.getStatus() == ImageCache.Status.READY)
            {
                int texture = -1;
                ImageRenderer renderer = image.getRenderer();
                if(renderer != null)
                    texture = renderer.texture((int)level.getGameTime(), MathAPI.tickToMs(partialTicks), true);
                if(texture != -1)
                {
                    RenderSystem.setShaderTexture(0, texture);

                    float imageWidth = frameWidth;
                    float imageHeight = frameHeight;

                    if(!tileEntity.isStretched())
                    {
                        //Calculates the positioning and scale so the GIF keeps its ratio and renders within the screen
                        float scaleWidth = frameWidth / (float) renderer.width;
                        float scaleHeight = frameHeight / (float) renderer.height;
                        float scale = Math.min(scaleWidth, scaleHeight);
                        imageWidth = renderer.width * scale;
                        imageHeight = renderer.height * scale;
                        startX = (frameWidth - imageWidth) / 2.0F;
                        startY = (frameHeight - imageHeight) / 2.0F;
                    }

                    startX *= 0.0625F;
                    startY *= 0.0625F;
                    imageWidth *= 0.0625F;
                    imageHeight *= 0.0625F;

                    //Render the Image
                    poseStack.translate(0, 0, -0.01 * 0.0625);
                    Matrix4f matrix = poseStack.last().pose();
                    Matrix3f rot = new Matrix3f(matrix);
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder buffer = tesselator.getBuilder();
                    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
                    buffer.defaultColor(255, 255, 255, 255);
                    buffer.vertex(matrix, startX, startY, 0).uv(1, 1)
                            .uv2(light).normal(rot, 1, 0, 0).endVertex();
                    buffer.vertex(matrix, startX, startY + imageHeight, 0).uv(1, 0)
                            .uv2(light).normal(rot, 1, 0, 0).endVertex();
                    buffer.vertex(matrix, startX + imageWidth, startY + imageHeight, 0).uv(0, 0)
                            .uv2(light).normal(rot, 1, 0, 0).endVertex();
                    buffer.vertex(matrix, startX + imageWidth, startY, 0).uv(0, 1)
                            .uv2(light).normal(rot, 1, 0, 0).endVertex();
                    buffer.unsetDefaultColor();
                    tesselator.end();
                }
            }
        }
        catch(PhotoFrameBlockEntity.ImageDownloadException e)
        {
            poseStack.translate(frameWidth * 0.0625 - 0.0625, frameHeight * 0.0625 - 0.0625, 0);
            poseStack.scale(1, -1, -1);
            poseStack.scale(0.01F, 0.01F, 0.01F);
            poseStack.mulPose(new Quaternionf(0, 1, 0, 0));

            List<FormattedCharSequence> lines = this.font.split(Component.translatable(e.getMessage()), (int) ((frameWidth - 2.0) * 6.3));
            for(int i = 0; i < lines.size(); i++)
                font.drawInBatch(lines.get(i), 0, font.lineHeight * i, 16777215, false, poseStack.last().pose(), source, Font.DisplayMode.NORMAL, overlay, light);
        }
        finally
        {
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
        }
        poseStack.popPose();
    }
}
