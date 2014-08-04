package com.johnz.treeview;

import java.util.ArrayList;
import java.util.List;

public class TreeViewNode {

    private int nid;
    private TreeViewNode parent;
    private List<TreeViewNode> children;
    private String content;
    private boolean isExpand;
    private int iconRes;
    
    public TreeViewNode(TreeViewNode parent, String content, int iconRes) {
        this.content = content;
        this.parent = parent;
        this.iconRes = iconRes;
        this.isExpand = false;
        this.children = new ArrayList<TreeViewNode>();
    }
    
    public String getContent() {
        return content;
    }
    
    public TreeViewNode getParent() {
        return parent;
    }
    
    public List<TreeViewNode> getChildren() {
        return children;
    }
    
    public boolean isExpand() {
        return isExpand;
    }
    
    public void setExpand(boolean expand) {
        this.isExpand = expand;
    }
    
    public int getIconRes() {
        return iconRes;
    }
    
    public boolean isLeaf() {
        if (children != null && children.size() > 0) {
            return false;
        }
        return true;
    }
    
    public void addChildNode(TreeViewNode child) {
        this.children.add(child);
    }
    
    public int getLevel() {
        return parent != null ? parent.getLevel() + 1 : 0;
    }
    
    public String getTreePosition() {
        return parent != null ? parent.getTreePosition() + "," + getPositionInParent() : "0";
    }
    
    public int getPositionInParent() {
        List<TreeViewNode> nodes = parent.getChildren();
        return nodes.indexOf(this);
    }
    
    public boolean isLastNodeInParent() {
        List<TreeViewNode> nodes = parent.getChildren();
        return getPositionInParent() == nodes.size() - 1;
    }
}
