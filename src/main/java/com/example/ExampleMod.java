package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import org.lwjgl.glfw.GLFW;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Text;


public class DupeMod implements ModInitializer {

    // Declare a KeyBinding for the "+" key
    private static KeyBinding dupeKeyBinding;

    @Override
    public void onInitialize() {
        // Register the key binding for the "+" key (GLFW.GLFW_KEY_KP_ADD refers to the Numpad "+")
        dupeKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.dupemod.duplicate",  // Translation key for the control settings
            InputUtil.Type.KEYSYM,     // We are binding to a keyboard key
            GLFW.GLFW_KEY_KP_ADD,      // The "+" key on the numpad
            "category.dupemod.keys"    // Control category
        ));

        // Register a callback that runs on every game tick to check if the "+" key is pressed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (dupeKeyBinding.wasPressed()) {
                if (client.player != null) {
                    duplicateHeldItem(client.player);
                }
            }
        });
    }

    // Method to duplicate the item in the player's hand
    private void duplicateHeldItem(PlayerEntity player) {
        ItemStack heldItem = player.getMainHandStack();  // Get the item in the player's main hand
        if (!heldItem.isEmpty()) {
            // Duplicate the item by adding a copy to the player's inventory
            ItemStack duplicate = heldItem.copy();  // Create a copy of the held item
            if (player.getInventory().insertStack(duplicate)) {
                player.sendMessage(Text.literal("Item duplicated!"), true);  // Send a message to the player
            } else {
                player.sendMessage(Text.literal("No room in inventory!"), true);  // No space for the duplicate
            }
        }
    }
}
