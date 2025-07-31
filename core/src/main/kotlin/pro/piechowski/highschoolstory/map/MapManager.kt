package pro.piechowski.highschoolstory.map

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MapManager {
    val currentMap = MutableStateFlow<TiledMap?>(null)

    val mapRenderer get() = currentMap.value?.let(::OrthogonalTiledMapRenderer)

    fun openMap(map: TiledMap) {
        currentMap.value = map
    }
}
