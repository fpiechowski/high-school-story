package pro.piechowski.highschoolstory.engine.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class CreateConnectionDialog(
    private val project: Project,
) : DialogWrapper(project) {
    private var name = "Game Server"
    private var url = "http://localhost:8080"

    init {
        init()
        title = "Create Connection"
    }

    override fun doOKAction() {
        InspectorSettings
            .getInstance()
            .state.servers
            .add(InspectorSettings.DebugServer("name", url))
    }

    override fun createCenterPanel(): JComponent =
        panel {
            row("Name:") {
                textField()
                    .bindText(::name)
                    .focused()
                    .align(AlignX.FILL)
            }
            row("URL:") {
                textField()
                    .bindText(::url)
                    .focused()
                    .align(AlignX.FILL)
            }
        }
}
