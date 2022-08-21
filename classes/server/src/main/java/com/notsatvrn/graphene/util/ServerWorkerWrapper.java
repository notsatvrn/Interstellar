// Original code by Titaniumtown, licensed under GNU General Public License v3.0
// You can find the original code on https://gitlab.com/Titaniumtown/JettPack

package com.notsatvrn.graphene.util;

import com.google.common.base.Preconditions;
import net.minecraft.Util;

public final class ServerWorkerWrapper implements Runnable {
    private final Runnable internalRunnable;

    public ServerWorkerWrapper(Runnable runnable) {
        this.internalRunnable = Preconditions.checkNotNull(runnable, "internalRunnable");
    }

    @Override
    public final void run() {
        try {
            this.internalRunnable.run();
            return;
        }
        catch (Throwable throwable) {
            Util.onThreadException(Thread.currentThread(), throwable);
            return;
        }
    }
}

