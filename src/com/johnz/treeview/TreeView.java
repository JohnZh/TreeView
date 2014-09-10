package com.johnz.treeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

public class TreeView extends HorizontalScrollView {
    
    public static interface TreeViewCallback {
        public void onNodeClick(TreeViewNode<?> node);
        public void onNodeLongClick(TreeViewNode<?> node);
        
        /**
         * Called when tree view node is expanded or collapsed
         * @param node the expanded or collapsed node
         * @param flag true is expanded, false is collapsed
         */
        public void onNodeExpandedOrCollapsed(TreeViewNode<?> node, boolean flag);
    }
    
    private TreeViewAdapter<?> mTreeViewAdapter;

    private ListView mTreeViewList;
    private TreeViewCallback mCallback;
    
    private boolean mDottedVisible;
    private boolean mNodeClickExpand;
    
    public TreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    
    public void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_treeview, this, true);
        mTreeViewList = (ListView) findViewById(R.id.treeList);
        mDottedVisible = true;
        mNodeClickExpand = true;
    }
    
    public ListView getListView(){
        return mTreeViewList;
    }
    
    public TreeViewCallback getCallback() {
        return mCallback;
    }
    
    public void setTreeViewCallback(TreeViewCallback callback) {
        mCallback = callback;
    }
    
    public void setDottedLineVisible(boolean visible) {
        mDottedVisible = visible;
        if (mTreeViewAdapter != null) {
            mTreeViewAdapter.notifyDataSetChanged();
        }
    }
    
    public boolean isDottedLineVisible() {
        return mDottedVisible;
    }
    
    public void setNodeClickExpandEnable(boolean enable) {
        mNodeClickExpand = enable;
    }
    
    public boolean isNodeClickExpandEnable() {
        return mNodeClickExpand;
    }
    
    public void setAdapter(TreeViewAdapter<?> adapter) {
        if (adapter != null) {
            mTreeViewAdapter = adapter;
            mTreeViewAdapter.setTreeView(this);
            mTreeViewList.setAdapter(adapter);
        }
    }
    
    public void setTreeViewWidth(final int width) {
        if (mTreeViewList != null) {
            mTreeViewList.post(new Runnable() {
                
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = mTreeViewList.getLayoutParams();
                    params.width = width;
                    mTreeViewList.setLayoutParams(params);
                    mTreeViewList.requestLayout();
                }
            });
        }
    } 
}
