package org.example.spongetemplate;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
    id = SpongeTemplate.PLUGIN_ID,
    name = SpongeTemplate.PLUGIN_NAME,
    description = "Sponge plugin template",
    version = "@version@",
    authors = "SettingDust"
)
public class SpongeTemplate {

    public static final String PLUGIN_ID = "sponge-template";
    public static final String PLUGIN_NAME = "Sponge Template";

    @Inject private Logger logger;

    @Inject
    public SpongeTemplate(
        EventManager eventManager,
        PluginContainer pluginContainer
    ) { eventManager.registerListener(pluginContainer, GameInitializationEvent.class, this::onInit); }

    private void onInit(GameInitializationEvent event) {
        logger.info("Template plugin is running!");
    }
}
