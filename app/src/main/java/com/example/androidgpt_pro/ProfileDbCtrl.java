package com.example.androidgpt_pro;

import android.media.Image;

public class ProfileDbCtrl extends DatabaseCtrl {

    private int pID;
    private String pNm;
    private String pPw;
    private Image pImg;
    private String pPhn;
    private String pEml;

//    public int createNewProfile() {
//        return 0;
//    }

    public void setInitProfile(int profileID,
                               String profileName,
                               String profilePassword,
                               Image profileImage,
                               String profilePhoneNumber,
                               String profileEmail) {
        pNm = profileName;
        pPw = profilePassword;
        pImg = profileImage;
        pPhn = profilePhoneNumber;
        pEml = profileEmail;
    }

    public void setProfileName(String profileName) {
        pNm = profileName;
    }
    public void setProfilePassword(String profilePassword) {
        pPw = profilePassword;
    }
    public void setProfileImage(Image profileImage) {
        pImg = profileImage;
    }
    public void setProfilePhoneNumber(String profilePhoneNumber) {
        pPhn = profilePhoneNumber;
    }
    public void setProfileEmail(String profileEmail) {
        pEml = profileEmail;
    }
}
