package com.johnz.treeview;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public abstract class TreeViewAdapter extends BaseAdapter {
    
    public abstract View getTreeNodeView(TreeViewNode node);
    
    public static interface TreeViewCallback {
        public void onNodeClick(TreeViewNode node);
        public void onNodeLongClick(TreeViewNode node);
        
        /**
         * Called when tree view node is expanded or collapsed
         * @param node the expanded or collapsed node
         * @param flag true is expanded, false is collapsed
         */
        public void onNodeExpandedOrCollapsed(TreeViewNode node, boolean flag);
    }
    
    private TreeViewCallback mCallback;
    
    public void setTreeViewCallback(TreeViewCallback callback) {
        mCallback = callback;
    }
    
    private Context mContext;
    private TreeViewBuilder mTreeViewBuilder;
    private List<TreeViewNode> mDisplayedNodes;
    
    private boolean mDotted;
    private boolean mNodeClickExpand;
    
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
    
    public void setDottedLineVisible(boolean visible) {
        mDotted = visible;
        notifyDataSetChanged();
    }
    
    public void setNodeClickExpandEnable(boolean enable) {
        mNodeClickExpand = enable;
    }
    
    public TreeViewBuilder getTreeViewBuilder() {
        return mTreeViewBuilder;
    }
    
    public Context getContext() {
        return mContext;
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
            viewHolder.contentWrapper = (FrameLayout) convertView.findViewById(R.id.wrapper_content);
            convertView.setTag(viewHolder);
        }
        TreeViewNode node = (TreeViewNode) getItem(position);
        setupTreeView(viewHolder, node);
        setupTreeViewNode(viewHolder, node);
        
        convertView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        Log.d("John", "Row width: " + convertView.getMeasuredWidth());
        
        return convertView;
    }
    
    private class ViewHolder {
        public LinearLayout dottedBlock;
        public ImageView icExpandCollapse;
        public FrameLayout contentWrapper;
    }
    
    private void setupTreeView(ViewHolder viewHolder, final TreeViewNode node) {
        setupDottedBlockView(viewHolder, node);
        setupExpandCollapseView(viewHolder, node);
    }
    
    private void setupTreeViewNode(ViewHolder viewHolder, final TreeViewNode node) {
        View treeNodeView = getTreeNodeView(node);
        if (treeNodeView != null) {
            viewHolder.contentWrapper.removeAllViews();
            viewHolder.contentWrapper.addView(treeNodeView);
        }
        viewHolder.contentWrapper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onNodeClick(node);
                }
                if (mNodeClickExpand) {
                    expandOrCollapseNode(node);
                }
            }
        });
        viewHolder.contentWrapper.setLongClickable(true);
        viewHolder.contentWrapper.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickNode(node);
                return true;
            }
        });
    }

    private void setupDottedBlockView(ViewHolder viewHolder, TreeViewNode node) {
        LayoutParams layoutParams = viewHolder.dottedBlock.getLayoutParams();
        int singleOffset = mContext.getResources().getDimensionPixelSize(R.dimen.ic_expand_collapse_width);
        int nodeOffset = (node.getLevel() + (node.isLeaf()? 1: 0)) * singleOffset;
        int nodeHeight = mContext.getResources().getDimensionPixelSize(R.dimen.treeview_node_height);
        layoutParams.width = nodeOffset;
        layoutParams.height = nodeHeight;
        viewHolder.dottedBlock.setLayoutParams(layoutParams);
        if (mDotted && nodeOffset > 0) { // Root not need dotted line
            Bitmap dottedLineBackground = createDottedLineBackground(node, singleOffset, nodeOffset, nodeHeight);
            if (dottedLineBackground != null) {
                viewHolder.dottedBlock.setBackgroundDrawable(
                        new BitmapDrawable(mContext.getResources(), dottedLineBackground));
            } else {
                viewHolder.dottedBlock.setBackgroundColor(Color.TRANSPARENT);
            }
        } else {
            viewHolder.dottedBlock.setBackgroundColor(Color.TRANSPARENT);
        }
    }
    
    private Bitmap createDottedLineBackground(TreeViewNode node, int singleOffset, int nodeOffset, int nodeHeight) {
        Bitmap bitmap = Bitmap.createBitmap(nodeOffset, nodeHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = createDottedLinePaint();
        
        int startX = singleOffset / 2;
        TreeViewNode parent = node.getParent();
        TreeViewNode child = node;
        
        // Draw one horizontal line
        canvas.drawLine(startX + parent.getLevel() * singleOffset, nodeHeight/2, 
                nodeOffset, nodeHeight/2, paint);
        
        // Draw multiple vertical line
        while (parent != null) {
            int x = startX + parent.getLevel() * singleOffset;
            int y = nodeHeight;
            if (node.getParent().equals(parent)) {
                if (node.isLastNodeInParent()) {
                    y = nodeHeight / 2;
                }
                canvas.drawLine(x, 0, x, y, paint);
            } else {
                if (!child.isLastNodeInParent()) {
                    canvas.drawLine(x, 0, x, y, paint);
                }
            }
            child = parent;
            parent = parent.getParent();
        }
        
        return bitmap;
    }
    
    private Paint createDottedLinePaint() {
        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        PathEffect pathEffect = new DashPathEffect(new float[] {2, 2}, 1);
        paint.setPathEffect(pathEffect);
        paint.setStrokeWidth(1);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        return paint;
    }

    private void setupExpandCollapseView(ViewHolder viewHolder, final TreeViewNode node) {
        if (node.isLeaf()) {
            viewHolder.icExpandCollapse.setVisibility(View.GONE);
        } else {
            viewHolder.icExpandCollapse.setVisibility(View.VISIBLE);
            viewHolder.icExpandCollapse.setImageResource(node.isExpand()? R.drawable.ic_collapse: R.drawable.ic_expand);
        }
        viewHolder.icExpandCollapse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                expandOrCollapseNode(node);
            }
        });
    }
    
    private void expandOrCollapseNode(TreeViewNode node) {
        node.setExpand(!node.isExpand());
        mTreeViewBuilder.updateDisplayedNodes();
        mDisplayedNodes = mTreeViewBuilder.getDisplayedNodes();
        notifyDataSetChanged();
        if (mCallback != null) {
            mCallback.onNodeExpandedOrCollapsed(node, node.isExpand());
        }
    }
    
    private void longClickNode(TreeViewNode node) {
        if (mCallback != null) {
            mCallback.onNodeLongClick(node);
        }
    }
}
