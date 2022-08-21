// Original code by CaffeineMC, licensed under GNU Lesser General Public License v3.0
// You can find the original code on https://github.com/CaffeineMC/lithium-fabric (Yarn mappings)

package com.notsatvrn.graphene.util.tuples;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.SectionPos;

//Y values use coordinates, not indices (y=0 -> chunkY=0)
//upper bounds are EXCLUSIVE
public record WorldSectionBox(Level world, int chunkX1, int chunkY1, int chunkZ1, int chunkX2, int chunkY2,
                              int chunkZ2) {
    public static WorldSectionBox entityAccessBox(Level world, AABB box) {
        int minX = SectionPos.posToSectionCoord((double)box.minX - 2.0D);
        int minY = SectionPos.posToSectionCoord((double)box.minY - 2.0D);
        int minZ = SectionPos.posToSectionCoord((double)box.minZ - 2.0D);
        int maxX = SectionPos.posToSectionCoord((double)box.maxX + 2.0D) + 1;
        int maxY = SectionPos.posToSectionCoord((double)box.maxY + 2.0D) + 1;
        int maxZ = SectionPos.posToSectionCoord((double)box.maxZ + 2.0D) + 1;
        return new WorldSectionBox(world, minX, minY, minZ, maxX, maxY, maxZ);
    }

    public int numSections() {
        return (this.chunkX2 - this.chunkX1) * (this.chunkY2 - this.chunkY1) * (this.chunkZ2 - this.chunkZ1);
    }
}

