package com.lateensoft.pathfinder.toolkit.dao;

public interface OwnedWeakGenericDAO<OwnerId, EntityId, Entity>
        extends OwnedGenericDAO<OwnerId, EntityId, Entity> {
    public Entity find(OwnerId ownerId, EntityId entityId);
    public boolean exists(OwnerId ownerId, EntityId entityId);
    public void removeById(OwnerId ownerId, EntityId entityId) throws DataAccessException;
    public void remove(OwnerId ownerId, Entity entity) throws DataAccessException;
}
