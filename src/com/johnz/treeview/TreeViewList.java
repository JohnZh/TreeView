package com.johnz.treeview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class TreeViewList extends ListView {

    private TreeViewBuilder mTreeViewBuilder;
    
    public TreeViewList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    
    private void initialize() {
        this.setDivider(null);
        this.setSelector(android.R.color.transparent);
        
    }

    public void setTreeViewBuilder(TreeViewBuilder treeViewBuilder) {
        mTreeViewBuilder = treeViewBuilder;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int singleOffset = getContext().getResources().getDimensionPixelSize(R.dimen.ic_expand_collapse_width);
        int extraOffset = getContext().getResources().getDimensionPixelSize(R.dimen.treeview_extra_offset);
        int offset = mTreeViewBuilder.getDeepestLevel() * singleOffset + extraOffset;
        setMeasuredDimension(offset + getMeasuredWidth(), getMeasuredHeight());
    }
}
