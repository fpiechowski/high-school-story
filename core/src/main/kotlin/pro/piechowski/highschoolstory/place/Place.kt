package pro.piechowski.highschoolstory.place

import com.badlogic.gdx.maps.tiled.TiledMap
import kotlinx.coroutines.Deferred
import ktx.assets.async.Identifier
import pro.piechowski.highschoolstory.map.Map

open class Place(
    val name: String,
    val map: Map,
)
