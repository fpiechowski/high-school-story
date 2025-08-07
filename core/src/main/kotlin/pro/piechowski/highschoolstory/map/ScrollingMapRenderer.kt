package pro.piechowski.highschoolstory.map

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

class ScrollingMapRenderer(
    map: TiledMap,
    val scrolling: Map.Scrolling,
    unitScale: Float = 1f,
) : OrthogonalTiledMapRenderer(map, unitScale) {
    private var scrollX = 0f
    private var scrollY = 0f

    val speedX = if (scrolling is Map.Scrolling.Horizontal) scrolling.speed else 0f
    val speedY = if (scrolling is Map.Scrolling.Vertical) scrolling.speed else 0f

    private val mapWidthPx: Float
    private val mapHeightPx: Float

    init {
        val widthInTiles = map.properties.get("width", Int::class.java)
        val heightInTiles = map.properties.get("height", Int::class.java)
        val tileWidth = map.properties.get("tilewidth", Int::class.java)
        val tileHeight = map.properties.get("tileheight", Int::class.java)

        mapWidthPx = widthInTiles * tileWidth * unitScale
        mapHeightPx = heightInTiles * tileHeight * unitScale
    }

    fun update(delta: Float) {
        scrollX += speedX * delta
        scrollY += speedY * delta

        // Wrap horizontally
        if (scrollX >= mapWidthPx) scrollX -= mapWidthPx
        if (scrollX <= -mapWidthPx) scrollX += mapWidthPx

        // Wrap vertically
        if (scrollY >= mapHeightPx) scrollY -= mapHeightPx
        if (scrollY <= -mapHeightPx) scrollY += mapHeightPx
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
        camera.position.y = originalY + scrollY
        camera.update()
        setView(camera)
        if (layers == null) render() else render(layers)

        // Second tile (above/below)
        val offsetY = if (speedY > 0) scrollY - mapHeightPx else scrollY + mapHeightPx
        camera.position.y = originalY + offsetY
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
        camera.position.x = originalX + scrollX
        camera.update()
        setView(camera)
        if (layers == null) render() else render(layers)

        // Second tile (to the left/right)
        val offsetX = if (speedX > 0) scrollX - mapWidthPx else scrollX + mapWidthPx
        camera.position.x = originalX + offsetX
        camera.update()
        setView(camera)
        if (layers == null) render() else render(layers)

        camera.position.x = originalX
    }
}
