package org.chzz.demo.view.fragment;

import android.os.Bundle;
import android.view.View;

import org.chzz.demo.R;
import org.chzz.demo.common.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by copy on 2017/10/12.
 */

public class SecondFragment extends BaseFragment{

    @Override
    protected void initView(Bundle savedInstanceState) {

        mContentView = View.inflate(getActivity(), R.layout.fragment_second, null);
        ButterKnife.bind(this, mContentView);
    }

    @Override
    protected void setListener() {

    }

}
