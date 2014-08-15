package com.johnz.treeview;

import java.util.ArrayList;
import java.util.List;

public class TreeViewBuilder<T> {
    
    private List<TreeViewNode<T>> displayedNodes;
    private List<TreeViewNode<T>> allNodes; // not be used
    private int counter;
    
    private void initialize() {
        displayedNodes = new ArrayList<TreeViewNode<T>>();
        allNodes = new ArrayList<TreeViewNode<T>>();
        counter = 0; 
    }
    
    public TreeViewBuilder() {
        initialize();
    }
    
    public TreeViewBuilder(TreeViewNode<T> root) {
        initialize();
        buildAllNodes(root);
        buildDisplayedNodes(root, true);
    }
    
    public List<TreeViewNode<T>> getDisplayedNodes() {
        return displayedNodes;
    }
    
    public void updateDisplayedNodes() {
        TreeViewNode<T> root = displayedNodes.get(0);
        clearDisplayedNodes();
        buildDisplayedNodes(root, false);
    }
    
    private void clearDisplayedNodes() {
        if (displayedNodes != null) {
            displayedNodes.clear();
        }
    }
    
    private void buildAllNodes(TreeViewNode<T> root) {
        if (root != null) {
            root.setNodeId(counter++);
            allNodes.add(root);
        }
        List<TreeViewNode<T>> children = root.getChildren();
        if (children != null && children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                buildAllNodes(children.get(i));
            }
        }
    }
    
    private void buildDisplayedNodes(TreeViewNode<T> root, boolean expandAll) {
        if (root != null) {
            displayedNodes.add(root);
        }
        
        if (expandAll) {
            root.setExpand(true);
        }
        
        if (!root.isLeaf() && root.isExpand()) {
            List<TreeViewNode<T>> children = root.getChildren();
            for (int i = 0; i < children.size(); i++) {
                buildDisplayedNodes(children.get(i), expandAll);
            }
        }
    }
}
