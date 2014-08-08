package com.johnz.treeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.HorizontalScrollView;

public class TreeView extends HorizontalScrollView {

    private TreeViewList mTreeViewList;
    
    public TreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    
    public void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_treeview, this, true);
        mTreeViewList = (TreeViewList) findViewById(R.id.treeList);
    }
    
    public void setAdapter(TreeViewAdapter adapter) {
        mTreeViewList.setTreeViewBuilder(adapter.getTreeViewBuilder());
        mTreeViewList.setAdapter(adapter);
    }
}
