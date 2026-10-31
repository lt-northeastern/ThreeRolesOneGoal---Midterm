/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccounts;

import Business.Profiles.Profile;
import java.time.LocalDateTime;



/**
 *
 * @author kal bugrara
 */
public class UserAccount {
    
    Profile profile;
    String username;
    String password;
    private final LocalDateTime createdTime;
    private LocalDateTime lastLoginTime;   
    private LocalDateTime lastUpdatedTime;
    
    /**
     * Creates a login account for a profile and stamps the created and last-updated times with
     * the current time.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public UserAccount (Profile profile, String un, String pw){
        username = un;
        password = pw;
        this.profile = profile;
        createdTime = LocalDateTime.now();
        lastUpdatedTime = createdTime;

    }

    public String getPersonId(){
        return profile.getPerson().getPersonId();
    }
    public String getUserLoginName(){
        return username;
    }
    /**
     * Changes the username and updates the account's last-updated time.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
      public void setUserLoginName(String un) {
        username = un;
        checkLogin();
    }
    /**
     * Changes the password and updates the account's last-updated time.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
        public void setPassword(String pw) {
        password = pw;
        touch();
    }

        public boolean isMatch(String id){
        if(getPersonId().equals(id)) return true;
        return false;
    }
        public boolean IsValidUser(String un, String pw){
        
            if (username.equalsIgnoreCase(un) && password.equals(pw)) return true;
            else return false;
        
        }
    public String getRole(){
            return profile.getRole();
    }
        
    public Profile getAssociatedPersonProfile(){
            return profile;
    }
    /**
     * Returns when the account was created.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    /**
     * Sets the last-updated time to now; called when the account or the profile behind it is
     * edited.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void touch() {
    lastUpdatedTime = LocalDateTime.now();
    }

    /**
     * Returns the time of the last successful login, or null if the user has never logged in.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * Returns when the account was last changed.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }
    /**
     * Sets the last-login time to now; called by the main frame after a successful login.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
     public void recordLogin() {
        lastLoginTime = LocalDateTime.now();
    }

    /**
     * Sets the last-updated time to now (same effect as touch()); called when the username
     * changes.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public void checkLogin() {
        lastUpdatedTime = LocalDateTime.now();
    }
        
    @Override
        public String toString(){
            
            return getUserLoginName();
        }
        
}

