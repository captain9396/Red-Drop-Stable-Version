package org.reddrop.reddrop;

import java.io.File;

/**
 * Created by dipto on 1/2/18.
 */

public class CurrentSessionData {

    private static final CurrentSessionData currentSessionData = new CurrentSessionData();
    private static UserInfo currentUserInfo;
    private static File currentUserProfilePicture;

    private CurrentSessionData(){
        currentUserInfo = null;
        currentUserProfilePicture = null;
    }

    public UserInfo getCurrentUserInfo() {
        return currentUserInfo;
    }

    public void setCurrentUserInfo(UserInfo currentUserInfo) {
        this.currentUserInfo = currentUserInfo;
    }

    public File getCurrentUserProfilePicture() {
        return currentUserProfilePicture;
    }

    public void setCurrentUserProfilePicture(File currentUserProfilePicture) {
        this.currentUserProfilePicture = currentUserProfilePicture;
    }

    public static CurrentSessionData getInstance(){
        return currentSessionData;
    }

    public static void clearAllData(){
        currentUserInfo = null;
        currentUserProfilePicture = null;
    }
}
