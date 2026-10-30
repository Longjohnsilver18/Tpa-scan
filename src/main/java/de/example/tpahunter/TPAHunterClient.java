package de.example.tpahunter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class TPAHunterClient implements ClientModInitializer {
    public static final ScanState STATE = new ScanState();
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        // German keyboard: physical semicolon key is the Ä key on a QWERTZ layout.
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tpahunter.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_SEMICOLON,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new HunterScreen());
                }
            }
        });

        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (!STATE.isScanning()) return;

            String raw = message.getString();
            if (TpaPhraseMatcher.looksLikeTpaOffer(raw)) {
                String player = sender == null ? "Unbekannt" : sender.getName();
                STATE.addCandidate(player, raw);
            }
        });
    }
}
