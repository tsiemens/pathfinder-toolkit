package com.lateensoft.pathfinder.toolkit.adapters.nav;

import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author tsiemens
 */
public class NavDrawerGroupItem extends NavDrawerItem {
    private List<NavDrawerChildItem> m_children = null;

    public NavDrawerGroupItem(String text, int iconResId, List<NavDrawerChildItem> children) {
        super(text, iconResId);
        setChildren(children);
    }

    public NavDrawerGroupItem(String text, int iconResId, @Nullable Class<? extends BasePageFragment> fragment) {
        super(text, iconResId, fragment);
    }

    public List<NavDrawerChildItem> getChildren() {
        return m_children;
    }

    public void setChildren(List<NavDrawerChildItem> children) {
        if (children != null) {
            for (NavDrawerChildItem child : children) {
                child.setGroup(this);
            }
        }
        m_children = children;
    }
}
