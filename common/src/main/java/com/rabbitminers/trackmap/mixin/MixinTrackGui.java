package com.rabbitminers.trackmap.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.rabbitminers.trackmap.helpers.Colour;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.trains.TrackGraph;
import com.simibubi.create.content.logistics.trains.TrackNode;
import com.simibubi.create.content.logistics.trains.entity.Train;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import xaero.map.gui.GuiMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Mixin(GuiMap.class)
public abstract class MixinTrackGui {
    @Shadow public abstract void drawDotOnMap(PoseStack matrixStack, VertexConsumer guiLinearBuffer, double x, double z, float angle, double sc);

    @Shadow private double cameraX;
    @Shadow private double cameraZ;
    @Shadow private double scale;

    @Shadow public abstract void drawObjectOnMap(PoseStack matrixStack, VertexConsumer guiLinearBuffer, double x, double z, float angle, double sc, float offX, float offY, int textureX, int textureY, int w, int h, int filter);

    @Shadow protected abstract void setColourBuffer(float r, float g, float b, float a);

    private PoseStack matrixStack;

    private Collection<Train> allTrains() {
        return Create.RAILWAYS.trains.values();
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public PoseStack captureMatrixStack(PoseStack value) {
        System.out.println("Captured matrix stack");
        this.matrixStack = value;
        return value;
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
                    ordinal = 2
            )
    )
    public VertexConsumer renderTrains(MultiBufferSource.BufferSource renderTypeBuffers, RenderType type) {
        VertexConsumer regularUIObjectConsumer = renderTypeBuffers.getBuffer(type);
        Collection<Train> trains = this.allTrains();
        this.drawTrainOnMap(regularUIObjectConsumer, trains);
        return regularUIObjectConsumer;
    }

    private void drawTrainOnMap(VertexConsumer buffers, Collection<Train> trains) {
        trains.forEach(train -> drawTrainOnMap(buffers, train));
    }

    private void drawTrainOnMap(VertexConsumer buffers, Train train) {
        train.carriages.forEach(carriage -> {
            Vec3 position = carriage.getLeadingPoint().getPosition();
            this.drawDotOnMap(
                matrixStack, buffers, position.x - this.cameraX, position.z - this.cameraZ, 0.0F, 1.0 / this.scale
            );
        });

        this.renderTrack(buffers);
    }

    private void renderTrack(VertexConsumer buffers) {
        Collection<TrackGraph> trackGraphs = Create.RAILWAYS.trackNetworks.values();
        trackGraphs.forEach(graph -> this.renderGraph(buffers, graph));
    }

    private void renderGraph(VertexConsumer buffers, TrackGraph graph) {
        graph.getNodes().stream().map(graph::locateNode).forEach(node -> renderNode(buffers, new Colour(graph.color), node));
    }

    private void renderNode(VertexConsumer buffers, Colour colour, TrackNode node) {
        Vec3 location = node.getLocation().getLocation();
        Matrix4f matrix = matrixStack.last().pose();
        this.setColourBuffer(colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, colour.getAlpha() / 255f);
        this.renderSolidRect(
            matrix, buffers, (float) (location.x - this.cameraX), (float) (location.z - this.cameraZ), 30, 30, 0f, 10.0 / this.scale
        );
    }

    private void renderSolidRect(
        Matrix4f matrix,
        VertexConsumer vertexBuffer,
        float x,
        float y,
        float width,
        float height,
        float angle,
        double sc
    ) {
        this.drawObjectOnMap(matrixStack, vertexBuffer, x, y, angle, sc, 2.5F, 2.5F, 0, 69, 5, 5, 9729);
    }
}
