package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

class AutoSaveOnTypingListener : EditorFactoryListener {

    // CoroutineScope for managing auto-save tasks
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val documentJobs = ConcurrentHashMap<Document, Job>() // Track jobs for each document

    override fun editorCreated(event: EditorFactoryEvent) {
        val document = event.editor.document
        document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                // Cancel any existing job for this document
                documentJobs[document]?.cancel()

                // Start a new coroutine for saving this document
                val delay = AutoSaveConfig().getDelay() * 1000L

                val job = scope.launch {
                    if (delay > 0) {
                        delay(delay.toLong()) // Wait for the delay period
                    }
                    saveDocument(document)
                }
                documentJobs[document] = job
            }
        })
    }

    private suspend fun saveDocument(document: Document) {
        try {
            val virtualFile: VirtualFile? = FileDocumentManager.getInstance().getFile(document)
            if (virtualFile != null && virtualFile.isValid) {
                // Switch to EDT to interact with IntelliJ APIs
                withContext(Dispatchers.Main) {
                    FileDocumentManager.getInstance().saveDocument(document)
                }
            }
        } catch (e: Exception) {
            println("Error during auto-save: ${e.message}")
        }
    }
}