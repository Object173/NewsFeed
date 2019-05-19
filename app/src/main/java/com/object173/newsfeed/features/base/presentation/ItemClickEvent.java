package com.object173.newsfeed.features.base.presentation;

public class ItemClickEvent<V> {

    public enum Type {
        CLICK,
        LONG_CLICK
    }

    public final Type mType;
    public final V mItem;

    ItemClickEvent(Type type, V item) {
        mType = type;
        mItem = item;
    }
}
