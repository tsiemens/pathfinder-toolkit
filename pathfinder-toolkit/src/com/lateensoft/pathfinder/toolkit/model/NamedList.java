package com.lateensoft.pathfinder.toolkit.model;

import com.lateensoft.pathfinder.toolkit.db.repository.Storable;

import java.util.ArrayList;

/**
 * @author tsiemens
 */
public class NamedList<E> extends ArrayList<E> implements Storable {

    private long m_id;
    private String m_name;

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    @Override
    public void setID(long id) {
        m_id = id;
    }

    @Override
    public long getID() {
        return m_id;
    }
}
