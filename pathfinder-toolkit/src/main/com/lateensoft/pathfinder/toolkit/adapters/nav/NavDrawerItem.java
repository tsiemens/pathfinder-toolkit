package com.lateensoft.pathfinder.toolkit.adapters.nav;

import android.view.View;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author tsiemens
 */
public abstract class NavDrawerItem {
    private String m_text;
    private int m_iconResId;
    private int m_iconVisibility = View.VISIBLE;
    private Class<? extends BasePageFragment> m_fragmentClass = null;

    public NavDrawerItem(String text, int iconResId) {
        m_text = text;
        m_iconResId = iconResId;
    }

    public NavDrawerItem(String text, int iconResId, @Nullable Class<? extends BasePageFragment> fragment) {
        m_text = text;
        m_iconResId = iconResId;
        m_fragmentClass = fragment;
    }

    public NavDrawerItem(String text, @Nullable Class<? extends BasePageFragment> fragment) {
        this(text, 0, fragment);
    }

    public String getText() {
        return m_text;
    }

    public void setText(String text) {
        m_text = text;
    }

    public int getIconResId() {
        return m_iconResId;
    }

    public void setIconResId(int iconResId) {
        m_iconResId = iconResId;
    }

    public int getIconVisibility() {
        return m_iconVisibility;
    }

    public void setIconVisibility(int iconVisibility) {
        m_iconVisibility = iconVisibility;
    }

    public @Nullable Class<? extends BasePageFragment> getFragmentClass() {
        return m_fragmentClass;
    }

    public void setFragmentClass(@Nullable Class<? extends BasePageFragment> fragmentClass) {
        m_fragmentClass = fragmentClass;
    }
}
