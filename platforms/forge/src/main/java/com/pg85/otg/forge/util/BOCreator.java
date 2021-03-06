package com.pg85.otg.forge.util;

import com.pg85.otg.util.bo3.NamedBinaryTag;
import com.sk89q.worldedit.regions.Region;

import net.minecraft.world.World;

public abstract class BOCreator
{
	protected String name;
    protected boolean includeAir = false;
    protected boolean includeTiles = false;

    public abstract boolean create(Region selection, World world, String blockName, boolean branch);
    
    protected String getTileEntityName(NamedBinaryTag tag)
    {
        NamedBinaryTag idTag = tag.getTag("id");
        if (idTag != null)
        {
            String name = (String) idTag.getValue();

            return name.replace("minecraft:", "").replace(':', '_');
        }
        return "Unknown";
    }

    public void includeAir(boolean include)
    {
        this.includeAir = include;
    }

    public void includeTiles(boolean include)
    {
        this.includeTiles = include;
    }
}
