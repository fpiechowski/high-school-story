package pro.piechowski.highschoolstory.map

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.CircleMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import ktx.assets.async.AssetStorage
import ktx.async.RenderingScope
import ktx.async.onRenderingThread
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.circle
import ktx.box2d.loop
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.CoroutineContexts
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.METERS_PER_PIXEL
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.place.PlaceManager

class MapManager : KoinComponent {
    private val placeManager by inject<PlaceManager>()
    private val assetStorage by inject<AssetStorage>()
    private val physicsWorld by inject<PhysicsWorld>()
    private val coroutineScope = RenderingScope()

    val currentMap =
        placeManager.currentPlace
            .map { it?.map }
            .runningFold(null, ::replaceLoadedMapAsset)
            .map { it?.apply { tiledMap.complete(assetStorage[it.assetIdentifier]) } }
            .flowOn(CoroutineContexts.IO)
            .stateIn(coroutineScope, SharingStarted.Companion.Eagerly, null)

    val currentTiledMap =
        currentMap
            .map { it?.tiledMap?.await() }
            .stateIn(coroutineScope, SharingStarted.Companion.Eagerly, null)

    val currentMapBodies =
        currentTiledMap
            .runningFold(
                emptyList(),
                ::replaceBodies,
            ).stateIn(coroutineScope, SharingStarted.Companion.Eagerly, emptyList())

    val mapRenderer =
        currentMap
            .filterNotNull()
            .map { map ->
                map.scrolling?.let { scroll ->
                    ScrollingMapRenderer(map.tiledMap.await(), scroll, METERS_PER_PIXEL)
                } ?: OrthogonalTiledMapRenderer(map.tiledMap.await(), METERS_PER_PIXEL)
            }.stateIn(coroutineScope, SharingStarted.Companion.Eagerly, null)

    private suspend fun replaceLoadedMapAsset(
        previousMap: Map?,
        nextMap: Map?,
    ): Map? {
        previousMap?.also { assetStorage.unload(it.assetIdentifier) }
        return nextMap?.also { assetStorage.load(it.assetIdentifier) }
    }

    private suspend fun replaceBodies(
        previousBodies: List<Body>,
        newMap: TiledMap?,
    ): List<Body> {
        onRenderingThread {
            previousBodies.forEach { physicsWorld.destroyBody(it) }
        }

        return newMap
            ?.layers
            ?.find { it.name == MapLayer.Walls.name }
            ?.objects
            ?.map { wall -> wall.toStaticBody() } ?: emptyList()
    }

    private suspend fun MapObject.toStaticBody() =
        onRenderingThread {
            physicsWorld.body {
                when (this@toStaticBody) {
                    is PolygonMapObject -> {
                        loop(
                            this@toStaticBody
                                .polygon.vertices
                                .map { it.px.toMeter().value }
                                .toFloatArray(),
                        )

                        position.set(
                            this@toStaticBody.polygon.let {
                                Vector2(
                                    it.x.px
                                        .toMeter()
                                        .value,
                                    it.y.px
                                        .toMeter()
                                        .value,
                                )
                            },
                        )
                    }

                    is RectangleMapObject ->
                        box(
                            this@toStaticBody
                                .rectangle.width.px
                                .toMeter()
                                .value,
                            this@toStaticBody
                                .rectangle.height.px
                                .toMeter()
                                .value,
                        )

                    is CircleMapObject ->
                        circle(
                            this@toStaticBody
                                .circle.radius.px
                                .toMeter()
                                .value,
                        )
                }
            }
        }
}
