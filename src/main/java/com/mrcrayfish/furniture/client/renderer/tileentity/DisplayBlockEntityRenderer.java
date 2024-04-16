package com.mrcrayfish.furniture.client.renderer.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mrcrayfish.furniture.block.FurnitureHorizontalBlock;
import com.mrcrayfish.furniture.tileentity.DisplayBlockEntity;
import me.srrapero720.watermedia.api.player.SyncVideoPlayer;
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
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class DisplayBlockEntityRenderer implements BlockEntityRenderer<DisplayBlockEntity>
{
    private static final ResourceLocation NOISE = new ResourceLocation("cfm:textures/noise.png");
    private static final Random RAND = new Random();
    private final Font font;

    public DisplayBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.font = context.getFont();
    }

    @Override
    public boolean shouldRender(DisplayBlockEntity entity, Vec3 playerPos)
    {
        return entity.isPowered() && BlockEntityRenderer.super.shouldRender(entity, playerPos);
    }

    @Override
    public void render(DisplayBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource source, int light, int overlay)
    {
        Level level = tileEntity.getLevel();
        if(level == null)
            return;

        BlockState state = tileEntity.getBlockState();
        if(!state.hasProperty(FurnitureHorizontalBlock.DIRECTION))
            return;

        poseStack.pushPose(); //Push

        float screenWidth = tileEntity.getScreenWidth();
        float screenHeight = tileEntity.getScreenHeight();
        double screenYOffset = tileEntity.getScreenYOffset();
        double screenZOffset = tileEntity.getScreenZOffset();

        //Setup translations
        poseStack.translate(8 * 0.0625, screenYOffset * 0.0625, 8 * 0.0625);
        Direction direction = state.getValue(FurnitureHorizontalBlock.DIRECTION);
        poseStack.mulPose(direction.getRotation().rotateX(-(float)Math.PI / 2F));
        poseStack.translate(-screenWidth / 2 * 0.0625, 0, 0);
        poseStack.translate(0, 0, screenZOffset * 0.0625);

        try
        {
            SyncVideoPlayer player = tileEntity.loadVideo();

            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();

            float startX = 0.0F;
            float startY = 0.0F;

            if(player.isReady() && !player.isPlaying())
            {
                RenderSystem.setShaderTexture(0, NOISE);

                float pixelScale = 1.0F / 256F;
                float scaledWidth = screenWidth * 4F;
                float scaledHeight = screenHeight * 4F;
                float u = ((int)((256 - scaledWidth) * RAND.nextFloat()) * pixelScale);
                float v = ((int)((256 - scaledHeight) * RAND.nextFloat()) * pixelScale);

                startX *= 0.0625;
                startY *= 0.0625;
                screenWidth *= 0.0625;
                screenHeight *= 0.0625;

                poseStack.translate(0, 0, -0.01 * 0.0625);
                Matrix4f matrix = poseStack.last().pose();
                Matrix3f rot = new Matrix3f(matrix);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder buffer = tesselator.getBuilder();
                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
                buffer.defaultColor(255, 255, 255, 255);
                buffer.vertex(matrix, startX, startY, 0).uv(u, v)
                        .normal(rot, 1, 0, 0).endVertex();
                buffer.vertex(matrix, startX, startY + screenHeight, 0).uv(u, v + scaledHeight * pixelScale)
                        .normal(rot, 1, 0, 0).endVertex();
                buffer.vertex(matrix, startX + screenWidth, startY + screenHeight, 0).uv(u + scaledWidth * pixelScale, v + scaledHeight * pixelScale)
                        .normal(rot, 1, 0, 0).endVertex();
                buffer.vertex(matrix, startX + screenWidth, startY, 0).uv(u + scaledWidth * pixelScale, v)
                        .normal(rot, 1, 0, 0).endVertex();
                buffer.unsetDefaultColor();
                tesselator.end();
            }
            else if(player.isPlaying())
            {
                int texture = player.getGlTexture();
                if(texture != -1)
                {
                    RenderSystem.bindTexture(texture);
                    RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                    RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                    RenderSystem.setShaderTexture(0, texture);

                    float imageWidth = screenWidth;
                    float imageHeight = screenHeight;

                    if(!tileEntity.isStretched())
                    {
                        //Calculates the positioning and scale so the GIF keeps its ratio and renders within the screen
                        float scaleWidth = screenWidth / (float) player.getWidth();
                        float scaleHeight = screenHeight / (float) player.getHeight();
                        float scale = Math.min(scaleWidth, scaleHeight);
                        imageWidth = player.getWidth() * scale;
                        imageHeight = player.getHeight() * scale;
                        startX = (screenWidth - imageWidth) / 2.0F;
                        startY = (screenHeight - imageHeight) / 2.0F;
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
                    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
                    buffer.defaultColor(255, 255, 255, 255);
                    buffer.vertex(matrix, startX, startY, 0).uv(1, 1)
                            .normal(rot, 1, 0, 0).endVertex();
                    buffer.vertex(matrix, startX, startY + imageHeight, 0).uv(1, 0)
                            .normal(rot, 1, 0, 0).endVertex();
                    buffer.vertex(matrix, startX + imageWidth, startY + imageHeight, 0).uv(0, 0)
                            .normal(rot, 1, 0, 0).endVertex();
                    buffer.vertex(matrix, startX + imageWidth, startY, 0).uv(0, 1)
                            .normal(rot, 1, 0, 0).endVertex();
                    buffer.unsetDefaultColor();
                    tesselator.end();
                }
            }
        }
        catch(DisplayBlockEntity.VideoDownloadException e)
        {
            poseStack.translate(screenWidth * 0.0625 - 0.0625, screenHeight * 0.0625 - 0.0625, 0);
            poseStack.scale(1, -1, -1);
            poseStack.scale(0.01F, 0.01F, 0.01F);
            poseStack.mulPose(new Quaternionf(0, 1, 0, 0));

            List<FormattedCharSequence> lines = this.font.split(Component.translatable(e.getMessage()), (int) ((screenWidth - 2.0) * 6.3));
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
