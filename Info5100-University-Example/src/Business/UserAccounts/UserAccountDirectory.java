/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccounts;

import Business.Profiles.Profile;

import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class UserAccountDirectory {
    
      ArrayList<UserAccount> useraccountlist ;
    
      public UserAccountDirectory (){
          
       useraccountlist = new ArrayList();

    }

    public UserAccount newUserAccount(Profile p, String un, String pw) {

        UserAccount ua = new UserAccount (p,  un,  pw);
        useraccountlist.add(ua);
        return ua;
    }

    public UserAccount findUserAccount(String id) {

        for (UserAccount ua : useraccountlist) {

            if (ua.isMatch(id)) {
                return ua;
            }
        }
            return null; //not found after going through the whole list
         }
      
    /**
     * Returns the account with this username (case-insensitive), or null.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
    public UserAccount findByUsername(String un) {
        if (un == null) {
            return null;
        }
        for (UserAccount ua : useraccountlist) {
            if (ua.getUserLoginName().equalsIgnoreCase(un.trim())) {
                return ua;
            }
        }
        return null;
    }
    
    /**
     * Returns true when another account already uses this username. The except parameter lets
     * an account keep its own name during an update.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
     public boolean isUsernameTaken(String un, UserAccount except) {
        UserAccount found = findByUsername(un);
        return found != null && found != except;
    }
     
     public UserAccount AuthenticateUser(String un, String pw) {

        for (UserAccount ua : useraccountlist) {

            if (ua.IsValidUser(un, pw)) {
                return ua;
            }
        }
            return null; //not found after going through the whole list
         }   
     public ArrayList<UserAccount> getUserAccountList()
     {
         return useraccountlist;
     }
     
    /**
     * Removes a login account from the directory.
     *
     * Christopher Ogden NUID: 003959375 Contributed from Assignment 3 codebase.
     */
        public void removeUserAccount(UserAccount ua) {
        useraccountlist.remove(ua);
    }
}
