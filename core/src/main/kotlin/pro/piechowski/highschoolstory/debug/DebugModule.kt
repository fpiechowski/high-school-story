package pro.piechowski.highschoolstory.debug

import org.koin.dsl.module
import pro.piechowski.highschoolstory.debug.camera.DebugCameraControlInputProcessor
import pro.piechowski.highschoolstory.debug.highlight.DebugEntityHighlightInputProcessor
import pro.piechowski.highschoolstory.debug.highlight.DebugEntityHighlightManager
import pro.piechowski.highschoolstory.debug.highlight.DebugEntityHighlightRenderingSystem
import pro.piechowski.highschoolstory.debug.selection.DebugEntitySelectionManager
import pro.piechowski.highschoolstory.debug.selection.DebugSelectionIndicatorRenderingSystem
import pro.piechowski.highschoolstory.debug.selection.DebugSelectionInputProcessor
import pro.piechowski.highschoolstory.debug.ui.DebugUserInterface

val DebugModule =
    module {
        single { DebugEntitySelectionManager() }
        single { DebugSelectionInputProcessor() }
        single { DebugSelectionIndicatorRenderingSystem() }

        single { DebugEntityHighlightManager() }
        single { DebugEntityHighlightInputProcessor() }
        single { DebugEntityHighlightRenderingSystem() }

        single { DebugCameraControlInputProcessor() }

        single { DebugUserInterface() }
    }
