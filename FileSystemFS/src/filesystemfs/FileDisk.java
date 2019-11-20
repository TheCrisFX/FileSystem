/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystemfs;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 *
 * @author cris9
 */
class FileDisk implements Serializable {
    class File implements Serializable{
        String owner = "";
        String group = "";
        String address ="";
        Date creationDate = new Date(); 
        String state= "Close";
        int permissionOwner = 0;
        int permissionGroup = 0;
        int size = 0;
        boolean isfile = false;
        HashMap < String, File > files = new HashMap < > ();
        String content = "";
    }
    public File root;
    public String nameDisk ="";
    public int size = 0;
    public float remaining =0;
    public  ArrayList<Group> listGroups = new ArrayList<>();//lista de grupos
    public  ArrayList<User> listUsers = new ArrayList<>();//lista de usuarios
    public  String insCurrent ="",pathCurrent = "";
    public  String userCurrent = "",fullnameCurrent = "";
    public  int permisoGrupo = 0,permisoUsuario =0;
    public Group groupCurrent = null;//grupo actual

    public FileDisk(String pnameDisk,int psize)  {
        root = new File();
        nameDisk = pnameDisk;
        size = psize;
        remaining = psize;
    }
    public List < String > ls(String path) {
        File t = root;
        List < String > files = new ArrayList < > ();
        if (!path.equals("/")) {
            String[] d = path.split("/");
            for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
            }
            if (t.isfile) {
                files.add(d[d.length - 1]);
                return files;
            }
        }
        List < String > res_files = new ArrayList < > (t.files.keySet());
        Collections.sort(res_files);
        return res_files;
    }
    public void rm(String path,String erre,String name) {
        File t = root;
        int condi = 0;
        if (!path.equals("/")) {
            String[] d = path.split("/");
            for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(name)){
                    if(t.files.get(name).isfile){
                        t.files.remove(name);
                        condi =1;
                    }else{
                        if(erre.equals("-R")){
                                t.files.remove(name);
                                condi =1;                             
                        }else{
                            if(ls(path+"/"+name).size() == 0){
                                t.files.remove(name);
                                condi =1;                            
                            }else{
                                condi =2;
                            }                        
                        }
                    }
                }
            }
            if(condi == 1){
                System.out.println("File o Directory Deleted");
            
            }
            if(condi == 2){
                System.out.println("Directory contain files");
            
            }
            if(condi == 0){
                System.out.println("File doesnt exists");
            }
            
        }
    }
    public void mv(String path,String oldN,String newN,String powner,String pgroup,int ppermissionOwner,int ppermissionGroup,int psize) {
        File t = root;
        int condi = 0;
        boolean flag = false;
        String contentTemp = "";
        if (!path.equals("/")) {
            String[] d = path.split("/");
            for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(oldN)){
                    if(t.files.get(oldN).isfile){
                        flag = true;
                        contentTemp = t.files.get(oldN).content;
                    }
                    if(flag){
                        File nuevo = new File();
                        nuevo.owner = powner;
                        nuevo.group = pgroup;
                        nuevo.address =path+"/"+newN;
                        nuevo.permissionOwner = ppermissionOwner;
                        nuevo.permissionGroup = ppermissionGroup;
                        nuevo.size = psize;
                        t.files.put(newN, nuevo);
                    }else{
                        t.files.put(newN, t.files.get(oldN));
                    }
                    t.files.remove(oldN);
                    condi =1;
                }
                if(condi == 1){
                    t = t.files.get(newN);
                    if(flag){
                    t.isfile = true;
                    t.content = contentTemp;
                    }
                }
            }
            if(condi == 1){
                System.out.println("File o Directory Renamed");
            }else{
                System.out.println("File doesnt exists");
            }
            
        }
    }

    public void mvm(String path,String name,String newpath){
        File temp = FindFile(path,name);
        File t = root;
        String[] d = newpath.split("/");
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);       
        }
        t.files.put(name,temp);
    }
 
    public File FindFile(String path,String name) {
        File t = root;
        String[] d = path.split("/");
        File temp = null;
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(name)){
                    temp =t.files.get(name);
                    t.files.remove(name);
                }            
        }
        return temp;
    }
    public void ChangeOwner(String path,String newOwner,String filename){
        File t = root;
        String[] d = path.split("/");
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(filename)){
                    t.files.get(filename).owner = newOwner;
                }            
        }       
        
    }
     public void ChangeGroup(String path,String newGroup,String filename){
        File t = root;
        String[] d = path.split("/");
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(filename)){
                    t.files.get(filename).group = newGroup;
                }            
        }       
        
    }
     public void NewString(String path,String pcontent,String filename){
        File t = root;
        String[] d = path.split("/");
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(filename)){
                    t.files.get(filename).content = pcontent;
                    t.files.get(filename).size = pcontent.length();
                    remaining = (remaining*1024) - t.files.get(filename).size;
                }            
        }       
        
    }
     public void openFile(String path,String filename){
        File t = root;
        String[] d = path.split("/");
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(filename)){
                    if(t.files.get(filename).isfile){
                    t.files.get(filename).state = "Open";
                    }
                    else{
                        System.out.println("The entry is a directory");
                    }
                }            
        }       
        
    }
     public void closeFile(String path,String filename){
        File t = root;
        String[] d = path.split("/");
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(filename)){
                    if(t.files.get(filename).isfile){
                    t.files.get(filename).state = "Close";
                    }
                    else{
                        System.out.println("The entry is a directory");
                    }
                    
                }            
        }       
        
    }
     
     public void whereis(String path,String filename){
        File t = root;
      /*  String[] d = path.split("/");
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);                
        } */  
        search(t,path,filename);
     }
     public void search(File t,String path,String filename){
        List<String> lista = ls(path);
        System.out.println(lista);
        if(lista.size() != 0){
            for(String s:lista){
            if(t.files.get(s).isfile){
                if(t.files.containsKey(filename)){
                    System.out.println("Path: "+path+"/"+filename);
                    break;
                }
            }else{
                //t = root;
                String temp = path;
       
                System.out.println("AUI "+path);
               /* String[] d = path.split("/");
                for (int i = 1; i < d.length - 1; i++) {
                        t = t.files.get(d[i]);                
                }*/
                System.out.println("Cae ahi");
                path+="/"+s;
                search(t,path,filename);
                path = temp;
            }
        }
        }     
     }
     
     
     public void viewOpenFiles(String path){
        File t = root;
        String[] d = path.split("/");
        int contador =0;
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                   
        }
        List<String> lista = ls(path);
        for(String s:lista){
            if(t.files.get(s).isfile){
                if(t.files.get(s).state.equals("Open")){
                    contador++;
                }
            }
        }
        System.out.println("Total files open: "+contador);
        
    }    
     public void ChangePermission(String path,int pOwner,int pGroup,String filename){
        File t = root;
        String[] d = path.split("/");
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(filename)){
                    t.files.get(filename).permissionOwner = pOwner;
                    t.files.get(filename).permissionGroup = pGroup;
                }            
        }       
        
    }
     public void infoFCB(String path,String filename){
        File t = root;
        String[] d = path.split("/");
        for (int i = 1; i < d.length; i++) {
                t = t.files.get(d[i]);
                if(t.files.containsKey(filename)){
                    System.out.println("Name: "+filename);
                    System.out.println("Owner: "+t.files.get(filename).owner);
                    System.out.println("Group: "+t.files.get(filename).group);
                    System.out.println("Creation Date: "+t.files.get(filename).creationDate);
                    System.out.println("Permission Owner: "+t.files.get(filename).permissionOwner);
                    System.out.println("Permission Group: "+t.files.get(filename).permissionGroup);
                    System.out.println("State: "+t.files.get(filename).state);
                    System.out.println("Size: "+t.files.get(filename).size+" KB");
                    System.out.println("Path: "+t.files.get(filename).address);
                }            
        }       
        
    }
    public void mkdir(String path,String powner,String pgroup,int ppermissionOwner,int ppermissionGroup,int psize) {
        File t = root;
        String[] d = path.split("/");
        for (int i = 1; i < d.length; i++) {
            if (!t.files.containsKey(d[i])){
                File nuevo = new File();
                nuevo.owner = powner;
                nuevo.group = pgroup;
                nuevo.address =path;
                nuevo.permissionOwner = ppermissionOwner;
                nuevo.permissionGroup = ppermissionGroup;
                nuevo.size = psize;               
            
                t.files.put(d[i], nuevo);
            }
                
            t = t.files.get(d[i]);
        }
    }

    public void touch(String filePath, String content,String powner,String pgroup,int ppermissionOwner,int ppermissionGroup,int psize) {
        File t = root;
        String[] d = filePath.split("/");
        for (int i = 1; i < d.length - 1; i++) {
            t = t.files.get(d[i]);
        }
        if (!t.files.containsKey(d[d.length - 1])){
            File nuevo = new File();
            nuevo.owner = powner;
            nuevo.group = pgroup;
            nuevo.address =filePath;
            nuevo.permissionOwner = ppermissionOwner;
            nuevo.permissionGroup = ppermissionGroup;
            nuevo.size = psize;           
            t.files.put(d[d.length - 1], nuevo);
        }
            
        t = t.files.get(d[d.length - 1]);
        t.isfile = true;
        t.content = t.content + content;
    }

    public String cat(String filePath) {
        File t = root;
        String[] d = filePath.split("/");
        for (int i = 1; i < d.length - 1; i++) {
            t = t.files.get(d[i]);
        }
        return t.files.get(d[d.length - 1]).content;
    }
    public    boolean verifyUser(String name){
            for(User user: listUsers){
                if(user.name.equals(name)){
                      return user.groupCreation;
                }
         
        
        }
    
        return false;
    }
}
