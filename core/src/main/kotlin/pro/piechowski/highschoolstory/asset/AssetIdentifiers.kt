package pro.piechowski.highschoolstory.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.tiled.TiledMap
import ktx.assets.async.Identifier

object AssetIdentifiers {
    object Textures {
        fun characterTextureIdentifier(name: String) = Identifier("textures/character/$name.png", Texture::class.java)

        fun textureIdentifier(name: String) = Identifier("textures/$name.png", Texture::class.java)

        val PlayerCharacter = characterTextureIdentifier("player_character")
        val Character = characterTextureIdentifier("character")
        val Exteriors = textureIdentifier("exteriors")
    }

    object Maps {
        fun mapIdentifier(name: String) = Identifier("maps/$name.tmx", TiledMap::class.java)

        val Town = mapIdentifier("town")
        val Road = mapIdentifier("road")
    }
}
