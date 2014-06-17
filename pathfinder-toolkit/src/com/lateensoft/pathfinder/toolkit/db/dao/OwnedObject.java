package com.lateensoft.pathfinder.toolkit.db.dao;

public class OwnedObject<OwnerId, T> {
    private OwnerId ownerId;
    private T item;

    public static <OwnerId, T> OwnedObject<OwnerId, T> of(OwnerId ownerId, T item) {
        return new OwnedObject<OwnerId, T>(ownerId, item);
    }

    public OwnedObject(OwnerId ownerId, T item) {
        this.ownerId = ownerId;
        this.item = item;
    }

    public OwnerId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OwnerId ownerId) {
        this.ownerId = ownerId;
    }

    public T getObject() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
