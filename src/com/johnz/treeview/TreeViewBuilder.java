package com.johnz.treeview;

import java.util.ArrayList;
import java.util.List;

public class TreeViewBuilder {
    
    private List<TreeViewNode> displayedNodes;
    private List<TreeViewNode> allNodes; // not be used
    private int counter;
    
    private void initialize() {
        displayedNodes = new ArrayList<TreeViewNode>();
        allNodes = new ArrayList<TreeViewNode>();
        counter = 0; 
    }
    
    public TreeViewBuilder() {
        initialize();
    }
    
    public TreeViewBuilder(TreeViewNode root) {
        initialize();
        buildAllNodes(root);
        buildDisplayedNodes(root, false);
    }
    
    public TreeViewBuilder(TreeViewNode root, boolean expandAll) {
        initialize();
        buildAllNodes(root);
        buildDisplayedNodes(root, expandAll);
    }
    
    public List<TreeViewNode> getDisplayedNodes() {
        return displayedNodes;
    }
    
    public void updateDisplayedNodes() {
        TreeViewNode root = displayedNodes.get(0);
        clearDisplayedNodes();
        buildDisplayedNodes(root, false);
    }
    
    private void clearDisplayedNodes() {
        if (displayedNodes != null) {
            displayedNodes.clear();
        }
    }
    
    private void buildAllNodes(TreeViewNode root) {
        if (root != null) {
            root.setNodeId(counter++);
            allNodes.add(root);
        }
        List<TreeViewNode> children = root.getChildren();
        if (children != null && children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                buildAllNodes(children.get(i));
            }
        }
    }
    
    private void buildDisplayedNodes(TreeViewNode root, boolean expandAll) {
        if (root != null) {
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
