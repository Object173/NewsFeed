package com.object173.newsfeed.features.settings.device.model;

public class NotificationConfig {

    public final boolean enabled;
    public final Type type;

    public NotificationConfig(boolean enabled, Type type) {
        this.enabled = enabled;
        this.type = type;
    }

    public enum Type {
        NONE,
        VIBRATION_ONLY,
        SOUND_ONLY,
        SOUND_AND_VIBRATION
    }
}
