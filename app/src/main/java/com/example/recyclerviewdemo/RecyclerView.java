package com.example.recyclerviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhongXinyu
 * @作用 学习recyclerview用 and ViewGroup重写
 */
public class RecyclerView extends ViewGroup {
    boolean needRelayout;//是否需要重新布局
    List<View> viewList;
    private Adapter mAdapter;
    private int rowCount;
    private int[] heights;
    private int height;
    private int width;
    private Recycler recycler;
    private int touchSlop;
    private float currentY;
    private int scrollY;
    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        needRelayout = true;
        viewList = new ArrayList<>();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        touchSlop = viewConfiguration.getScaledTouchSlop();
    }

    public void setAdapter(Adapter adapter){
        if(adapter != null){
            needRelayout = true;
            mAdapter = adapter;
            recycler = new Recycler(adapter.getItemTypeCount());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                currentY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y2 = (int) Math.abs(ev.getRawY() - currentY);
                if(y2 > touchSlop){
                    intercept = true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                int y2 = (int) event.getRawY();
                int diff = (int) (currentY - y2);
                scrollBy(0,diff);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollBy(int x, int diff) {
        scrollY += diff;
        super.scrollBy(x, diff);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(needRelayout || changed){
            needRelayout = false;
            //初始化
            viewList.clear();
            removeAllViews();
            if(mAdapter != null){
                rowCount = mAdapter.getCount();
                heights = new int[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    heights[i] += mAdapter.getHeight(i);
                }
                width = r - l;
                height = b - t;
                int top = 0,bottom = 0;
                for (int i = 0; i < rowCount && top < height; i++) {
                    bottom = top + heights[i];

                    //填充子view
                    View view = null;
                    try {
                        view = makeAndSetup(i,0,top,width,bottom);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    addView(view);
                    viewList.add(view);
                    top = bottom;
                }
            }

        }
    }

    private View makeAndSetup(int index, int left, int top, int right, int bottom) throws Exception {
        View view = obtain(index,right - left,bottom - top);
        view.layout(left,top,right,bottom);
        return view;
    }

    private View obtain(int row, int width, int height) throws Exception {
        //回收池逻辑
        int type = mAdapter.getItemViewType(row);
        View recycledView = recycler.getRecycledView(type);
        View view;
        if(recycledView == null){
            view = mAdapter.onCreateViewHolder(row,null,this);
            if(view == null){
                throw new RuntimeException("需要重写onCreateViewHolder");
            }
        }else{
            view = recycledView;
        }
        //将type值保存在view中
        view.setTag(123,type);
        view.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
        addView(view,0);
        return mAdapter.onBindViewHolder(row,view,this);
    }

    //存入回收池
    @Override
    public void removeView(View view) {
        super.removeView(view);
        int type = (int) view.getTag(123);
        recycler.addRecycledView(view,type);
    }

    interface Adapter{
        View onCreateViewHolder(int position,View convertView,ViewGroup parent);
        View onBindViewHolder(int position,View convertView,ViewGroup parent);
        //item
        int getItemViewType(int row);
        int getItemTypeCount();
        int getCount();
        int getHeight(int index);
    }
}
