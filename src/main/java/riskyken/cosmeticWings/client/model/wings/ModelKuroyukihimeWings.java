package riskyken.cosmeticWings.client.model.wings;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import riskyken.cosmeticWings.client.particles.EntityButterflyFx;
import riskyken.cosmeticWings.client.particles.ParticleManager;
import riskyken.cosmeticWings.common.lib.LibModInfo;
import riskyken.cosmeticWings.common.wings.WingData;
import riskyken.cosmeticWings.utils.PointD;
import riskyken.cosmeticWings.utils.Trig;

public class ModelKuroyukihimeWings extends ModelWingBase {
    
    ModelRenderer rightWing;
    ModelRenderer leftWing;
    private final ResourceLocation wingsImage;

    public ModelKuroyukihimeWings() {
        textureWidth = 256;
        textureHeight = 64;
        
        leftWing = new ModelRenderer(this, 0, 0);
        leftWing.addBox(-62.5F, 0F, -0.5F, 125, 59, 1);
        leftWing.setRotationPoint(0F, 0F, 0F);
        leftWing.setTextureSize(256, 64);
        
        rightWing = new ModelRenderer(this, 0, 0);
        rightWing.addBox(-62.5F, 0F, -0.5F, 125, 59, 1);
        rightWing.setRotationPoint(0F, 0F, 0F);
        rightWing.setTextureSize(256, 64);
        rightWing.mirror = true;

        wingsImage = new ResourceLocation(LibModInfo.ID.toLowerCase(), "textures/wings/kuroyukihime-wings.png");
    }

    public void render(EntityPlayer player, RenderPlayer renderer, WingData wingData) {
        GL11.glColor3f(1F, 1F, 1F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(wingsImage);
        RenderWing(player, player.capabilities.isFlying & player.isAirBorne, wingData);
    }
    
    private void RenderWing(EntityPlayer player, boolean isFlying, WingData wingData) {
        float angle = getWingAngle(isFlying, 40, 8000, 250, player.getEntityId());
        
        setRotation(leftWing, (float) Math.toRadians(angle + 20), (float) Math.toRadians(0), (float) Math.toRadians(0));
        setRotation(rightWing, (float) Math.toRadians(-angle - 20), (float) Math.toRadians(0), (float) Math.toRadians(0));

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 4 * SCALE, 1.9F * SCALE);
        GL11.glRotatef(90, 0, 1, 0);
        GL11.glRotatef(90, 0, 0, 1);

        GL11.glColor3f(1F, 1F, 1F);
        
        float scale = 0.32F;
        GL11.glTranslatef(0F, SCALE * scale * (wingData.wingScale - 1), 0F);
        
        GL11.glScalef(scale, scale, scale);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, wingData.centreOffset * 3 * SCALE);
        GL11.glScalef(wingData.wingScale, wingData.wingScale, wingData.wingScale);
        leftWing.render(SCALE);
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, -wingData.centreOffset * 3 * SCALE);
        GL11.glScalef(wingData.wingScale, wingData.wingScale, wingData.wingScale);
        rightWing.render(SCALE);
        GL11.glPopMatrix();
        
        GL11.glPopMatrix();
    }
    
    public void onTick(EntityPlayer player, float wingScale) {
        spawnParticales(player, wingScale);
    }

    private void spawnParticales(EntityPlayer player, float wingScale) {
        float scale = (1F - wingScale) * 0.2F;
        Random rnd = new Random();
        if (rnd.nextFloat() * 1000 > 980) {
            PointD offset;

            if (rnd.nextFloat() >= 0.5f) {
                offset = Trig.moveTo(new PointD(player.posX, player.posZ), 0.3f + rnd.nextFloat() * 1.4f * wingScale, player.renderYawOffset + 60);
            } else {
                offset = Trig.moveTo(new PointD(player.posX, player.posZ), 0.3f + rnd.nextFloat() * 1.4f * wingScale, player.renderYawOffset + 127);
            }

            float yOffset = 0f;

            double parX = offset.x;
            double parY = player.posY - 0.4D + scale + rnd.nextFloat() * 0.5F;
            double parZ = offset.y;

            EntityClientPlayerMP localPlayer = Minecraft.getMinecraft().thePlayer;

            if (!localPlayer.getDisplayName().equals(player.getDisplayName())) {
                parY += 1.6D;
            }

            EntityButterflyFx particle = new EntityButterflyFx(player.worldObj, parX, parY, parZ, wingScale, player);
            ParticleManager.INSTANCE.spawnParticle(player.worldObj, particle);
        }
    }
}
