package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import java.util.Timer
import java.util.TimerTask

class AutoSaveOnTypingListener : EditorFactoryListener {

    private var saveTimer: Timer? = null

    override fun editorCreated(event: EditorFactoryEvent) {
        val document = event.editor.document
        document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                // Cancel the previous save task if it's still running
                saveTimer?.cancel()

                // Get the delay from settings
                // Convert to milliseconds
                val delayInSeconds = AutoSaveConfig().getDelay()

                // Schedule the auto-save
                saveTimer = Timer()
                saveTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        // Schedule the task to run on the EDT
                        ApplicationManager.getApplication().invokeLater {
                            autoSaveDocument(document)
                        }
                    }
                }, delayInSeconds * 1000L)
            }
        })
    }

    private fun autoSaveDocument(document: Document) {
        try {
            val virtualFile: VirtualFile? = FileDocumentManager.getInstance().getFile(document)
            if (virtualFile != null && virtualFile.isValid) {
                FileDocumentManager.getInstance().saveDocument(document)
                println("File auto-saved: ${virtualFile.path}")
            }
        } catch (e: Exception) {
            println("Error during auto-save: ${e.message}")
        }
    }
}