package com.johnz.treeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
    
    private TreeViewAdapter mTreeViewAdapter;

    private ListView mTreeViewList;
    private TreeViewCallback mCallback;
    
    private boolean mDottedVisible;
    private boolean mNodeClickExpand;
    private int mMaxWidth;
    
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
    
    public void setAdapter(TreeViewAdapter adapter) {
        mTreeViewAdapter = adapter;
        mTreeViewAdapter.setTreeView(this);
        mTreeViewList.setAdapter(adapter);
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < mTreeViewAdapter.getCount(); i++) {
            View child = mTreeViewAdapter.getView(i, null, mTreeViewList);
            child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            if (mMaxWidth < child.getMeasuredWidth()) {
                mMaxWidth = child.getMeasuredWidth();
            }
            ViewGroup.LayoutParams params = mTreeViewList.getLayoutParams();
            params.width = mMaxWidth;
            mTreeViewList.setLayoutParams(params);
            mTreeViewList.requestLayout();
        }
    }
}
