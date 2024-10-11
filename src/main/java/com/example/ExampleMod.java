package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text; // Correct import for Text
import net.minecraft.util.Identifier; // Import if you need it

import org.lwjgl.glfw.GLFW;

public class DupeMod implements ModInitializer {

    private static KeyBinding dupeKeyBinding;

    @Override
    public void onInitialize() {
        dupeKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.dupemod.duplicate",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_KP_ADD,
            "category.dupemod.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (dupeKeyBinding.wasPressed()) {
                if (client.player != null) {
                    duplicateHeldItem(client.player);
                }
            }
        });
    }

    private void duplicateHeldItem(PlayerEntity player) {
        ItemStack heldItem = player.getMainHandStack();
        if (!heldItem.isEmpty()) {
            ItemStack duplicate = heldItem.copy();
            if (player.getInventory().insertStack(duplicate)) {
                player.sendMessage(Text.literal("Item duplicated!"), true);
            } else {
                player.sendMessage(Text.literal("No room in inventory!"), true);
            }
        }
    }
}
