package net.marum.villagebusiness.util;

import net.marum.villagebusiness.init.VillagerBusinessItemInit;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class NonEmeraldSlot extends Slot {
    public NonEmeraldSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        Item item = stack.getItem();
        return item != Items.EMERALD && item != Items.EMERALD_BLOCK && item != VillagerBusinessItemInit.EMERALD_NUGGET;
    }
}