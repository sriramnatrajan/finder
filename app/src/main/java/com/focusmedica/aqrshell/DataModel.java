package com.focusmedica.aqrshell;

/**
 * Created by Lokesh on 15-Feb-16.
 */
public class DataModel {

    int Id;
    String TitleId;
    String titleName;
    String imageName;
    String iap;
    String key;
    String databaseName;
    String dLink;
    String info;
    String descriptionData;
    String titlePages;
    String state;

    String description;

    String appFolder;
    String name;
    String value;
    String app_id;

    String appInfo;
    String app_type;
   public DataModel(String mName,String mValue,String mAppInfo,String mAppId,String mAppType ){
        name=mName;
        value=mValue;
        appInfo=mAppInfo;
        app_id=mAppId;
        app_type=mAppType;
    }


    public String getAppFolder() {
        return appFolder;
    }

    public void setAppFolder(String appFolder) {
        this.appFolder = appFolder;
    }


    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
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



    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getDescriptionData() {
        return descriptionData;
    }

    public void setDescriptionData(String descriptionData) {
        this.descriptionData = descriptionData;
    }



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getTitleId() {
        return TitleId;
    }

    public void setTitleId(String titleId) {
        this.TitleId = titleId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getIAP() {
        return iap;
    }

    public void setIAP(String iap) {
        this.iap = iap;
    }
    public String getKey() {
        return key;
    }

    public void setKEY(String key) {
        this.key = key;
    }
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    public String getDlink() {
        return dLink;
    }

    public void setDlink(String dLink) {
        this.dLink = dLink;
    }
    public void setTitlePages(String titlePages){
        this.titlePages=titlePages;
    }

    public String getTitlePages(){
        return titlePages;
    }
}
