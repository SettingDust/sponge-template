package org.example.spongetemplate

import com.google.inject.Inject
import me.settingdust.laven.sponge.registerListener
import org.slf4j.Logger
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.plugin.PluginContainer

class SpongeTemplate @Inject constructor(
    logger: Logger,
    pluginContainer: PluginContainer
) {
    init {
        pluginContainer.registerListener<GameInitializationEvent> {
            logger.info("Template plugin is running!")
        }
    }
}
