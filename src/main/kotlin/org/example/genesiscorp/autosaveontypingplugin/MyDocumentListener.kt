package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.openapi.editor.event.*
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile

class MyDocumentListener : EditorFactoryListener {

    override fun editorCreated(event: EditorFactoryEvent) {
        event.editor.document.addDocumentListener(object : DocumentListener {
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