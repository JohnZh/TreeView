package com.johnz.treeview;

import java.util.ArrayList;
import java.util.List;

public class TreeViewBuilder {
    
    private List<TreeViewNode> displayedNodes;
    private int counter;
    
    private void initialize() {
        displayedNodes = new ArrayList<TreeViewNode>();
        counter = 0; 
    }
    
    public TreeViewBuilder() {
        initialize();
    }
    
    public TreeViewBuilder(TreeViewNode root) {
        initialize();
        buildDisplayedNodes(root, false);
    }
    
    public TreeViewBuilder(TreeViewNode root, boolean expandAll) {
        initialize();
        buildDisplayedNodes(root, expandAll);
    }
    
    public List<TreeViewNode> getDisplayedNodes() {
        return displayedNodes;
    }
    
    public void updateDisplayedNodes() {
        clearDisplayedNodes();
        TreeViewNode root = displayedNodes.get(0);
        buildDisplayedNodes(root, false);
    }
    
    private void clearDisplayedNodes() {
        if (displayedNodes != null) {
            displayedNodes.clear();
            counter = 0;
        }
    }
    
    private void buildDisplayedNodes(TreeViewNode root, boolean expandAll) {
        if (root != null) {
            root.setNodeId(counter++);
            displayedNodes.add(root);
        }
        
        if (expandAll) {
            root.setExpand(true);
        }
        
        if (!root.isLeaf() && root.isExpand()) {
            List<TreeViewNode> children = root.getChildren();
            for (int i = 0; i < children.size(); i++) {
                buildDisplayedNodes(children.get(i), expandAll);
            }
        }
    }
}
