package com.ian.chomibridge;

import java.util.Date;

public class Group {

    private int id;
    private int ownerUserId;
    private Date created;
    private Date modified;
    private String name;
    private String description;
    private String imagePath;
    private int isSuspended;
    private int isStarted;
    private int categoryId;
    private int publicGroup;
    private String profilePicture;
    private String CoverPicture;
    private int isDeleted;
    private int publicCanPost;
    private String country;
    private String website;
    private String email;
    private String contactNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(int isSuspended) {
        this.isSuspended = isSuspended;
    }

    public int getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(int isStarted) {
        this.isStarted = isStarted;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPublicGroup() {
        return publicGroup;
    }

    public void setPublicGroup(int publicGroup) {
        this.publicGroup = publicGroup;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getCoverPicture() {
        return CoverPicture;
    }

    public void setCoverPicture(String CoverPicture) {
        this.CoverPicture = CoverPicture;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getPublicCanPost() {
        return publicCanPost;
    }

    public void setPublicCanPost(int publicCanPost) {
        this.publicCanPost = publicCanPost;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

}
