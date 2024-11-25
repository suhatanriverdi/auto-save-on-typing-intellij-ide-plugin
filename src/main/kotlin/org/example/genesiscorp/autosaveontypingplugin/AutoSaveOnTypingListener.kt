package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.*
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile

class AutoSaveOnTypingListener : EditorFactoryListener {

    init {
        initializeListenersForExistingEditors()
    }

    override fun editorCreated(event: EditorFactoryEvent) {
        addListenerToDocument(event.editor.document)
    }

    private fun initializeListenersForExistingEditors() {
        val editorFactory = EditorFactory.getInstance()
        val editors = editorFactory.allEditors

        for (editor in editors) {
            addListenerToDocument(editor.document)
        }
    }

    private fun addListenerToDocument(document: com.intellij.openapi.editor.Document) {
        document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                autoSaveDocument(event.document)
            }
        })
    }
    private fun autoSaveDocument(document: com.intellij.openapi.editor.Document) {
        val virtualFile: VirtualFile? = try {
            FileDocumentManager.getInstance().getFile(document)
        } catch (e: Exception) {
            println("Error retrieving file: ${e.message}")
            null
        }

        if (virtualFile != null && virtualFile.isValid) {
            FileDocumentManager.getInstance().saveDocument(document)
        }
    }
}