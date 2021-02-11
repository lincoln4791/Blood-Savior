package com.bloodFinder.mybloodbank.mainActivity.feed;

public class ModelClassFeedFragment {
    private String postCreatorName;
    private String postCreatorPhoto;
    private String bloodGroup;
    private String district;
    private String postDescription;
    private String postImage;
    private String area;
    private String love;
    private String views;
    private String timeStamp;
    private String postCreatorID;
    private String postID;


    public ModelClassFeedFragment(String postCreatorName, String postCreatorPhoto, String bloodGroupLookingFor, String timeAgo, String district, String postDescription
            , String postImage, String area, String love, String views, String timeStamp, String postCreatorID, String postID) {
        this.postCreatorName = postCreatorName;
        this.bloodGroup = bloodGroupLookingFor;
        this.timeStamp = timeAgo;
        this.district = district;
        this.postDescription = postDescription;
        this.postImage = postImage;
        this.area = area;
        this.love = love;
        this.views = views;
        this.timeStamp = timeStamp;
        this.postCreatorID = postCreatorID;
        this.postID = postID;
    }

    public String getPostCreatorName() {
        return postCreatorName;
    }

    public void setPostCreatorName(String postCreatorName) {
        this.postCreatorName = postCreatorName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getTimeAgo() {
        return timeStamp;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeStamp = timeAgo;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPostCreatorID() {
        return postCreatorID;
    }

    public void setPostCreatorID(String postCreatorID) {
        this.postCreatorID = postCreatorID;
    }
    public void setPostCreatorPhoto(String postCreatorPhoto) {
        this.postCreatorPhoto = postCreatorPhoto;
    }
    public String getPostCreatorPhoto() {
        return postCreatorPhoto;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
