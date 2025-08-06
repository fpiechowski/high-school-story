package pro.piechowski.highschoolstory.map

import com.github.quillraven.fleks.IntervalSystem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.PixelCamera
import pro.piechowski.highschoolstory.place.PlaceManager

sealed class MapRenderingSystem(
    val layers: List<MapLayer> = emptyList(),
) : IntervalSystem(),
    KoinComponent {
    private val mapManager by inject<MapManager>()
    private val pixelCamera by inject<PixelCamera>()

    override fun onTick() {
        mapManager.mapRenderer.value?.let { renderer ->
            renderer.setView(pixelCamera)
            mapManager.currentMap.value?.let { map ->
                val layerIndices =
                    map.layers
                        .mapIndexed { idx, layer -> idx to layer }
                        .toMap()
                        .filterValues { layer -> layer.name in layers.map { it.name } }
                        .map { it.key }

                renderer.render(layerIndices.toIntArray())
            }
        }
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
