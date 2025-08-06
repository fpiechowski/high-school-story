package pro.piechowski.highschoolstory.lwjgl3

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class TileSelectionTool(
    private val imagePath: String,
    private var gridSize: Float = 48f,
) : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var texture: Texture
    private lateinit var camera: OrthographicCamera
    private lateinit var shapeRenderer: ShapeRenderer

    private var zoomSpeed = 0.1f
    private var panButton = Input.Buttons.MIDDLE // or RIGHT

    private var dragStartWorld: Vector3? = null
    private var dragEndWorld: Vector3? = null
    private var panningLast = Vector3()

    override fun create() {
        batch = SpriteBatch()
        texture = Texture(imagePath)
        shapeRenderer = ShapeRenderer()

        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)

        Gdx.input.inputProcessor =
            object : InputAdapter() {
                override fun touchDown(
                    screenX: Int,
                    screenY: Int,
                    pointer: Int,
                    button: Int,
                ): Boolean {
                    if (button == Input.Buttons.LEFT) {
                        dragStartWorld = snapToGrid(camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f)))
                        dragEndWorld = null
                    } else if (button == panButton) {
                        panningLast.set(screenX.toFloat(), screenY.toFloat(), 0f)
                    }
                    return true
                }

                override fun touchDragged(
                    screenX: Int,
                    screenY: Int,
                    pointer: Int,
                ): Boolean {
                    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        dragEndWorld = snapToGrid(camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f)))
                    } else if (Gdx.input.isButtonPressed(panButton)) {
                        val deltaX = screenX - panningLast.x
                        val deltaY = screenY - panningLast.y
                        camera.translate(-deltaX * camera.zoom, deltaY * camera.zoom)
                        panningLast.set(screenX.toFloat(), screenY.toFloat(), 0f)
                    }
                    return true
                }

                override fun touchUp(
                    screenX: Int,
                    screenY: Int,
                    pointer: Int,
                    button: Int,
                ): Boolean {
                    if (button == Input.Buttons.LEFT && dragStartWorld != null && dragEndWorld != null) {
                        val x1 = dragStartWorld!!.x
                        val y1 = dragStartWorld!!.y
                        val x2 = dragEndWorld!!.x
                        val y2 = dragEndWorld!!.y

                        val rect =
                            Rectangle(
                                min(x1, x2),
                                min(y1, y2),
                                abs(x2 - x1),
                                abs(y2 - y1),
                            )

                        println("Selection rect: x=${rect.x}, y=${rect.y}, width=${rect.width}, height=${rect.height}")
                    }
                    dragStartWorld = null
                    dragEndWorld = null
                    return true
                }

                override fun scrolled(
                    amountX: Float,
                    amountY: Float,
                ): Boolean {
                    camera.zoom *= 1f + zoomSpeed * amountY
                    camera.zoom = max(0.1f, min(camera.zoom, 10f))
                    return true
                }

                override fun keyDown(keycode: Int): Boolean {
                    when (keycode) {
                        Input.Keys.PLUS, Input.Keys.EQUALS -> gridSize += 4f
                        Input.Keys.MINUS -> gridSize = max(4f, gridSize - 4f)
                    }
                    return true
                }
            }
    }

    private fun snapToGrid(world: Vector3): Vector3 {
        val snappedX = floor(world.x / gridSize) * gridSize
        val snappedY = floor(world.y / gridSize) * gridSize
        return Vector3(snappedX, snappedY, 0f)
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        // Draw image
        batch.begin()
        batch.draw(texture, 0f, 0f)
        batch.end()

        // Draw grid
        drawGrid()

        // Draw selection rectangle
        if (dragStartWorld != null && dragEndWorld != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
            shapeRenderer.color = Color.RED

            val x1 = dragStartWorld!!.x
            val y1 = dragStartWorld!!.y
            val x2 = dragEndWorld!!.x
            val y2 = dragEndWorld!!.y

            shapeRenderer.rect(
                min(x1, x2),
                min(y1, y2),
                abs(x2 - x1),
                abs(y2 - y1),
            )

            shapeRenderer.end()
        }
    }

    private fun drawGrid() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.LIGHT_GRAY

        val cols = (texture.width / gridSize).toInt()
        val rows = (texture.height / gridSize).toInt()

        for (x in 0..cols) {
            val px = x * gridSize
            shapeRenderer.line(px, 0f, px, texture.height.toFloat())
        }
        for (y in 0..rows) {
            val py = y * gridSize
            shapeRenderer.line(0f, py, texture.width.toFloat(), py)
        }

        shapeRenderer.end()
    }

    override fun dispose() {
        batch.dispose()
        texture.dispose()
        shapeRenderer.dispose()
    }
}

fun main() {
    val config =
        Lwjgl3ApplicationConfiguration().apply {
            setTitle("Tile Selection Tool")
            setWindowedMode(1024, 768)
        }
    Lwjgl3Application(TileSelectionTool("assets/textures/exteriors.png"), config)
}
