package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.openapi.options.Configurable
import javax.swing.JPanel
import com.intellij.ui.components.JBTextField
import javax.swing.JLabel
import javax.swing.BoxLayout
import com.intellij.openapi.application.ApplicationManager

class AutoSaveConfig : Configurable {

    private val delayField = JBTextField()

    override fun getDisplayName(): String {
        return "Auto Save On Typing"
    }

    override fun createComponent(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        // Delay input field
        val label = JLabel("Delay (in seconds):")
        panel.add(label)
        panel.add(delayField)

        // Set initial value from settings
        delayField.text = getDelay().toString()

        return panel
    }

    override fun isModified(): Boolean {
        return delayField.text != getDelay().toString()
    }

    override fun apply() {
        val delay = delayField.text.toInt()
        setDelay(delay)
    }

    override fun reset() {
        delayField.text = getDelay().toString()
    }

    fun getDelay(): Int {
        val app = ApplicationManager.getApplication()
        val settings = app.getService(AutoSaveSettings::class.java)
        return settings.getDelaySetting()
    }

    private fun setDelay(delay: Int) {
        val app = ApplicationManager.getApplication()
        val settings = app.getService(AutoSaveSettings::class.java)
        settings.setDelaySetting(delay)
    }
}