package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.openapi.options.Configurable
import javax.swing.JPanel
import com.intellij.ui.components.JBSlider
import javax.swing.JLabel
import javax.swing.BoxLayout
import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.JBColor
import java.awt.Graphics

class AutoSaveConfig : Configurable {

    // Slider from 0 to 60 with initial value set from settings
    private val delaySlider = JBSlider(0, 60, getDelay())
    private val delayLabel = JLabel("Delay (in seconds): ${delaySlider.value}")

    override fun getDisplayName(): String {
        return "Auto Save On Typing"
    }

    override fun createComponent(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        // Slider and label for delay
        delaySlider.addChangeListener {
            delayLabel.text = "Delay (in seconds): ${delaySlider.value}"
        }

        panel.add(delayLabel)
        panel.add(delaySlider)

        // Add the custom panel to draw the line under the slider
        panel.add(createLinePanel())

        return panel
    }

    override fun isModified(): Boolean {
        return delaySlider.value != getDelay()
    }

    override fun apply() {
        val delay = delaySlider.value
        setDelay(delay)
    }

    override fun reset() {
        delaySlider.value = getDelay()
        delayLabel.text = "Delay (in seconds): ${delaySlider.value}"
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


    // Create a custom panel to draw a line
    private fun createLinePanel(): JPanel {
        return object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                // Set the color for the line
                g.color = JBColor.GRAY
                // Draw a dashed line (or solid if you prefer)
                val dash = floatArrayOf(5.0f) // Dash pattern
                val g2d = g.create() as java.awt.Graphics2D
                g2d.stroke = java.awt.BasicStroke(2.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0f, dash, 0f)
                g2d.drawLine(0, 0, width, 0) // Draw horizontal line
            }
        }.apply {
            preferredSize = java.awt.Dimension(0, 1) // Set the height to 1 for a thin line
        }
    }
}