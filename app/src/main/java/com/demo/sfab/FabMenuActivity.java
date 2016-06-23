package com.demo.sfab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.HashMap;


public class FabMenuActivity extends AppCompatActivity {
    private static final String TAG = "FabMenuActivity";

    private Toolbar m_actionBar;
    private FloatingButton fab_base, fab_1st, fab_2nd, fab_3rd, fab_4th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        MakeActionBar();
        // create fabs
        MakeView();
    }

    private void MakeActionBar() {
        // retrieve AppcompatActivity from getActivity for setting supportActionBar
        m_actionBar = (Toolbar)findViewById(R.id.tl_custom);
        setSupportActionBar(m_actionBar);
    }

    private void MakeView() {
        fab_base = (FloatingButton)findViewById(R.id.fabbase);
        fab_1st = (FloatingButton)findViewById(R.id.fab1);
        fab_2nd = (FloatingButton)findViewById(R.id.fab2);
        fab_3rd = (FloatingButton)findViewById(R.id.fab3);
        fab_4th = (FloatingButton)findViewById(R.id.fab4);

        FloatingButtonGroup fabGroup = new FloatingButtonGroup(getSupportFragmentManager());
        fabGroup.setRootFloatingButton(fab_base);
        fabGroup.addFloatingButtonToSubGroup("FAB1", fab_1st, new FloatingButtonFragment());
        fabGroup.addFloatingButtonToSubGroup("FAB2", fab_2nd, new FloatingButtonFragment());
        fabGroup.addFloatingButtonToSubGroup("FAB3", fab_3rd, new FloatingButtonFragment());
        fabGroup.addFloatingButtonToSubGroup("FAB4", fab_4th, new FloatingButtonFragment());

    }


    public class FloatingButtonGroup implements FloatingButton.FloatingButtonClickListener {

        private FragmentManager mMappingFragmentsManager = null;
        private FloatingButton mRootFloatingButton = null;
        private Fragment mCurrentFragment = null;
        private HashMap<String, FloatingButton> mSubGroupFloatingButtons = null;
        private HashMap<String, Fragment> mSubGroupMappingFragments = null;

        public FloatingButtonGroup(FragmentManager fgmntMgr) {
            mMappingFragmentsManager = fgmntMgr;
            mSubGroupFloatingButtons = new HashMap<>();
            mSubGroupMappingFragments = new HashMap<>();
        }

        public void setRootFloatingButton(FloatingButton fltbtn) {
            mRootFloatingButton = fltbtn;
        }

        public void addFloatingButtonToSubGroup(String tag, FloatingButton subFltbtn,
                                                Fragment mappingFgmnt) {
            if (null == subFltbtn || subFltbtn == mRootFloatingButton)
                return;
            else {
                subFltbtn.setFabName(tag);
                subFltbtn.setClickListener(this);
                // add new member fab to fab group list.
                if (null != mRootFloatingButton)
                    mRootFloatingButton.setSubFab(subFltbtn);
                // update each member fab to group list.
                for (String key : mSubGroupFloatingButtons.keySet())
                    mSubGroupFloatingButtons.get(key).setSubFab(subFltbtn);
                // update fabs list.
                mSubGroupFloatingButtons.put(tag, subFltbtn);

                // update current mapping fragments list.
                if (null != mappingFgmnt) {
                    mSubGroupMappingFragments.put(tag, mappingFgmnt);
                    mMappingFragmentsManager.beginTransaction().add(mappingFgmnt, tag).commit();
                    if (null != mCurrentFragment)
                        mMappingFragmentsManager.beginTransaction().hide(mCurrentFragment).commit();
                    mCurrentFragment = mappingFgmnt;
                }
            }
        }

        public FloatingButton getFloatingButtonFromGroup(String tag) {
            FloatingButton btn = mSubGroupFloatingButtons.get(tag);
            return btn;
        }

        public Fragment getFragmentFromGroup(String tag) {
            Fragment fgmnt = mSubGroupMappingFragments.get(tag);
            return fgmnt;
        }

        private void setCurrentFragment(String tag) {
            Fragment nextFragment = mSubGroupMappingFragments.get(tag);
            if (null == mCurrentFragment || null == nextFragment)
                return;
            if (mCurrentFragment == nextFragment)
                return;

            mMappingFragmentsManager.beginTransaction().hide(mCurrentFragment).show(nextFragment).commit();
            mCurrentFragment = nextFragment;
        }

        @Override
        public void onFabClick(String tag) {
            setCurrentFragment(tag);
        }
    }
}
