/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystemfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


/**
 *
 * @author cris9
 */
public class FileSystemFS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // TODO code application logic here
        ArrayList<String> listcmds = new ArrayList<>();//lista de los comandos ingresados

        Group groupCurrent = null;//grupo actual
        Scanner entry = new Scanner(System.in);//scanner de entrada
        String insCurrent ="",pathCurrent = "";
        String userCurrent = "",fullnameCurrent = "";
        int permisoGrupo = 0,permisoUsuario =0;
        String nameDisk ="";
        FileDisk fs = null;
        if(args.length == 1){
            FileInputStream fi = new FileInputStream(new File(args[0]));//obtenemos los objetos usuario del archivo
            ObjectInputStream oi = new ObjectInputStream(fi);
            fs = (FileDisk)oi.readObject();
            oi.close();
            fi.close();
            groupCurrent = fs.groupCurrent;//grupo actual
            insCurrent =fs.insCurrent;
            pathCurrent = fs.pathCurrent;
            userCurrent = fs.userCurrent;
            fullnameCurrent = fs.fullnameCurrent;
            permisoGrupo = fs.permisoGrupo;
            permisoUsuario =fs.permisoUsuario;
            nameDisk =fs.nameDisk;       
        }
/***************************************************************************************************************/       
       while(true){
            System.out.print(userCurrent+"@"+nameDisk+":"+pathCurrent+">");
            insCurrent = entry.nextLine();
            if(!insCurrent.equals("")){
                String [] instruction = insCurrent.split(" ");
/***************************************************************************************************************/
                if(instruction[0].equals("format") && instruction.length == 3){
                    
                String [] file = instruction[1].split("\\.");// .fs format verificacion
                
                if(file[1].equals("fs") && file.length == 2){
                    if(tryParseInt(instruction[2])){//size de disco verificacion de dato
                        System.out.print("Please write a password for the root user:");
                        
                        String password = entry.nextLine();

                       User root = new User("root",password,"root",true);
                       userCurrent = root.name;
                       fullnameCurrent = root.completeName;


                        Group rootgroup = new Group("root");
                        rootgroup.addUser(root);
                        groupCurrent = rootgroup;
                                            
                       fs = new FileDisk(instruction[1],Integer.parseInt(instruction[2]));
                       fs.mkdir("/user/root",userCurrent,groupCurrent.namegroup,7,7,0);
                       fs.listUsers.add(root);
                        
                       fs.listGroups.add(groupCurrent);  
                        
                        
                        
                        nameDisk = instruction[1];
                        pathCurrent ="/user/root";
                    }
                    else{
                    System.out.println("Size needs to be numbers");
                    }
                    
                }else{
                    System.out.println("Format not allowed, please create a .fs file");
                }                
     
                }
            else if(instruction[0].equals("useradd") && instruction.length == 2){
                System.out.println("Please write a fullname for the "+instruction[1]+" user:");
                String fullname = entry.nextLine();
                System.out.print("Please write a password for the "+instruction[1]+" user:");
                String password = entry.nextLine();  
                System.out.print("Please write if you want user "+instruction[1]+" may create groups (0:NO,1:YES):");
                String groupcreation = entry.nextLine(); 
                if(groupcreation.equals("0")){
                    
                User newUser = new User(instruction[1],password,fullname,false);
                fs.listUsers.add(newUser);
                
                Group newgroup = new Group(instruction[1]);
                newgroup.addUser(newUser);
                fs.listGroups.add(newgroup);                 
                groupCurrent = newgroup;
                
                
                fs.mkdir("/user/"+instruction[1],instruction[1],groupCurrent.namegroup,7,7,0);

                System.out.println("User Added");                
                }
                else if(groupcreation.equals("1")){
                User newUser = new User(instruction[1],password,fullname,true);
                fs.listUsers.add(newUser);
                
                Group newgroup = new Group(instruction[1]);
                newgroup.addUser(newUser);
                fs.listGroups.add(newgroup);                
                groupCurrent = newgroup;
                
                fs.mkdir("/user/"+instruction[1],instruction[1],groupCurrent.namegroup,7,7,0);

                System.out.println("User Added");                   
                
                }else{
                    System.out.println("Invalid option"); 
                }

            }
/***************************************************************************************************************/
            else if(instruction[0].equals("groupadd") && instruction.length == 2){
                if(fs.verifyUser(userCurrent)){
                    int condi = 0;
                    for(int i =0;i < fs.listGroups.size();i++){
                      if(instruction[1].equals(fs.listGroups.get(i).namegroup)){
                          condi = 1;
                      }  
                    }
                    if(condi == 0){
                    Group newgroup = new Group(instruction[1]);
                    fs.listGroups.add(newgroup);  
                    System.out.println("Group Added"); 
                    }else{
                    System.out.println("Group already exists");
                    }
                }else{
                    System.out.println("User no allowed to add groups");
                }
            
            }
/***************************************************************************************************************/
            else if(instruction[0].equals("passwd") && instruction.length == 2){
                int condi = 0;
                for(int i =0;i < fs.listUsers.size();i++){
                    if(instruction[1].equals(fs.listUsers.get(i).name)){
                        System.out.print("New password for the "+instruction[1]+" user:");
                        String password = entry.nextLine();
                        System.out.print("Confirm Password:");
                        String password1 = entry.nextLine();   
                        if(password.equals(password1)){
                        fs.listUsers.get(i).pwd = password;
                        condi =1;
                        }else{
                           System.out.println("Password doesnt match");
                           condi = -1;
                        }
                         
                    }
                }
                if(condi == 1){
                    System.out.println("Password changed successfully");
                }
                if(condi == 0){
                    System.out.println(instruction[1]+" username doesnt exists");
                }
            }
/***************************************************************************************************************/
            else if(insCurrent.equals("su")){
                for(int i =0;i < fs.listUsers.size();i++){
                    if("root".equals(fs.listUsers.get(i).name)){
                        System.out.print("Please type the password for "+fs.listUsers.get(i).name+" user:");
                        String password = entry.nextLine(); 
                        if(password.equals(fs.listUsers.get(i).pwd)){
                            userCurrent = fs.listUsers.get(i).name; 
                            fullnameCurrent =fs.listUsers.get(i).completeName;
                        }else{
                            System.out.println("Wrong password");
                        }

                    }
                }             
            }
/***************************************************************************************************************/
            else if(instruction[0].equals("su") && instruction.length == 2){
                int condi = 0;
                for(int i =0;i < fs.listUsers.size();i++){
                    if(instruction[1].equals(fs.listUsers.get(i).name)){
                        System.out.print("Please type the password for "+fs.listUsers.get(i).name+" user:");
                        String password = entry.nextLine(); 
                        if(password.equals(fs.listUsers.get(i).pwd)){
                            condi =1;
                            userCurrent = fs.listUsers.get(i).name;    
                            fullnameCurrent =fs.listUsers.get(i).completeName;
                        }else{
                            condi =1;
                            System.out.println("Wrong password");
                        }

                    }
                } 
                if(condi == 0){
                    System.out.println(instruction[1]+" username doesnt exists");
                }
            }  
/***************************************************************************************************************/
            else if(insCurrent.equals("whoami")){
                System.out.println("Username: "+userCurrent);
                System.out.println("Full name: "+fullnameCurrent);
            
            }
/***************************************************************************************************************/
                else if(insCurrent.equals("pwd")){ 
                    System.out.println(pathCurrent);
                }               
/***************************************************************************************************************/
                else if(instruction[0].equals("mkdir") && instruction.length == 2){ 
                    String [] directories =instruction[1].split(",");
                    if(directories.length > 1){
                        for(String dir: directories){
                            fs.mkdir(pathCurrent+"/"+dir,userCurrent,groupCurrent.namegroup,7,7,0);
                        }
                    }else{
                        fs.mkdir(pathCurrent+"/"+instruction[1],userCurrent,groupCurrent.namegroup,7,7,0);
                    }
                    
                }    
/***************************************************************************************************************/
                else if(insCurrent.equals("ls")){ 
                    System.out.println(fs.ls(pathCurrent));
                }
/***************************************************************************************************************/
                else if(instruction[0].equals("cd") && instruction.length == 2){ 
                    if(instruction[1].equals("..")){
                            List<String> path =Arrays.asList(pathCurrent.split("/"));
                            pathCurrent = "";
                            for(int i = 1;i< path.size()-1;i++){
                                pathCurrent+="/"+path.get(i);
                            }                  
                    }else{
                    try{
                         List <String> lista = fs.ls(pathCurrent+"/"+instruction[1]);
                         pathCurrent = pathCurrent+"/"+instruction[1];
                    }catch(Exception e){
                        System.out.println("Directory doesnt exists");
                    }
                    }
                }
/***************************************************************************************************************/
                else if(instruction[0].equals("touch") && instruction.length == 2){ 
                      fs.touch(pathCurrent+"/"+instruction[1],"HOla",userCurrent,groupCurrent.namegroup,7,7,0);
                      System.out.println("File Created");
                }  
/***************************************************************************************************************/
                else if(instruction[0].equals("rm") && instruction.length == 2){ 
                      fs.rm(pathCurrent,"",instruction[1]);
                }
/***************************************************************************************************************/
                else if(instruction[0].equals("rm") && instruction.length == 3){ 
                    if(instruction[1].equals("-R")){
                        fs.rm(pathCurrent,"-R",instruction[2]);
                    }
                }
/***************************************************************************************************************/
                else if(instruction[0].equals("mv") && instruction.length == 3){
                    try{
                      String [] path = instruction[2].split("/");
                      if(path.length == 1){
                          fs.mv(pathCurrent,instruction[1],instruction[2],userCurrent,groupCurrent.namegroup,7,7,0);
                      }else{
                          List <String> lista = fs.ls(instruction[2]);
                          fs.mvm(pathCurrent,instruction[1],instruction[2]);
                      }
                    }catch(Exception e){
                        System.out.println("Path doesnt exists");
                    }                      
                }
/***************************************************************************************************************/
                else if(instruction[0].equals("cat") && instruction.length == 2){ 
                    try{
                        System.out.println(fs.cat(pathCurrent+"/"+instruction[1]));
                    }catch(Exception e){
                        System.out.println("File doesnt exists");
                    }
                      
                }  
/***************************************************************************************************************/
                else if(instruction[0].equals("chown") && instruction.length == 3){ 
                    try{
                        fs.ChangeOwner(pathCurrent,instruction[1], instruction[2]);
                    }catch(Exception e){
                        System.out.println("File or directory doesnt exists");
                    }
                      
                }  
/***************************************************************************************************************/
                else if(instruction[0].equals("chogrp") && instruction.length == 3){ 
                    try{
                        fs.ChangeGroup(pathCurrent,instruction[1], instruction[2]);
                    }catch(Exception e){
                        System.out.println("File or directory doesnt exists");
                    }
                      
                }  
/***************************************************************************************************************/
                else if(instruction[0].equals("chmod") && instruction.length == 3){ 
                    try{
                        if(tryParseInt(instruction[1])){
                           if(Integer.parseInt(instruction[1]) <= 77 && Integer.parseInt(instruction[1]) >= 0){
                           fs.ChangePermission(pathCurrent,Integer.parseInt(instruction[1].charAt(0)+""),Integer.parseInt(instruction[1].charAt(1)+""), instruction[2]);
                           }
                            
                        }else{
                        System.out.println("Permissions needs to be numbers");
                        }
                        
                    }catch(Exception e){
                        System.out.println("File or directory doesnt exists");
                    }
                      
                }  
/***************************************************************************************************************/
                else if(instruction[0].equals("openFile") && instruction.length == 2){ 
                    try{
                        fs.openFile(pathCurrent,instruction[1]);
                    }catch(Exception e){
                        System.out.println("File  doesnt exists");
                    }
                      
                } 
/***************************************************************************************************************/
                else if(instruction[0].equals("whereis") && instruction.length == 2){ 
                    try{
                        fs.whereis(pathCurrent,instruction[1]);
                    }catch(Exception e){
                        System.out.println("File  doesnt exists");
                    }
                      
                } 
/***************************************************************************************************************/
                else if(instruction[0].equals("closeFile") && instruction.length == 2){ 
                    try{
                        fs.closeFile(pathCurrent,instruction[1]);
                    }catch(Exception e){
                        System.out.println("File  doesnt exists");
                    }
                      
                }  
/***************************************************************************************************************/
                else if(insCurrent.equals("viewFilesOpen")){ 
                    try{
                        fs.viewOpenFiles(pathCurrent);
                    }catch(Exception e){
                        System.out.println("Empty directory");
                    }
                      
                }  
/***************************************************************************************************************/
                else if(instruction[0].equals("infoFCB") && instruction.length == 2){ 
                    try{
                        fs.infoFCB(pathCurrent,instruction[1]);
                    }catch(Exception e){
                        System.out.println("File or directory doesnt exists");
                    }
                      
                }  
/***************************************************************************************************************/
                else if(instruction[0].equals("note") && instruction.length == 2){ 
                    try{
                        System.out.println("Previous Content:");
                        System.out.println(fs.cat(pathCurrent+"/"+instruction[1]));
                        System.out.println("New Content Content:");
                        String newString = entry.nextLine();
                       // Editor edi = new Editor();
                       // String newString = edi.start();
                        fs.NewString(pathCurrent, newString, instruction[1]);

                    }catch(Exception e){
                        System.out.println("File or directory doesnt exists");
                    }
                      
                }  
/***************************************************************************************************************/ 
            else if(insCurrent.equals("infoFS")){
                System.out.println("Name FileSystem: "+fs.nameDisk);
                System.out.println("Size: "+fs.size+" MB");
                System.out.println("Space Used: "+(((fs.size*1024)-fs.remaining)/1024)+" MB");
                System.out.println("Reamining Space: "+(fs.remaining/1024)+" MB");
            }
/***************************************************************************************************************/                
                else if(insCurrent.equals("poweroff")){ 
                        fs.groupCurrent = groupCurrent;//grupo actual
                        fs.insCurrent =insCurrent;
                        fs.pathCurrent = pathCurrent;
                        fs.userCurrent = userCurrent;
                        fs.fullnameCurrent = fullnameCurrent;
                        fs.permisoGrupo = permisoGrupo;
                        fs.permisoUsuario =permisoUsuario;
                        fs.nameDisk =nameDisk;
                    FileOutputStream f = new FileOutputStream(new File(nameDisk));
                    ObjectOutputStream o = new ObjectOutputStream(f);
                    o.writeObject(fs);
                    o.close();
                    f.close();
                    break;
                }
/***************************************************************************************************************/
                 else if(insCurrent.equals("clear")){ 
                    clrscr();
                } 
/***************************************************************************************************************/
                else{
                    System.out.println(insCurrent+" is not a recognized command, please try again");
                }
            }
       }
    }
        public static void clrscr(){
    //Clears Screen in java
    try {
        if (System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            Runtime.getRuntime().exec("clear");
            } catch (IOException | InterruptedException ex) {}
        }
       public static boolean tryParseInt(String value) {  //funcion para verificar si es entero
     try {  
         Integer.parseInt(value);  
         return true;  
      } catch (NumberFormatException e) {  
         return false;  
      }  
    }
}


