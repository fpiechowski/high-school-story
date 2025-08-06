package pro.piechowski.highschoolstory.map

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.CircleMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.stateIn
import ktx.assets.async.AssetStorage
import ktx.async.onRenderingThread
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.circle
import ktx.box2d.loop
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.CoroutineContexts
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.place.Place
import pro.piechowski.highschoolstory.place.PlaceManager

class MapManager : KoinComponent {
    private val placeManager by inject<PlaceManager>()
    private val assetStorage by inject<AssetStorage>()
    private val physicsWorld by inject<PhysicsWorld>()
    private val coroutineScope = CoroutineScope(CoroutineContexts.Logic)

    val currentMap =
        placeManager.currentPlace
            .runningReduce(::replaceLoadedMapAsset)
            .map { it?.let { assetStorage[it.mapAssetIdentifier] } }
            .flowOn(CoroutineContexts.IO)
            .stateIn(coroutineScope, SharingStarted.Companion.Eagerly, null)

    val currentMapBodies =
        currentMap
            .runningFold(
                emptyList(),
                ::replaceBodies,
            ).stateIn(coroutineScope, SharingStarted.Companion.Eagerly, emptyList())

    val mapRenderer =
        currentMap
            .map(::OrthogonalTiledMapRenderer)
            .stateIn(coroutineScope, SharingStarted.Companion.Eagerly, null)

    private suspend fun replaceLoadedMapAsset(
        previousPlace: Place?,
        nextPlace: Place?,
    ): Place? {
        previousPlace?.also { assetStorage.unload(it.mapAssetIdentifier) }
        return nextPlace?.also { assetStorage.load(it.mapAssetIdentifier) }
    }

    private suspend fun replaceBodies(
        previousBodies: List<Body>,
        newMap: TiledMap?,
    ): List<Body> {
        previousBodies.forEach { physicsWorld.destroyBody(it) }

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
