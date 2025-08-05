package pro.piechowski.highschoolstory.camera

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import org.koin.dsl.module
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.ui.userInterfaceViewportQualifier

val MainCameraModule =
    module {
        single<Camera>(pixelCameraQualifier) { OrthographicCamera() }
        single<OrthographicCamera> { get<Camera>(pixelCameraQualifier) as OrthographicCamera }
        single<Viewport>(pixelViewportQualifier) {
            FitViewport(1280f, 720f, get(pixelCameraQualifier))
        }

        single<Viewport>(userInterfaceViewportQualifier) { FitViewport(1280f, 720f) }
    }

val GameCameraModule =
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

        single { CameraMovementSystem() }
    }
