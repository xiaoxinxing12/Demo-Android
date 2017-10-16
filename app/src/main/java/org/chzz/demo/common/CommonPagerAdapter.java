package org.chzz.demo.common;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.chzz.demo.model.TitleEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by copy on 2017/4/19.
 */

public class CommonPagerAdapter extends FragmentStatePagerAdapter {
    private List<TitleEntity> list;
    private Context mContext;
    private Class[] mClass;
    private int flag;
    private List<Fragment> mFragmentList=new ArrayList<>();
    public Fragment getFragmentListItem(int i) {
        return mFragmentList == null ? null : mFragmentList.get(i);
    }

    public CommonPagerAdapter(FragmentManager fm, Context context, List<TitleEntity> list, int flag, Class[] classe) {
        super(fm);
        this.list = list;
        this.mContext = context;
        this.flag = flag;
        this.mClass = classe;
    }

    @Override
    public Fragment getItem(int position) {
        String name;
        Bundle args = new Bundle();
        try {
            name = mClass[position].getName();

        } catch (Exception e) {
            name = mClass[0].getName();
        }
        args.putInt("flag", flag);
        Fragment fragment = Fragment.instantiate(mContext, name, args);
        mFragmentList.add(fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }
}