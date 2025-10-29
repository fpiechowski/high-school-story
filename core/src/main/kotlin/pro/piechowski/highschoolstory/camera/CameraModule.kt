package pro.piechowski.highschoolstory.camera

import org.koin.dsl.module
import pro.piechowski.highschoolstory.ui.UserInterfaceViewport

val MainCameraModule =
    module {
        single { CameraManager() }

        single<UserInterfaceViewport> { UserInterfaceViewport() }
    }

val GameCameraModule =
    module {
        single<MeterCamera> { MeterCamera() }
        single<MeterViewport> { MeterViewport(get()) }

        single<CameraSet> { CameraSet(get()) }

        single { CameraFollowingPlayerCharacterSystem() }
    }
