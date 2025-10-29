package pro.piechowski.highschoolstory.camera

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import pro.piechowski.highschoolstory.physics.px

class MeterCamera : OrthographicCamera(23f, 13f)

class MeterViewport(
    meterCamera: MeterCamera,
) : FitViewport(
        23f,
        13f,
        meterCamera,
    )
