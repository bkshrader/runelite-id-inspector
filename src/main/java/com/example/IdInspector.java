package com.example;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Example"
)
public class IdInspector extends Plugin {
    @Inject
    private Client client;

    private static final String INSPECT = "Inspect ID";

    @Override
    protected void startUp() throws Exception {
        log.info("Id inspector started!");
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Id inspector stopped!");
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        final MenuEntry menuEntry = event.getMenuEntry();
        final MenuAction menuAction = menuEntry.getType();
        final String target = menuEntry.getTarget();

        final boolean hotKeyPressed = client.isKeyPressed(KeyCode.KC_SHIFT);
        if (hotKeyPressed && (menuAction == MenuAction.EXAMINE_OBJECT || menuAction == MenuAction.EXAMINE_NPC)) {
                client.createMenuEntry(-1)
                        .setOption(INSPECT)
                        .setTarget(event.getTarget())
                        .setIdentifier(menuEntry.getIdentifier())
                        .onClick(e -> {
                            log.info(String.format("Target Object (ID: %d): %s", e.getIdentifier(), e.getTarget()));
                            client.addChatMessage(ChatMessageType.GAMEMESSAGE,
                                    "",
                                    Text.removeTags(String.format("%s - %s ID: %d", e.getTarget(), e.getNpc() == null ? "Object": "NPC", e.getIdentifier())),
                                    null);
                        });
        }
    }


}
