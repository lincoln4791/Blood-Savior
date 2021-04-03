package com.bloodFinder.bloodSavior.allUsers.searchByName;

public class ModelClassSearchByName {
    private String userName;
    private String userPhoto;
    private String userID;
    private String area;
    private String district;
    private String bloodGroup;
    private String age;
    private String email;
    private String gender;
    private String timeStamp;
    private String phone;

    public ModelClassSearchByName(String userName, String userPhoto, String userID, String area, String district,
                              String bloodGroup, String age, String email, String gender, String timeStamp, String phone) {
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userID = userID;
        this.area = area;
        this.district = district;
        this.bloodGroup = bloodGroup;
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.timeStamp = timeStamp;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
