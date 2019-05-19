package com.object173.newsfeed.features.base.presentation;

public interface OnItemClickListener<V> {
    void onItemClick(V item);
    void onItemLongClick(V item);
}
