//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.gui.BossInfoClient
 *  net.minecraft.client.gui.GuiBossOverlay
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityFallingBlock
 *  net.minecraft.network.play.server.SPacketMaps
 *  net.minecraft.world.BossInfo
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FogDensity
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Pre
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package cascade.features.modules.visual;

import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.core.Pair;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.world.BossInfo;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class NoRender
extends Module {
    public Setting<Boolean> noMaps = this.register(new Setting<Boolean>("Maps", true));
    Setting<Boolean> smartFPS = this.register(new Setting<Boolean>("SmartFPS", false));
    Setting<Boolean> noFallingBlocks = this.register(new Setting<Boolean>("NoFallingBlocks", false));
    public Setting<Boolean> noHurt = this.register(new Setting<Boolean>("Hurt", true));
    public Setting<Boolean> noOverlay = this.register(new Setting<Boolean>("Overlay", true));
    public Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPop", false));
    public Setting<Boolean> noArmor = this.register(new Setting<Boolean>("Armor", true));
    public Setting<Boolean> noAdvancements = this.register(new Setting<Boolean>("Advancement", true));
    public Setting<Boss> boss = this.register(new Setting<Boss>("BossBars", Boss.None));
    public Setting<Float> scale = this.register(new Setting<Object>("Scale", Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f), v -> this.boss.getValue() == Boss.Minimize || this.boss.getValue() != Boss.Stack));
    Set<Integer> ids = new HashSet<Integer>();
    private static NoRender INSTANCE;

    public NoRender() {
        super("NoRender", Module.Category.VISUAL, "stops certain things from rendering");
        INSTANCE = this;
    }

    public static NoRender getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoRender();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (NoRender.fullNullCheck()) {
            return;
        }
        if (this.noFallingBlocks.getValue().booleanValue()) {
            for (Entity e : NoRender.mc.world.loadedEntityList) {
                if (!(e instanceof EntityFallingBlock) || e.isDead) continue;
                e.setDead();
            }
        }
    }

    @SubscribeEvent
    public void onRenderPre(RenderGameOverlayEvent.Pre e) {
        if (e.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && this.boss.getValue() != Boss.None && this.isEnabled()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderPost(RenderGameOverlayEvent.Post e) {
        block7: {
            block8: {
                if (e.getType() != RenderGameOverlayEvent.ElementType.BOSSINFO || this.boss.getValue() == Boss.None || !this.isEnabled()) break block7;
                if (this.boss.getValue() != Boss.Minimize) break block8;
                Map map = NoRender.mc.ingameGUI.getBossOverlay().mapBossInfos;
                if (map == null) {
                    return;
                }
                ScaledResolution scaledresolution = new ScaledResolution(mc);
                int i = scaledresolution.getScaledWidth();
                int j = 12;
                for (Map.Entry entry : map.entrySet()) {
                    BossInfoClient info = (BossInfoClient)entry.getValue();
                    String text = info.getName().getFormattedText();
                    int k = (int)((float)i / this.scale.getValue().floatValue() / 2.0f - 91.0f);
                    GL11.glScaled((double)this.scale.getValue().floatValue(), (double)this.scale.getValue().floatValue(), (double)1.0);
                    if (!e.isCanceled()) {
                        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        mc.getTextureManager().bindTexture(GuiBossOverlay.GUI_BARS_TEXTURES);
                        NoRender.mc.ingameGUI.getBossOverlay().render(k, j, (BossInfo)info);
                        NoRender.mc.fontRendererObj.drawStringWithShadow(text, (float)i / this.scale.getValue().floatValue() / 2.0f - (float)(NoRender.mc.fontRendererObj.getStringWidth(text) / 2), (float)(j - 9), 0xFFFFFF);
                    }
                    GL11.glScaled((double)(1.0 / (double)this.scale.getValue().floatValue()), (double)(1.0 / (double)this.scale.getValue().floatValue()), (double)1.0);
                    j += 10 + NoRender.mc.fontRendererObj.FONT_HEIGHT;
                }
                break block7;
            }
            if (this.boss.getValue() != Boss.Stack) break block7;
            Map map = NoRender.mc.ingameGUI.getBossOverlay().mapBossInfos;
            HashMap to = new HashMap();
            for (Map.Entry entry2 : map.entrySet()) {
                Pair p;
                String s = ((BossInfoClient)entry2.getValue()).getName().getFormattedText();
                if (to.containsKey(s)) {
                    p = (Pair)to.get(s);
                    p = new Pair(p.getKey(), p.getValue() + 1);
                    to.put(s, p);
                    continue;
                }
                p = new Pair(entry2.getValue(), 1);
                to.put(s, p);
            }
            ScaledResolution scaledresolution2 = new ScaledResolution(mc);
            int l = scaledresolution2.getScaledWidth();
            int m = 12;
            for (Map.Entry entry3 : to.entrySet()) {
                String text = (String)entry3.getKey();
                BossInfoClient info2 = (BossInfoClient)((Pair)entry3.getValue()).getKey();
                int a = (Integer)((Pair)entry3.getValue()).getValue();
                text = text + " x" + a;
                int k2 = (int)((float)l / this.scale.getValue().floatValue() / 2.0f - 91.0f);
                GL11.glScaled((double)this.scale.getValue().floatValue(), (double)this.scale.getValue().floatValue(), (double)1.0);
                if (!e.isCanceled()) {
                    GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    mc.getTextureManager().bindTexture(GuiBossOverlay.GUI_BARS_TEXTURES);
                    NoRender.mc.ingameGUI.getBossOverlay().render(k2, m, (BossInfo)info2);
                    NoRender.mc.fontRendererObj.drawStringWithShadow(text, (float)l / this.scale.getValue().floatValue() / 2.0f - (float)(NoRender.mc.fontRendererObj.getStringWidth(text) / 2), (float)(m - 9), 0xFFFFFF);
                }
                GL11.glScaled((double)(1.0 / (double)this.scale.getValue().floatValue()), (double)(1.0 / (double)this.scale.getValue().floatValue()), (double)1.0);
                m += 10 + NoRender.mc.fontRendererObj.FONT_HEIGHT;
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onPacketReceive(PacketEvent.Receive e) {
        if (e.getPacket() instanceof SPacketMaps && this.noMaps.getValue().booleanValue() && this.isEnabled()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent e) {
        if (!NoRender.fullNullCheck() && this.isEnabled() && (e.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER || e.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK) && this.noOverlay.getValue().booleanValue()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void something(EntityViewRenderEvent.FogDensity e) {
        if (this.noOverlay.getValue().booleanValue() && this.isEnabled() && (e.getState().getMaterial().equals(Material.WATER) || e.getState().getMaterial().equals(Material.LAVA))) {
            e.setDensity(0.0f);
            e.setCanceled(true);
        }
    }

    public static enum Boss {
        None,
        Remove,
        Stack,
        Minimize;

    }
}

