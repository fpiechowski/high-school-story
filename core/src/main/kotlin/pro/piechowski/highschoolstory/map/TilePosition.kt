package pro.piechowski.highschoolstory.map

import com.badlogic.gdx.math.Vector2
import ktx.math.times

class Tile {

    companion object {
        const val SIZE = 48
    }

    class Position(
        x: Int,
        y: Int,
    ) : Vector2(x.toFloat(), y.toFloat()) {
        constructor(position: Vector2) : this(position.x.toInt(), position.y.toInt())

        fun toPixel() = this * SIZE
    }
}

fun Vector2.toTilePosition() = Vector2(x / Tile.SIZE, y / Tile.SIZE)
