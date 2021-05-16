package database.models;

import enums.NotificationType;

import java.io.Serializable;

public class SystemNotification implements Serializable {
    private NotificationType notificationType;
    private String notification;

    public SystemNotification() {
    }

    public SystemNotification(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public SystemNotification(NotificationType notificationType, String notification) {
        this.notificationType = notificationType;
        this.notification = notification;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
