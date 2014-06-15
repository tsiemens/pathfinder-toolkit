package com.lateensoft.pathfinder.toolkit.dao;

import java.util.List;

public interface WeakGenericDAO<StrongId, WeakId, T>
        extends GenericDAO<WeakGenericDAO.IdPair<StrongId, WeakId>, T> {
    public List<T> findAll(StrongId strongId);
    public WeakId add(StrongId strongId, T entity) throws DataAccessException;
    public void update(StrongId strongId, T entity) throws DataAccessException;
    public void remove(StrongId strongId, T entity) throws DataAccessException;

    public static class IdPair<StrongId, WeakId> {
        private StrongId strongId;
        private WeakId weakId;

        public IdPair(StrongId strongId, WeakId weakId) {
            this.strongId = strongId;
            this.weakId = weakId;
        }

        public StrongId getStrongId() { return strongId; }

        public void setStrongId(StrongId strongId) { this.strongId = strongId; }

        public WeakId getWeakId() { return weakId; }

        public void setWeakId(WeakId weakId) { this.weakId = weakId; }
    }
}
