/*
 * Decompiled with CFR 0.152.
 */
package cascade.features.modules.player;

import cascade.features.modules.Module;

public class LiquidInteract
extends Module {
    private static LiquidInteract INSTANCE;

    public LiquidInteract() {
        super("LiquidInteract", Module.Category.PLAYER, "");
        INSTANCE = this;
    }

    public static LiquidInteract getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LiquidInteract();
        }
        return INSTANCE;
    }
}

