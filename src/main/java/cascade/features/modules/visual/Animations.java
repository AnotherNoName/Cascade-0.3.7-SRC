//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ItemStackHelper
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.event.EntityViewRenderEvent$CameraSetup
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.visual;

import cascade.event.events.PerspectiveEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.render.ColorUtil;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Animations
extends Module {
    Setting<Boolean> slideWalk = this.register(new Setting<Boolean>("SlideWalk", false));
    Setting<Boolean> selfOnly = this.register(new Setting<Object>("SelfOnly", Boolean.valueOf(true), v -> this.slideWalk.getValue()));
    Setting<Boolean> headRotTest = this.register(new Setting<Boolean>("HeadRotTest", false));
    Setting<Boolean> aspect = this.register(new Setting<Boolean>("Aspect", false));
    Setting<Float> aspectValue = this.register(new Setting<Object>("AspectValue", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(3.0f), v -> this.aspect.getValue()));
    Setting<Integer> fov = this.register(new Setting<Integer>("FOV", 139, 0, 180));
    public Setting<Boolean> shulkerPreview = this.register(new Setting<Boolean>("ShulkerPreview", true));
    static ResourceLocation SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
    static Animations INSTANCE;

    public Animations() {
        super("Animations", Module.Category.VISUAL, "AnimationZZzz(Shlyypih");
        INSTANCE = this;
    }

    public static Animations getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Animations();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (Animations.fullNullCheck()) {
            return;
        }
        if (this.slideWalk.getValue().booleanValue()) {
            Animations.mc.player.prevLimbSwingAmount = 0.0f;
            Animations.mc.player.limbSwingAmount = 0.0f;
            Animations.mc.player.limbSwing = 0.0f;
            if (!this.selfOnly.getValue().booleanValue()) {
                for (EntityPlayer p : Animations.mc.world.playerEntities) {
                    if (!p.isEntityAlive() || p.getName() == Animations.mc.player.getName()) continue;
                    p.prevLimbSwingAmount = 0.0f;
                    p.limbSwingAmount = 0.0f;
                    p.limbSwing = 0.0f;
                }
            }
        }
        Animations.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, (float)this.fov.getValue().intValue());
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup e) {
        if (this.headRotTest.getValue().booleanValue() && this.isEnabled() && e != null) {
            e.setPitch(0.0f);
            e.setRoll(0.0f);
            e.setYaw(0.0f);
        }
    }

    @SubscribeEvent
    public void onPerspectiveEvent(PerspectiveEvent e) {
        if (this.aspect.getValue().booleanValue() && this.isEnabled() && e != null) {
            e.setAspect(this.aspectValue.getValue().floatValue());
        }
    }

    public void renderShulkerToolTip(ItemStack stack, int x, int y, String name) {
        NBTTagCompound blockEntityTag;
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            mc.getTextureManager().bindTexture(SHULKER_GUI_TEXTURE);
            RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
            RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54, 500);
            RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
            GlStateManager.disableDepth();
            Color color = new Color(-1);
            this.renderer.drawStringWithShadow(name == null ? stack.getDisplayName() : name, x + 8, y + 6, ColorUtil.toRGBA(color));
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            NonNullList nonnulllist = NonNullList.func_191197_a((int)27, (Object)ItemStack.field_190927_a);
            ItemStackHelper.func_191283_b((NBTTagCompound)blockEntityTag, (NonNullList)nonnulllist);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                int iX = x + i % 9 * 18 + 8;
                int iY = y + i / 9 * 18 + 18;
                ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                Animations.mc.getRenderItem().zLevel = 501.0f;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(Animations.mc.fontRendererObj, itemStack, iX, iY, null);
                Animations.mc.getRenderItem().zLevel = 0.0f;
            }
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }
}

