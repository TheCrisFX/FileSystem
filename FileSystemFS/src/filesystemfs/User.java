/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystemfs;

import java.io.Serializable;

/**
 *
 * @author cris9
 */
public class User implements Serializable{
    public String name = "";
    public String completeName ="";
    public String pwd = "";
    public boolean groupCreation = false;
    public User(String pname,String ppwd, String pcompleteName,boolean pgroupCreation){
        name = pname;
        pwd = ppwd;
        completeName = pcompleteName;
        groupCreation = pgroupCreation;
    }
    
}
