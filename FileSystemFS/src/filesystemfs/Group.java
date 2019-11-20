/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystemfs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author cris9
 */
public class Group implements Serializable {
    public String namegroup ="";
    public ArrayList<User> listUsers = new ArrayList<>();

    public Group(String pname) {
        namegroup = pname;

        
    }
   public void addUser(User user){
       listUsers.add(user);
   }
    
}
