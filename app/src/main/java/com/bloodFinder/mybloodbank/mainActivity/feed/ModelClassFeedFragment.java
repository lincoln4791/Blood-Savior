package com.bloodFinder.mybloodbank.mainActivity.feed;

public class ModelClassFeedFragment {
    private String postCreatorName;
    private String postCreatorPhoto;
    private String bloodGroup;
    private String district;
    private String postDescription;
    private String postImage;
    private String area;
    private String postLove;
    private String postView;
    private String timeStamp;
    private String postCreatorID;
    private String postID;
    private String cause;
    private String gender;
    private String accepted;
    private String donated;
    private String phone;
    private String unitBag;
    private Boolean loveCheckerFlag;




    public ModelClassFeedFragment(String postCreatorName, String postCreatorPhoto, String bloodGroupLookingFor, String timeAgo, String district, String postDescription
            , String postImage, String area, String postLove, String postView, String timeStamp, String postCreatorID, String postID, String cause,
                                  String gender, String accepted, String donated, String phone, String unitBag, Boolean loveCheckerFlag) {
        this.postCreatorName = postCreatorName;
        this.postCreatorPhoto = postCreatorPhoto;
        this.bloodGroup = bloodGroupLookingFor;
        this.timeStamp = timeAgo;
        this.district = district;
        this.postDescription = postDescription;
        this.postImage = postImage;
        this.area = area;
        this.postLove = postLove;
        this.postView = postView;
        this.timeStamp = timeStamp;
        this.postCreatorID = postCreatorID;
        this.postID = postID;
        this.cause = cause;
        this.gender = gender;
        this.accepted = accepted;
        this.donated = donated;
        this.phone = phone;
        this.unitBag = unitBag;
        this.loveCheckerFlag=loveCheckerFlag;
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

    public String getPostLove() {
        return postLove;
    }

    public void setPostLove(String postLove) {
        this.postLove = postLove;
    }

    public String getPostView() {
        return postView;
    }

    public void setPostView(String postView) {
        this.postView = postView;
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

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getDonated() {
        return donated;
    }

    public void setDonated(String donated) {
        this.donated = donated;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUnitBag() {
        return unitBag;
    }

    public void setUnitBag(String unitBag) {
        this.unitBag = unitBag;
    }

    public Boolean getLoveCheckerFlag() {
        return loveCheckerFlag;
    }

    public void setLoveCheckerFlag(Boolean loveCheckerFlag) {
        this.loveCheckerFlag = loveCheckerFlag;
    }
}
