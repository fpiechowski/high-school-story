package pro.piechowski.highschoolstory.map

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import pro.piechowski.highschoolstory.physics.Meter
import pro.piechowski.highschoolstory.physics.m
import pro.piechowski.highschoolstory.physics.mps
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.physics.s

class ScrollingMapRenderer(
    map: TiledMap,
    val scrolling: Map.Scrolling,
    unitScale: Float = 1f,
) : OrthogonalTiledMapRenderer(map, unitScale) {
    private var scrollX = 0f.m
    private var scrollY = 0f.m

    val speedX = if (scrolling is Map.Scrolling.Horizontal) scrolling.speed else 0f.mps
    val speedY = if (scrolling is Map.Scrolling.Vertical) scrolling.speed else 0f.mps

    private val mapWidth: Meter
    private val mapHeight: Meter

    init {
        val widthInTiles = map.properties.get("width", Int::class.java)
        val heightInTiles = map.properties.get("height", Int::class.java)
        val tileWidth = map.properties.get("tilewidth", Int::class.java)
        val tileHeight = map.properties.get("tileheight", Int::class.java)

        mapWidth = (widthInTiles * tileWidth).px.toMeter()
        mapHeight = (heightInTiles * tileHeight).px.toMeter()
    }

    fun update(delta: Float) {
        scrollX += speedX * delta.s
        scrollY += speedY * delta.s

        // Wrap horizontally
        if (scrollX >= mapWidth) scrollX -= mapWidth
        if (scrollX <= -mapWidth) scrollX += mapWidth

        // Wrap vertically
        if (scrollY >= mapHeight) scrollY -= mapHeight
        if (scrollY <= -mapHeight) scrollY += mapHeight
    }

    /**
     * Renders the map infinitely tiled in the scrolling directions.
     * @param camera The camera to render from.
     * @param layers The layers to render (all if null).
     */
    fun renderLooped(
        camera: OrthographicCamera,
        layers: IntArray? = null,
    ) {
        val originalX = camera.position.x
        val originalY = camera.position.y

        if (scrolling is Map.Scrolling.Horizontal) {
            scrollHorizontally(camera, originalX, layers)
        } else if (scrolling is Map.Scrolling.Vertical) {
            scrollVertically(camera, originalY, layers)
        } else {
            camera.update()
            setView(camera)
            if (layers == null) render() else render(layers)
        }

        restoreCamera(camera, originalX, originalY)
    }

    private fun restoreCamera(
        camera: OrthographicCamera,
        originalX: Float,
        originalY: Float,
    ) {
        camera.position.x = originalX
        camera.position.y = originalY
        camera.update()
    }

    private fun scrollVertically(
        camera: OrthographicCamera,
        originalY: Float,
        layers: IntArray?,
    ) {
        // First tile
        camera.position.y = (originalY.m + scrollY).value
        camera.update()
        setView(camera)
        if (layers == null) render() else render(layers)

        // Second tile (above/below)
        val offsetY = if (speedY > 0f.mps) scrollY - mapHeight else scrollY + mapHeight
        camera.position.y = (originalY.m + offsetY).value
        camera.update()
        setView(camera)
        if (layers == null) render() else render(layers)

        camera.position.y = originalY
    }

    private fun scrollHorizontally(
        camera: OrthographicCamera,
        originalX: Float,
        layers: IntArray?,
    ) {
        // First tile
        camera.position.x = (originalX.m + scrollX).value
        camera.update()
        setView(camera)
        if (layers == null) render() else render(layers)

        // Second tile (to the left/right)
        val offsetX = if (speedX > 0f.mps) scrollX - mapWidth else scrollX + mapWidth
        camera.position.x = (originalX.m + offsetX).value
        camera.update()
        setView(camera)
        if (layers == null) render() else render(layers)

        camera.position.x = originalX
    }
}
