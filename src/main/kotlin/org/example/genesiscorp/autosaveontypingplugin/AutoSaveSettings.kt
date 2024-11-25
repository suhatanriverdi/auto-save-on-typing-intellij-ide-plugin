package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "AutoSaveOnTypingSettings", storages = [Storage("AutoSaveOnTypingSettings.xml")])
@Service
class AutoSaveSettings {

    private var delay: Int = 0 // Default delay of 0 seconds.

    fun getDelaySetting(): Int {
        return delay
    }

    fun setDelaySetting(newDelay: Int) {
        delay = newDelay
    }
}