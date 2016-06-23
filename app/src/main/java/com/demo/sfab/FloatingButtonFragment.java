package com.demo.sfab;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FloatingButtonFragment extends Fragment {

    // action bar
    private AppCompatActivity m_activity;
    private ActionBar m_actionbar;
    private String m_actionbar_title = "";
    private int m_actionbar_icon = 0;
    private int m_actionbar_bgColor = 0;

    private String[] m_sample_fragments = {"FAB1", "FAB2", "FAB3", "FAB4"};

    public FloatingButtonFragment() {
        // default constructor
        m_actionbar_title = this.getClass().getSimpleName();
        m_actionbar_icon = R.mipmap.ic_launcher;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_floating_button, container, false);
        m_actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        setActionBar();
        return rootView;
    }

    private void setActionBar() {
        m_actionbar_title = getTag();
        m_actionbar_icon = android.R.drawable.ic_menu_help;
        m_actionbar_bgColor = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);

        if (m_actionbar_title.equals(m_sample_fragments[0])) {
            m_actionbar_icon = android.R.drawable.ic_dialog_email;
            m_actionbar_bgColor = ContextCompat.getColor(getContext(), R.color.blue);
        } else if (m_actionbar_title.equals(m_sample_fragments[1])) {
            m_actionbar_icon = android.R.drawable.ic_dialog_map;
            m_actionbar_bgColor = ContextCompat.getColor(getContext(), R.color.cyan);
        } else if (m_actionbar_title.equals(m_sample_fragments[2])) {
            m_actionbar_icon = android.R.drawable.ic_dialog_info;
            m_actionbar_bgColor = ContextCompat.getColor(getContext(), R.color.green);
        } else if (m_actionbar_title.equals(m_sample_fragments[3])) {
            m_actionbar_icon = android.R.drawable.ic_dialog_dialer;
            m_actionbar_bgColor = ContextCompat.getColor(getContext(), R.color.yellow);
        }
        m_actionbar.setTitle(m_actionbar_title);
        m_actionbar.setIcon(m_actionbar_icon);
        ColorDrawable colorDrawable = new ColorDrawable(m_actionbar_bgColor);
        m_actionbar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            setActionBar();
    }

}
