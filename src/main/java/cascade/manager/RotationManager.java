/*
 * Decompiled with CFR 0.152.
 */
package cascade.manager;

import cascade.features.Feature;
import cascade.util.player.RotationUtil;

public class RotationManager
extends Feature {
    public int getDirection4D() {
        return RotationUtil.getDirection4D();
    }

    public String getDirection4D(boolean northRed) {
        return RotationUtil.getDirection4D(northRed);
    }
}

