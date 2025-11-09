package pro.piechowski.highschoolstory.engine.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.platform.impl.toolkit.IdeToolkit
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.tabs.impl.JBTabsImpl
import com.intellij.ui.treeStructure.SimpleTree
import javax.swing.JPanel

class InspectorToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow,
    ) {
        val panel = ConnectionsPanel(project)

        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(panel.component, "", false)

        toolWindow.contentManager.addContent(content)
    }
}
