package com.lateensoft.pathfinder.toolkit.model;

import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author tsiemens
 */
public class NamedList<E> extends ArrayList<E> implements Identifiable {

    private long id = UNSET_ID;
    private String name;

    public NamedList(String name) {
        super();
        id = UNSET_ID;
        this.name = name;
    }

    public NamedList(long id, String name, Collection<E> items) {
        super(items);
        this.id = id;
        this.name = name;
    }

    public NamedList(IdNamePair idNamePair, Collection<E> items) {
        this(idNamePair.getId(), idNamePair.getName(), items);
    }

    public NamedList(String name, Collection<E> items) {
        this(UNSET_ID, name, items);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public IdNamePair idNamePair() {
        return new IdNamePair(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamedList)) return false;
        if (!super.equals(o)) return false;

        NamedList namedList = (NamedList) o;

        if (id != namedList.id) return false;
        if (name != null ? !name.equals(namedList.name) : namedList.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
