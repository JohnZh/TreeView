package com.johnz.treeview;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TreeViewAdapter extends BaseAdapter {
    
    private Context mContext;
    private TreeViewBuilder mTreeViewBuilder;
    private List<TreeViewNode> mDisplayedNodes;
    
    private boolean mDotted;
    
    public TreeViewAdapter(Context context, TreeViewBuilder treeViewBuilder) {
        mContext = context;
        mTreeViewBuilder = treeViewBuilder;
        mDisplayedNodes = mTreeViewBuilder.getDisplayedNodes();
        mDotted = true;
    }
    
    public TreeViewAdapter(Context context, TreeViewBuilder treeViewBuilder, boolean dotted) {
        mContext = context;
        mTreeViewBuilder = treeViewBuilder;
        mDisplayedNodes = mTreeViewBuilder.getDisplayedNodes();
        mDotted = dotted;
    }

    @Override
    public int getCount() {
        return mDisplayedNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mDisplayedNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.wrapper_treeview_node, null);
            viewHolder.dottedBlock = (LinearLayout) convertView.findViewById(R.id.dotted_block);
            viewHolder.icExpandCollapse = (ImageView) convertView.findViewById(R.id.ic_expand_collapse);
            viewHolder.contentWrapper = (LinearLayout) convertView.findViewById(R.id.wrapper_content);
            convertView.setTag(viewHolder);
        }
        TreeViewNode node = (TreeViewNode) getItem(position);
        setupTreeView(viewHolder, node);
        
        return convertView;
    }
    
    private void setupTreeView(ViewHolder viewHolder, final TreeViewNode node) {
        setupDottedBlockView(viewHolder, node);
        setupExpandCollapseView(viewHolder, node);
    }

    private void setupDottedBlockView(ViewHolder viewHolder, TreeViewNode node) {
        LayoutParams layoutParams = viewHolder.dottedBlock.getLayoutParams();
        int singleOffset = mContext.getResources().getDimensionPixelSize(R.dimen.ic_expand_collapse_width);
        int nodeOffset = (node.getLevel() + (node.isLeaf()? 1: 0)) * singleOffset;
        layoutParams.width = nodeOffset;
        viewHolder.dottedBlock.setLayoutParams(layoutParams);
        if (mDotted && nodeOffset > 0) { // Root not need dotted line
            Bitmap dottedLineBackground = createDottedLineBackground();
            
        } else {
            viewHolder.dottedBlock.setBackgroundColor(Color.TRANSPARENT);
        }
    }
    
    private Bitmap createDottedLineBackground() {
        
        return null;
    }

    private void setupExpandCollapseView(ViewHolder viewHolder, final TreeViewNode node) {
        if (node.isLeaf()) {
            viewHolder.icExpandCollapse.setVisibility(View.GONE);
        } else {
            viewHolder.icExpandCollapse.setVisibility(View.VISIBLE);
            viewHolder.icExpandCollapse.setImageResource(node.isExpand()? R.drawable.ic_expand: R.drawable.ic_collapse);
        }
        viewHolder.icExpandCollapse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                node.setExpand(!node.isExpand());
                mTreeViewBuilder.updateDisplayedNodes();
                mDisplayedNodes = mTreeViewBuilder.getDisplayedNodes();
                notifyDataSetChanged();
            }
        });
    }

    private class ViewHolder {
        public LinearLayout dottedBlock;
        public ImageView icExpandCollapse;
        public LinearLayout contentWrapper;
    }
}
