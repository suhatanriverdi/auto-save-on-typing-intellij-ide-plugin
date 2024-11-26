package org.example.genesiscorp.autosaveontypingplugin

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
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

    private val processingDocuments = mutableSetOf<com.intellij.openapi.editor.Document>()

    private fun addListenerToDocument(document: com.intellij.openapi.editor.Document) {
        document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                // If processing, do not repeat
                if (processingDocuments.contains(event.document)) {
                    return
                }

                try {
                    // Add to processing set
                    processingDocuments.add(event.document)
                    ApplicationManager.getApplication().invokeLater {
                        ApplicationManager.getApplication().runWriteAction {
                            autoSaveDocument(event.document)
                        }
                    }
                } catch (e: Exception) {
                    showNotification(
                        "Error during document change processing: ${e.message}",
                        NotificationType.ERROR
                    )
                } finally {
                    // Remove from the set
                    processingDocuments.remove(event.document)
                }
            }
        })
    }

    private fun autoSaveDocument(document: com.intellij.openapi.editor.Document) {
        try {
            val virtualFile: VirtualFile? = FileDocumentManager.getInstance().getFile(document)

            if (virtualFile != null && virtualFile.isValid) {
                FileDocumentManager.getInstance().saveDocument(document)
            } else {
                showNotification(
                    "Failed to save file: File is either null or invalid.",
                    NotificationType.WARNING
                )
            }
        } catch (e: Exception) {
            showNotification("Error saving document: ${e.message}", NotificationType.ERROR)
        }
    }

    private fun showNotification(content: String, type: NotificationType) {
        val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("Auto Save Notifications")
        val notification = notificationGroup.createNotification(content, type)
        notification.notify(null)
    }
}