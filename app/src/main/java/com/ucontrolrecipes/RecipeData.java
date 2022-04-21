package com.ucontrolrecipes;

public class RecipeData {
    private int ID;
    private String title;
    private String imageURL;
    private String imageType;

    public RecipeData(int ID, String title, String imageURL, String imageType){
        setID(ID);
        setTitle(title);
        setImageURL(imageURL);
        setImageType(imageType);
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getImageType() {
        return imageType;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
