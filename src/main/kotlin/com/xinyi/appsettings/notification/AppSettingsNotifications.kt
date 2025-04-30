package com.xinyi.appsettings.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.util.NlsContexts

object AppSettingsNotifications {
    private const val CHANNEL: String = "AppSettings notifications"

    @Suppress("unused")
    @JvmStatic
    fun showSimple(
        @NlsContexts.NotificationContent content: String,
        notification: NotificationType = NotificationType.INFORMATION
    ) {
        val notification = createNotification("AppSettings notifications", content, notification)
        Notifications.Bus.notify(notification)
    }


    @JvmStatic
    fun createNotification(
        @NlsContexts.NotificationTitle title: String,
        @NlsContexts.NotificationContent content: String,
        notification: NotificationType = NotificationType.INFORMATION
    ): Notification {
        val group = NotificationGroupManager.getInstance().getNotificationGroup(CHANNEL)
        return group.createNotification(title, content, notification)
    }
}