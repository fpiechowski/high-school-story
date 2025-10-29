package pro.piechowski.highschoolstory.map

import com.github.quillraven.fleks.IntervalSystem
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.MeterCamera

sealed class MapRenderingSystem(
    val layers: List<MapLayer> = emptyList(),
) : IntervalSystem(),
    KoinComponent {
    private val mapManager by inject<MapManager>()
    private val meterCamera by inject<MeterCamera>()

    private val logger = KotlinLogging.logger { }

    override fun onTick() {
        mapManager.mapRenderer.value?.let { renderer ->
            renderer.setView(meterCamera)
            mapManager.currentTiledMap.value?.let { map ->
                val layerIndices =
                    map.layers
                        .mapIndexed { idx, layer -> idx to layer }
                        .toMap()
                        .filterValues { layer -> layer.name in layers.map { it.name } }
                        .map { it.key }

                when (renderer) {
                    is ScrollingMapRenderer ->
                        renderer
                            .renderLooped(meterCamera, layerIndices.toIntArray())
                            .also { renderer.update(deltaTime) }
                    else -> renderer.render(layerIndices.toIntArray())
                }
            }
        }

        logger.debug { "Map layers $layers rendered" }
    }

    class Background : MapRenderingSystem(layerNames) {
        companion object {
            val layerNames = listOf(MapLayer.Background, MapLayer.Ground)
        }
    }

    class Foreground : MapRenderingSystem(layerNames) {
        companion object {
            val layerNames = listOf(MapLayer.Foreground)
        }
    }
}
