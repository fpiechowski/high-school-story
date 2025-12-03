package pro.piechowski.highschoolstory.exterior

import com.badlogic.gdx.graphics.g2d.TextureRegion
import pro.piechowski.highschoolstory.asset.Assets
import pro.piechowski.kge.di.DependencyInjection.Global.inject

class ExteriorTexture {
    val assets by inject<Assets>()

    fun region(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
    ) = TextureRegion(assets.textures.exteriorsTexture, x, y, width, height)
}
