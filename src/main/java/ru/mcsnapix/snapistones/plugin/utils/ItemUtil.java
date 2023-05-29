package ru.mcsnapix.snapistones.plugin.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class ItemUtil {
    public ItemStack setNBTTag(ItemStack itemStack, String key, String value) {
        ItemStack bukkitItemStack;
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        nmsItemCompound.set(key, new NBTTagString(value));
        nmsItem.setTag(nmsItemCompound);
        bukkitItemStack = CraftItemStack.asBukkitCopy(nmsItem);
        return bukkitItemStack;
    }

    public String nbtTag(ItemStack itemStack, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsItem.hasTag()) {
            return null;
        }
        NBTTagCompound nmsItemCompound = nmsItem.getTag();
        if (nmsItemCompound != null) {
            return ((NBTTagString) nmsItemCompound.get(key)).c_();
        }

        return null;
    }

    public boolean hasTag(ItemStack itemStack, String key) {
        return nbtTag(itemStack, key) != null;
    }
}
