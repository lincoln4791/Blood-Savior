package com.bloodFinder.mybloodbank.mainActivity.requests.MyRequests;

public class ModelClassMyRequests {
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
    private String requiredDate;
    private String loveCheckerFlag;
    private String acceptedFlag;
    private String completedFlag;
    private String post_order;

    public ModelClassMyRequests(String bloodGroup, String area, String district, String accepted, String donated,
                                String postCreatorID, String postCreatorName, String postID, String postImage, String timeStamp, String cause,
                                String gender, String postCreatorPhoto, String postDescription, String postLove, String postView,
                                String unitBag, String phone,String acceptedFlag, String  loveCheckerFlag, String requiredDate,
                                String completedFlag, String post_order) {
        this.bloodGroup = bloodGroup;
        this.area = area;
        this.district = district;
        this.accepted = accepted;
        this.donated = donated;
        this.postCreatorID = postCreatorID;
        this.postCreatorName = postCreatorName;
        this.postID = postID;
        this.postImage = postImage;
        this.timeStamp = timeStamp;
        this.cause = cause;
        this.gender = gender;
        this.postCreatorPhoto = postCreatorPhoto;
        this.postDescription = postDescription;
        this.postLove = postLove;
        this.postView = postView;
        this.phone = phone;
        this.unitBag = unitBag;
        this.requiredDate = requiredDate;
        this.acceptedFlag = acceptedFlag;
        this.loveCheckerFlag = loveCheckerFlag;
        this.completedFlag = completedFlag;
        this.post_order = post_order;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public String getPostCreatorID() {
        return postCreatorID;
    }

    public void setPostCreatorID(String postCreatorID) {
        this.postCreatorID = postCreatorID;
    }

    public String getPostCreatorName() {
        return postCreatorName;
    }

    public void setPostCreatorName(String postCreatorName) {
        this.postCreatorName = postCreatorName;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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

    public String getPostCreatorPhoto() {
        return postCreatorPhoto;
    }

    public void setPostCreatorPhoto(String postCreatorPhoto) {
        this.postCreatorPhoto = postCreatorPhoto;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
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

    public String getLoveCheckerFlag() {
        return loveCheckerFlag;
    }

    public void setLoveCheckerFlag(String loveCheckerFlag) {
        this.loveCheckerFlag = loveCheckerFlag;
    }

    public String getAcceptedFlag() {
        return acceptedFlag;
    }

    public void setAcceptedFlag(String acceptedFlag) {
        this.acceptedFlag = acceptedFlag;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getCompletedFlag() {
        return completedFlag;
    }

    public void setCompletedFlag(String completedFlag) {
        this.completedFlag = completedFlag;
    }

    public String getPost_order() {
        return post_order;
    }

    public void setPost_order(String post_order) {
        this.post_order = post_order;
    }
}
