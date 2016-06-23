package com.demo.sfab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FloatingButton extends LinearLayout {

    public interface FloatingButtonClickListener {
        void onFabClick(String tag);
    }

    private enum FAB_TYPE {
        NORMAL,
        MINI
    }

    private String mPackageName = "";
    private TextView mFabLabel;
    private FloatingButton mRootFabView;
    private FloatingActionButton mFabMainView;
    private FloatingButtonClickListener mClickListener;
    private Animation mAnimFabOpen, mAnimFabClose;
    public int miFabType, miFabBgColor, miFabIcon;
    private ArrayList<FloatingButton> mSubFabGroup = new ArrayList<>();


    public FloatingButton(Context context) {
        super(context);
    }

    public FloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        // get application's package name for getResourId() in later few steps.
        mPackageName = context.getPackageName();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.fab,
                0, 0);

        try {
            // get label text and floating button attribute
            String label_text = typedArray.getString(R.styleable.fab_labelText);
            int i_lbl_bg_color = typedArray.getInt(R.styleable.fab_labelBackgroundColor, R.color.fab_lbl_default_color);
            miFabType = typedArray.getInt(R.styleable.fab_fabType, 0);
            miFabBgColor = typedArray.getColor(R.styleable.fab_fabBackgroundColor, ContextCompat.getColor(context, R.color.fab_bg_default_color));
            miFabIcon = typedArray.getResourceId(R.styleable.fab_fabIconSrc, 0);


            float density = getResources().getDisplayMetrics().density;
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.weight = 1.0f;
            params.gravity = Gravity.CENTER_VERTICAL;
            int fab_margin = (int)(8 *density);


            // Floating Button Label
            mFabLabel = new TextView(context);
            mFabLabel.setLayoutParams(params);
            int text_horizontal_padding = (int)(4 * density);
            int text_vertical_padding = (int)(2 * density);
            mFabLabel.setPadding(text_horizontal_padding, text_vertical_padding, text_horizontal_padding, text_vertical_padding);
            if (null == label_text) {
                label_text = "";
                mFabLabel.setVisibility(View.INVISIBLE);
            }
            mFabLabel.setText(label_text);
            mFabLabel.setBackgroundColor(i_lbl_bg_color);

            // floating button
            int fab_layout = getResourceId((miFabType == FAB_TYPE.NORMAL.ordinal()) ?
                                                "fab_normal_style" : "fab_mini_style", "layout");
            if (fab_layout != 0) {
                View view;
                if (miFabType == FAB_TYPE.NORMAL.ordinal()) {
                    view = LayoutInflater.from(context).inflate(R.layout.fab_normal_style, null);
                    mFabMainView = (FloatingActionButton)view.findViewById(R.id.fab_normal);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        params.setMargins(fab_margin, fab_margin*2, fab_margin*2, fab_margin*2);
                    else
                        params.setMargins(0, 0, 0, 0);
                }
                else {
                    view = LayoutInflater.from(context).inflate(R.layout.fab_mini_style, null);
                    mFabMainView = (FloatingActionButton)view.findViewById(R.id.fab_mini);
                    params.setMargins(fab_margin, fab_margin, fab_margin*2, fab_margin);
                    this.setVisibility(View.GONE);
                }
            } else { // can not find any style xml in resources, give a default floating button to create.
                mFabMainView = new FloatingActionButton(context);
            }
            mFabMainView.setLayoutParams(params);
            mFabMainView.setBackgroundTintList(ColorStateList.valueOf(miFabBgColor));
            if (miFabIcon != 0)
                mFabMainView.setImageResource(miFabIcon);

            this.addView(mFabLabel);
            this.addView(mFabMainView);

            mAnimFabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_jump_up);
            mAnimFabClose = AnimationUtils.loadAnimation(context,R.anim.fab_jump_down);

            this.setOnClickListener(onFabClickListener);

        } finally {
            typedArray.recycle();
        }
    }

    private OnClickListener onFabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mSubFabGroup)
                return;

            for (FloatingButton fab : mSubFabGroup) {
                // Change root fab background color and icon by Current chosen sub fab.
                if (fab.getId() == v.getId())
                    changeRootFabStyle(fab.miFabBgColor, fab.miFabIcon);

                // demonstrate open/close animation as button click is active.
                if (fab.getVisibility() == View.VISIBLE) {
                    fab.startAnimation(mAnimFabClose);
                    fab.setVisibility(View.INVISIBLE);
                } else {
                    fab.startAnimation(mAnimFabOpen);
                    fab.setVisibility(View.VISIBLE);
                }
            }

            if (null != mClickListener)
                mClickListener.onFabClick((String)v.getTag());
        }
    };

    public void setClickListener(FloatingButtonClickListener listener) {
        mClickListener = listener;
    }

    public void setFabName(String name) {
        this.setTag(name);
    }

    public String getmFabName() {
        return (String)getTag();
    }

    public void setSubFab(FloatingButton fab) {
        if (miFabType == FAB_TYPE.NORMAL.ordinal()) {
            // give new sub fab a root fab and add itself to sub group list.
            fab.mRootFabView = this;
            fab.changeRootFabStyle(fab.miFabBgColor, fab.miFabIcon);
            fab.setSubFab(fab);
        }

        if (!mSubFabGroup.contains(fab)) {
            if (fab != mRootFabView)
                // add new sub fab to each sub fab in current sub group list.
                mSubFabGroup.add(fab);

            if (miFabType == FAB_TYPE.MINI.ordinal() && fab != this) {
                // add each current sub fabs to new sub fab's group list but without root fab and new fab.
                fab.setSubFab(this);
            }
        }
    }

    public void setSubFabGroup(ArrayList<FloatingButton> fabGroup) {
        mSubFabGroup = fabGroup;
        if (miFabType == FAB_TYPE.NORMAL.ordinal()) {
            for (FloatingButton fab : fabGroup) {
                fab.setSubFabGroup(fabGroup);
            }
        }
    }

    private void changeRootFabStyle(int fabBgColor, int fabIcon) {
        mRootFabView.mFabMainView.setBackgroundTintList(ColorStateList.valueOf(fabBgColor));
        mRootFabView.mFabMainView.setImageResource(fabIcon);
    }

    private int getResourceId(String resName, String resType) {
        int retValue = 0;
        if (null != resName && null != resType) {
            retValue = getResources().getIdentifier(resName, resType, mPackageName);
        }
        return retValue;
    }

}
