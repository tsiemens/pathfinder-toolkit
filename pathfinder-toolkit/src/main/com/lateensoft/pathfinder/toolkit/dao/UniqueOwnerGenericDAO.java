package com.lateensoft.pathfinder.toolkit.dao;

public interface UniqueOwnerGenericDAO<OwnerId, Entity>
        extends OwnedGenericDAO<OwnerId, Void, Entity> {
    public Entity find(OwnerId ownerId);
    public boolean exists(OwnerId ownerId);
    public void removeById(OwnerId ownerId) throws DataAccessException;
}
