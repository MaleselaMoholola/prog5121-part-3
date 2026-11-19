package pitsimoholola_part1;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


public class PitsiMoholola_part1 {

    
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        loginclass loginsystem = new loginclass();
        
        String name;
        String surname;
        String username;
        String password;
        String cellnumber;
        
        System.out.println("Please enter your Name: ");
         name = input.nextLine();
         
         
         System.out.println("Please enter your Surname: ");
         surname = input.nextLine();
        
          System.out.println("Please enter your Username: ");
          username = input.nextLine();
        
           System.out.println("Please enter your password: ");
           password = input.nextLine();
           
            System.out.println("Please enter your Cellphone number: ");
            cellnumber = input.nextLine();
            
            String registrationMessage = loginsystem.registerUserWithMessage(username, password, cellnumber);
System.out.println(registrationMessage);
            

   input.close();
   
} 
}
 

    




    