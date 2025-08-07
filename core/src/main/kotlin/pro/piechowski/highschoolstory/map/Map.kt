package pro.piechowski.highschoolstory.map

import com.badlogic.gdx.maps.tiled.TiledMap
import kotlinx.coroutines.CompletableDeferred
import ktx.assets.async.Identifier

class Map(
    val assetIdentifier: Identifier<TiledMap>,
    val scrolling: Scrolling? = null,
) {
    val tiledMap: CompletableDeferred<TiledMap> = CompletableDeferred()

    sealed class Scrolling(
        val speed: Float,
    ) {
        class Horizontal(
            speed: Float,
        ) : Scrolling(speed)

        class Vertical(
            speed: Float,
        ) : Scrolling(speed)
    }
}
