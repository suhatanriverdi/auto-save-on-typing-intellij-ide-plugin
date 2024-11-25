package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.openapi.options.Configurable
import javax.swing.JPanel
import com.intellij.ui.components.JBSlider
import javax.swing.JLabel
import javax.swing.BoxLayout
import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.JBColor
import java.awt.Graphics
import java.awt.Dimension

class AutoSaveConfig : Configurable {

    // Slider from 0 to 10, with increments of 1 second (1000ms)
    private val delaySlider = JBSlider(0, 10, getDelay()).apply {
        // Set slider to move in steps of 1 second
        paintTicks = true
        paintLabels = true
        majorTickSpacing = 1  // Every 1 second for major tick
        minorTickSpacing = 1  // No minor ticks (since it's 1 second step)
        snapToTicks = true     // Ensure the slider snaps to ticks
    }
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

        // Add the custom panel to draw the scale (ruler) under the slider
        panel.add(createRulerPanel())

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

    // Create a custom panel to draw a ruler (scale) under the slider
    private fun createRulerPanel(): JPanel {
        return object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                // Set the color for the scale markings
                g.color = JBColor.GRAY
                val totalWidth = width
                val step = totalWidth / 10  // 10 steps from 0 to 10 seconds

                // Draw the scale and labels
                for (i in 0..10) {
                    // Calculate the x position for each tick mark
                    val xPos = i * step

                    // Draw the tick mark (a small vertical line)
                    g.drawLine(xPos, 0, xPos, 5)

                    // Draw the label under each tick mark
                    val label = (i).toString()
                    g.drawString(label, xPos - 5, 20) // Adjust the label to center it under the tick mark
                }
            }
        }.apply {
            preferredSize = Dimension(0, 30) // Set height to accommodate the scale and labels
        }
    }
}