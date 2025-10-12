package pro.piechowski.highschoolstory.light.framebuffer

import io.github.oshai.kotlinlogging.KotlinLogging
import pro.piechowski.highschoolstory.framebuffer.EndFrameBufferSystem

class EndLightFrameBufferSystem(
    frameBufferManager: LightFrameBufferManager,
) : EndFrameBufferSystem<LightFrameBufferManager>(frameBufferManager) {
    private val logger = KotlinLogging.logger { }

    override fun onTick() {
        super.onTick()

        logger.debug { "Light frame buffer ended" }
    }
}
