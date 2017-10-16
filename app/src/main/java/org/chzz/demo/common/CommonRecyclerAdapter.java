package org.chzz.demo.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.chzz.adapter.CHZZRecyclerViewAdapter;
import org.chzz.demo.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by copy on 2017/4/15.
 */

public class CommonRecyclerAdapter extends CHZZRecyclerViewAdapter {
    List<BaseEntity> list=new ArrayList<>();
    private int[] itemLayout;
    private View header, foot;

    /**
     *   单布局
     * @param recyclerView
     * @param itemLayout int 单布局的Id
     * @param headView
     * @param footView
     * @param fillDataListener
     * @param itemChildListener
     */
    public CommonRecyclerAdapter(RecyclerView recyclerView, int itemLayout, View headView, View footView, IFillDataListener fillDataListener, ItemChildListener itemChildListener) {
        super(recyclerView, itemLayout, headView, footView, fillDataListener, itemChildListener);
        this.header = headView;
        this.foot = footView;
    }

    /**
     * 多布局
     * @param recyclerView
     * @param itemLayout  int[] 多布局的Id
     * @param headView   recyclerView的头部
     * @param footView   recyclerView的底部
     * @param fillDataListener   回调数据
     * @param itemChildListener   item  子控件点击回调
     */
    public CommonRecyclerAdapter(RecyclerView recyclerView, int[] itemLayout, View headView, View footView, IFillDataListener fillDataListener, ItemChildListener itemChildListener) {
        super(recyclerView, itemLayout, headView, footView, fillDataListener, itemChildListener);
        this.itemLayout = itemLayout;
        this.header = headView;
        this.foot = footView;
    }


    /**
     * 多布局
     * @param recyclerView
     * @param itemLayout  int[] 多布局的Id
     * @param headView   recyclerView的头部
     * @param footView   recyclerView的底部
     * @param fillDataListener   回调数据
     * @param itemChildListener   item  子控件点击回调
     */
    public CommonRecyclerAdapter(RecyclerView recyclerView, int[] itemLayout, View headView, View footView, IFillDataListener fillDataListener, ItemChildListener itemChildListener, boolean setIsRecyclable) {
        super(recyclerView, itemLayout, headView, footView, fillDataListener, itemChildListener,setIsRecyclable);
        this.itemLayout = itemLayout;
        this.header = headView;
        this.foot = footView;
    }

    @Override
    public int getItemViewType(int position) {
        //判断Item使用哪种布局  return 的int和item_layout Id对应。
        if (itemLayout != null && itemLayout.length > 0&&list!=null) {
            if (null != header && null != foot && position > 0 && position <= list.size()) {
                return itemLayout[list.get(position - 1).getItemFlag()];
            } else if (null != header && position > 0 && position <= list.size()) {
                return itemLayout[list.get(position - 1).getItemFlag()];
            } else if (null != foot && header == null && position < list.size()) {
                return itemLayout[list.get(position).getItemFlag()];
            } else if (null == foot && header == null) {
                return itemLayout[list.get(position).getItemFlag()];
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public void setData(List data) {
        list=new ArrayList<>();
        list.addAll(data);
        super.setData(data);
    }

    @Override
    public void addMoreData(List data) {
        list.addAll(data);
        super.addMoreData(data);
    }

}
