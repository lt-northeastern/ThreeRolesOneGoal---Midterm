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
      public void setUserLoginName(String un) {
        username = un;
        checkLogin();
    }
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
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    public void touch() {
    lastUpdatedTime = LocalDateTime.now();
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }
     public void recordLogin() {
        lastLoginTime = LocalDateTime.now();
    }

    /** Called whenever the account (or the profile behind it) is changed. */
    public void checkLogin() {
        lastUpdatedTime = LocalDateTime.now();
    }
        
    @Override
        public String toString(){
            
            return getUserLoginName();
        }
        
}

