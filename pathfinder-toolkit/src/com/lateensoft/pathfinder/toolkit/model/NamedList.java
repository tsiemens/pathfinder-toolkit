package com.lateensoft.pathfinder.toolkit.model;

import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author tsiemens
 */
public class NamedList<E> extends ArrayList<E> implements Identifiable {

    private long m_id = UNSET_ID;
    private String m_name;

    public NamedList(String name) {
        super();
        m_id = UNSET_ID;
        m_name = name;
    }

    public NamedList(long id, String name, Collection<E> items) {
        super(items);
        m_id = id;
        m_name = name;
    }

    public NamedList(String name, Collection<E> items) {
        this(UNSET_ID, name, items);
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    @Override
    public void setId(long id) {
        m_id = id;
    }

    @Override
    public long getId() {
        return m_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamedList)) return false;
        if (!super.equals(o)) return false;

        NamedList namedList = (NamedList) o;

        if (m_id != namedList.m_id) return false;
        if (m_name != null ? !m_name.equals(namedList.m_name) : namedList.m_name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (m_id ^ (m_id >>> 32));
        result = 31 * result + (m_name != null ? m_name.hashCode() : 0);
        return result;
    }
}
