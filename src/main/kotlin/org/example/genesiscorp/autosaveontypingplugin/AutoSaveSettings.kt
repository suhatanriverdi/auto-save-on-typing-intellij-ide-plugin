package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

@State(name = "AutoSaveOnTypingSettings", storages = [Storage("AutoSaveOnTypingSettings.xml")])
@Service
class AutoSaveSettings {
    private val pcs = PropertyChangeSupport(this)
    private var delay: Int = 0 // Default delay of 0 seconds.

    fun getDelaySetting(): Int {
        return delay
    }

    fun setDelaySetting(newDelay: Int) {
        delay = newDelay
    }

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        pcs.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        pcs.removePropertyChangeListener(listener)
    }
}