package pro.piechowski.highschoolstory.asset

import arrow.fx.coroutines.await.ExperimentalAwaitAllApi
import arrow.fx.coroutines.await.awaitAll
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.tiled.TiledMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import ktx.assets.async.AssetStorage
import ktx.assets.async.Identifier
import ktx.async.KtxAsync
import pro.piechowski.kge.di.DependencyInjection.Global.inject

@ExperimentalCoroutinesApi
@ExperimentalAwaitAllApi
class AssetsLoader {
    suspend fun load() = assetsDeferred.await()

    private val assetsDeferred = KtxAsync.async { Assets() }

    val assets: Assets get() = if (assetsDeferred.isCompleted) {
        assetsDeferred.getCompleted()
    } else {
        error("Assets were requested before they finished loading.")
    }
}

class Assets private constructor(
    val textures: Textures,
    val maps: Maps,
) {
    @ExperimentalAwaitAllApi
    companion object {
        suspend operator fun invoke() = awaitAll {
            val textures = async { Textures() }
            val maps = async { Maps() }

            Assets(textures.await(), maps.await())
        }
    }

    class Textures private constructor(
        val playerCharacterTexture: Texture,
        val characterTexture: Texture,
        val exteriorsTexture: Texture,
    ) {
        @ExperimentalAwaitAllApi
        companion object {
            private val assetStorage by inject<AssetStorage>()

            suspend fun characterTexture(name: String) =
                assetStorage.load(Identifier("textures/character/$name.png", Texture::class.java))

            suspend fun texture(name: String) = assetStorage.load(Identifier("textures/$name.png", Texture::class.java))

            suspend operator fun invoke() = awaitAll {
                val playerCharacterTexture = async { characterTexture("player_character") }
                val characterTexture = async { characterTexture("character") }
                val exteriorsTexture = async { texture("exteriors") }

                Textures(
                    playerCharacterTexture = playerCharacterTexture.await(),
                    characterTexture = characterTexture.await(),
                    exteriorsTexture = exteriorsTexture.await(),
                )
            }
        }

    }

    class Maps private constructor(
        val town: TiledMap,
        val road: TiledMap,
    ) {
        @ExperimentalAwaitAllApi
        companion object {
            val assetStorage by inject<AssetStorage>()

            suspend fun map(name: String) = assetStorage.load(Identifier("maps/$name.tmx", TiledMap::class.java))

            suspend operator fun invoke() = awaitAll {
                val town = async { map("town") }
                val road = async { map("road") }

                Maps(
                    town = town.await(),
                    road = road.await(),
                )
            }
        }
    }
}
