package com.example.recyclerviewdemo;

import android.view.View;

import java.util.Stack;

/**
 * @author ZhongXinyu
 * @作用 recyclerview回收池
 */

public class Recycler {
    //缓存itemView的集合
    private Stack<View>[] views;
    public Recycler(int itemTypeCount) {
        views = new Stack[itemTypeCount];
        for (int i = 0; i < itemTypeCount; i++) {
            views[i] = new Stack<>();
        }
    }

    public View getRecycledView(int type){
        try{
            return views[type].pop();
        }catch (Exception e){
            return null;
        }
    }

    public void addRecycledView(View view,int type){
        views[type].push(view);
    }
}
