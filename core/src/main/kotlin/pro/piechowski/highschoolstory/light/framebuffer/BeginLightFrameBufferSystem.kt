package pro.piechowski.highschoolstory.light.framebuffer

import io.github.oshai.kotlinlogging.KotlinLogging
import pro.piechowski.highschoolstory.framebuffer.BeginFrameBufferSystem

class BeginLightFrameBufferSystem(
    frameBufferManager: LightFrameBufferManager,
) : BeginFrameBufferSystem<LightFrameBufferManager>(frameBufferManager) {
    private val logger = KotlinLogging.logger { }

    override fun onTick() {
        super.onTick()

        logger.debug { "Light frame buffer started" }
    }
}
