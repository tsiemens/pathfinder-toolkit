package com.lateensoft.pathfinder.toolkit.adapters.nav;

import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import org.jetbrains.annotations.Nullable;

/**
 * @author tsiemens
 */
public class NavDrawerChildItem extends NavDrawerItem {
    private NavDrawerGroupItem m_group;

    public NavDrawerChildItem(String text, int iconResId, @Nullable Class<? extends BasePageFragment> fragment) {
        super(text, iconResId, fragment);
    }

    protected void setGroup(NavDrawerGroupItem group) {
        m_group = group;
    }

    public NavDrawerGroupItem getGroup() {
        return m_group;
    }
}
