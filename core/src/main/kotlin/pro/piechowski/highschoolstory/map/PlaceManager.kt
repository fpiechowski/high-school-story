package pro.piechowski.highschoolstory.map

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.CircleMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ktx.assets.async.AssetStorage
import ktx.async.RenderingScope
import ktx.async.newSingleThreadAsyncContext
import ktx.async.onRenderingThread
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.circle
import ktx.box2d.loop
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.place.Place

class PlaceManager : KoinComponent {
    private val coroutineScope = RenderingScope()

    val currentPlace = MutableStateFlow<Place?>(null)

    val currentMap =
        currentPlace
            .map { it?.let { assetStorage.load(it.mapAssetIdentifier) } }
            .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    val currentMapBodies =
        currentMap
            .map { map ->
                map?.let {
                    it.layers
                        ?.find { it.name == MapLayer.Walls.name }
                        ?.objects
                        ?.map { wall -> onRenderingThread { wall.toStaticBody() } }
                } ?: emptyList()
            }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val mapRenderer =
        currentMap
            .map(::OrthogonalTiledMapRenderer)
            .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    private val physicsWorld by inject<PhysicsWorld>()
    private val assetStorage by inject<AssetStorage>()

    suspend fun openPlace(place: Place) {
        currentPlace.value?.mapAssetIdentifier?.let { assetStorage.unload(it) }
        onRenderingThread {
            currentMapBodies.value.map { physicsWorld.destroyBody(it) }
        }

        currentPlace.value = place
    }

    private suspend fun MapObject.toStaticBody() =
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
