package pro.piechowski.highschoolstory.camera

import org.koin.dsl.module
import pro.piechowski.highschoolstory.ui.UserInterfaceViewport

val MainCameraModule =
    module {
        single<PixelCamera> { PixelCamera() }
        single<PixelViewport> { PixelViewport(get()) }

        single<UserInterfaceViewport> { UserInterfaceViewport() }
    }

val GameCameraModule =
    module {
        single<MeterCamera> { MeterCamera() }
        single<MeterViewport> { MeterViewport(get()) }

        single<CameraSet> { CameraSet(get(), get()) }

        single { CameraMovementSystem() }
    }
