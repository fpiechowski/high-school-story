package pro.piechowski.highschoolstory.camera

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import org.koin.dsl.module
import pro.piechowski.highschoolstory.physics.meterCameraQualifier
import pro.piechowski.highschoolstory.physics.meterViewportQualifier
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.rendering.pixelCameraQualifier
import pro.piechowski.highschoolstory.rendering.pixelViewportQualifier
import pro.piechowski.highschoolstory.ui.uiViewportQualifier

val CameraModule =
    module {
        single<Camera>(meterCameraQualifier) {
            OrthographicCamera()
        }
        single<Viewport>(meterViewportQualifier) {
            FitViewport(
                1280f
                    .px
                    .toMeter()
                    .value,
                720f
                    .px
                    .toMeter()
                    .value,
                get(meterCameraQualifier),
            )
        }

        single<Camera>(pixelCameraQualifier) { OrthographicCamera() }
        single<OrthographicCamera> { get<Camera>(pixelCameraQualifier) as OrthographicCamera }
        single<Viewport>(pixelViewportQualifier) {
            FitViewport(1280f, 720f, get(pixelCameraQualifier))
        }

        single { CameraMovementSystem() }

        single<Viewport>(uiViewportQualifier) { FitViewport(1280f, 720f) }
    }
