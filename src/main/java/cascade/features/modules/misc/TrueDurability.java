/*
 * Decompiled with CFR 0.152.
 */
package cascade.features.modules.misc;

import cascade.features.modules.Module;

public class TrueDurability
extends Module {
    private static TrueDurability INSTANCE;

    public TrueDurability() {
        super("TrueDurability", Module.Category.MISC, "");
        INSTANCE = this;
    }

    public static TrueDurability getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TrueDurability();
        }
        return INSTANCE;
    }
}

