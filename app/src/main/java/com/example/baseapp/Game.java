package com.example.baseapp;

public class Game {
    private String title;
    private String description;
    private int iconResId;
    private Class<?> activityClass;

    public Game(String title, String description, int iconResId, Class<?> activityClass) {
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
        this.activityClass = activityClass;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }
}
