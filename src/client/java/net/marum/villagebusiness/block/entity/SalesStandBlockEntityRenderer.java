package net.marum.villagebusiness.block.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;

public class SalesStandBlockEntityRenderer implements BlockEntityRenderer<SalesStandBlockEntity> {

    private final ItemRenderer itemRenderer;
    private final Quaternionf rotationMatrix = new Quaternionf(0.707f, 0f, 0f, 0.707f);
    private final Quaternionf slightRotationMatrix = new Quaternionf(0f, 0f, 0.0499792f, 0.9987503f);

    public SalesStandBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SalesStandBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        World world = entity.getWorld();

        if (entity.getInputCount() > 0) {
            ItemStack product = entity.getStack(3);
            // Product
            int productCount = 1;

            matrices.push();
            if (product.getItem() instanceof BlockItem) {
                productCount = 1;
                matrices.translate(0.5f, 1.2f, 0.5f);
                matrices.scale(0.75f, 0.75f, 0.75f);
            } else {
                productCount = Math.min(1 + entity.getInputCount() / 8, 5);
                matrices.translate(0.5f, 1.04f, 0.5f);
                matrices.scale(0.75f, 0.75f, 0.75f);
                matrices.multiply(rotationMatrix);
            }

            for (int i = 0; i < productCount; i++) {
                itemRenderer.renderItem(
                        product,
                        ItemDisplayContext.FIXED,
                        lightAbove,
                        overlay,
                        matrices,
                        vertexConsumers,
                        world,
                        0
                );
                matrices.translate(0f, 0f, -0.07f);
                matrices.multiply(slightRotationMatrix);
            }
            matrices.pop();
        }

        // Emeralds
        if (entity.getOutputBlockCount() > 0) {
            ItemStack emeraldStack = entity.getStack(0);
            matrices.push();
            matrices.translate(0.18f, 1.13f, 0.18f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(rotationMatrix);
            int emeraldPile = entity.getOutputBlockCount();
            emeraldPile = Math.min(emeraldPile, 3);
            for (int i = 0; i < emeraldPile; i++) {
                itemRenderer.renderItem(
                        emeraldStack,
                        ItemDisplayContext.FIXED,
                        lightAbove,
                        overlay,
                        matrices,
                        vertexConsumers,
                        world,
                        0
                );
                matrices.translate(0f, 0f, -0.5f);
                matrices.multiply(slightRotationMatrix);
            }
            matrices.pop();
        } else if (entity.getOutputEmeraldCount() > 0) {
            ItemStack emeraldStack = entity.getStack(1);
            matrices.push();
            matrices.translate(0.15f, 1.03f, 0.15f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(rotationMatrix);
            int emeraldPile = entity.getOutputEmeraldCount();
            emeraldPile = Math.min(emeraldPile, 5);
            for (int i = 0; i < emeraldPile; i++) {
                itemRenderer.renderItem(
                        emeraldStack,
                        ItemDisplayContext.FIXED,
                        lightAbove,
                        overlay,
                        matrices,
                        vertexConsumers,
                        world,
                        0
                );
                matrices.translate(0f, 0f, -0.07f);
                matrices.multiply(slightRotationMatrix);
            }
            matrices.pop();
        } else if (entity.getOutputNuggetCount() > 0) {
            ItemStack emeraldStack = entity.getStack(2);
            matrices.push();
            matrices.translate(0.12f, 1.03f, 0.12f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(rotationMatrix);
            int emeraldPile = entity.getOutputNuggetCount();
            emeraldPile = Math.min(emeraldPile, 5);
            for (int i = 0; i < emeraldPile; i++) {
                itemRenderer.renderItem(
                        emeraldStack,
                        ItemDisplayContext.FIXED,
                        lightAbove,
                        overlay,
                        matrices,
                        vertexConsumers,
                        world,
                        0
                );
                matrices.translate(0f, 0f, -0.07f);
                matrices.multiply(slightRotationMatrix);
            }
            matrices.pop();
        }
    }
}
