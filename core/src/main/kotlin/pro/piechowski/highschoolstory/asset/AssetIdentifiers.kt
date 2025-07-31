package pro.piechowski.highschoolstory.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.tiled.TiledMap
import ktx.assets.async.Identifier

object AssetIdentifiers {
    object Textures {
        val PlayerCharacter = Identifier("player_character.png", Texture::class.java)
        val Character = Identifier("character.png", Texture::class.java)
    }

    object Maps {
        val Town = Identifier("maps/town.tmx", TiledMap::class.java)
    }
}
