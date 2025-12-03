package pro.piechowski.highschoolstory.sprite.character.player

import pro.piechowski.highschoolstory.asset.Assets
import pro.piechowski.highschoolstory.sprite.character.CharacterSpriteSheet
import pro.piechowski.kge.di.DependencyInjection.Global.get

class PlayerCharacterSpriteSheet : CharacterSpriteSheet(get<Assets>().textures.playerCharacterTexture)
