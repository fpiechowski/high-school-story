package pro.piechowski.highschoolstory.exterior

import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.async.AssetStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.asset.AssetIdentifiers

class ExteriorTexture : KoinComponent {
    private val assetStorage by inject<AssetStorage>()
    val texture = assetStorage.loadAsync(AssetIdentifiers.Textures.Exteriors)

    suspend fun region(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
    ) = TextureRegion(texture.await(), x, y, width, height)
}
