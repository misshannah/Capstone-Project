package com.olukoye.hannah.planmywedding;

class MainRecyclerViewItem {
    private String catName;

    private int catImageId;

    public MainRecyclerViewItem(String catName, int catImageId) {
        this.catName = catName;
        this.catImageId = catImageId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatImageId() {
        return catImageId;
    }

    public void setCatImageId(int catImageId) {
        this.catImageId = catImageId;
    }
}
