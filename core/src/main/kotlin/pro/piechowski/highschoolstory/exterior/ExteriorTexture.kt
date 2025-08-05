package pro.piechowski.highschoolstory.exterior

import com.badlogic.gdx.graphics.Texture
import ktx.assets.async.AssetStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.asset.AssetIdentifiers

class ExteriorTexture(
    val texture: Texture,
) {
    class Loader : KoinComponent {
        private val assetStorage by inject<AssetStorage>()

        suspend fun load() = assetStorage.load(AssetIdentifiers.Textures.Exteriors)
    }
}
