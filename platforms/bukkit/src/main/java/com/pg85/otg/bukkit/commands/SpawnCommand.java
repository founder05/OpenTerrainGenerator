package com.pg85.otg.bukkit.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.pg85.otg.OTG;
import com.pg85.otg.bukkit.OTGPerm;
import com.pg85.otg.bukkit.OTGPlugin;
import com.pg85.otg.bukkit.world.WorldHelper;
import com.pg85.otg.common.LocalWorld;
import com.pg85.otg.customobjects.CustomObject;
import com.pg85.otg.util.bo3.Rotation;

public class SpawnCommand extends BaseCommand
{
    public SpawnCommand(OTGPlugin _plugin)
    {
        super(_plugin);
        name = "spawn";
        perm = OTGPerm.CMD_SPAWN.node;
        usage = "spawn Name";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> args)
    {
        Player me = (Player) sender;
        Random random = new Random();

        String objectName = StringUtils.join(args, " ").trim();
        LocalWorld bukkitWorld = WorldHelper.toLocalWorld(me.getWorld());

        if (args.isEmpty())
        {
            me.sendMessage(ERROR_COLOR + "You must enter the name of the BO2/BO3.");
            return true;
        }

        // Search the current world, if no BO3 is found, search dims, else globalobjects.
        CustomObject spawnObject = null;
        if (bukkitWorld != null)
        {
            spawnObject = OTG.getCustomObjectManager().getGlobalObjects().getObjectByName(objectName, bukkitWorld.getName(), false);
        }

        if(spawnObject == null)
        {
	        for(LocalWorld localWorld : OTG.getAllWorlds())
	        {
	        	if(localWorld != bukkitWorld)
	        	{
	        		spawnObject = OTG.getCustomObjectManager().getGlobalObjects().getObjectByName(objectName, localWorld.getName(), false);
	        	}
	        	if(spawnObject != null)
	        	{
	        		break;
	        	}
	        }
        }
        if(spawnObject == null)
        {
        	spawnObject = OTG.getCustomObjectManager().getGlobalObjects().getObjectByName(objectName, null, true);
        }

        if (spawnObject == null)
        {
            sender.sendMessage(ERROR_COLOR + "Object not found.");
            return true;
        }

        Block block = this.getWatchedBlock(me, true);
        if (block == null)
        {
            return true;
        }
        
        if (spawnObject.spawnForced(bukkitWorld, random, Rotation.NORTH, block.getX(), block.getY(), block.getZ()))
        {
            me.sendMessage(BaseCommand.MESSAGE_COLOR + spawnObject.getName() + " was spawned.");
        } else {
            me.sendMessage(BaseCommand.ERROR_COLOR + "Object can't be spawned over there.");
        }

        return true;
    }

    public Block getWatchedBlock(Player me, boolean verbose)
    {
        if (me == null)
            return null;

        Block block;
        Block previousBlock = null;

        Iterator<Block> itr = new BlockIterator(me, 200);
        while (itr.hasNext())
        {
            block = itr.next();
            if (block.getType() != Material.AIR && block.getType() != Material.LONG_GRASS)
            {
                return previousBlock;
            }
            previousBlock = block;
        }

        if (verbose)
        {
            me.sendMessage(ERROR_COLOR + "No block in sight.");
        }

        return null;
    }
}