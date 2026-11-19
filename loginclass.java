package pitsimoholola_part1;

import java.util.regex.Pattern;

public class loginclass {
    
    private String registeredUserName;
    private String registeredPassWord;
    private String registeredCellNumber;
    
    public boolean checkUsername(String username) {
        if (username == null) return false;
        return username.contains("_") && username.endsWith("@gmail.com");
    }
    
    
    public String registerUserWithMessage(String username, String password, String cellnumber) {
    if (!checkUsername(username)) {
        return "Username must contain an underscore, end with @gmail.com.";
    }
    if (!checkPassWordComplexity(password)) {
        return "Password not correctly formatted; must contain at least 8 characters, a capital letter, a number, and a special character.";
    }
    if (!checkCellPhoneNumber(cellnumber)) {
        return "Cellphone number must be in South African format (e.g., +27123456789).";
    }

    this.registeredUserName = username;
    this.registeredPassWord = password;
    this.registeredCellNumber = cellnumber;
    return "User registered successfully.";
}
    public boolean checkPassWordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapitalLetter = false;
        boolean hasSpecialCharacter = false;
        boolean hasNumber = false;
        
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasCapitalLetter = true;
            }
            if (Character.isDigit(ch)) {
                hasNumber = true;
            }
            if (!Character.isLetterOrDigit(ch)) {
                hasSpecialCharacter = true;
            }
        }    
        return hasCapitalLetter && hasSpecialCharacter && hasNumber;
    }
    
    public boolean checkCellPhoneNumber(String cellnumber) {
        if (cellnumber == null) return false;
        String regex = "^\\+27\\d{9}$";
        return Pattern.matches(regex, cellnumber);
    }
    
    public boolean registeredUser(String username, String password, String cellnumber) {
        if (!checkUsername(username)) {
            System.out.println("Username must contain an underscore, end with @gmail.com.");
            return false;
        }
        if (!checkPassWordComplexity(password)) {
            System.out.println("Password not correctly formatted; must contain at least 8 characters, a capital letter, a number, and a special character.");
            return false;
        }
        if (!checkCellPhoneNumber(cellnumber)) {
            System.out.println("Cellphone number must be in South African format (e.g., +27123456789).");
            return false;
        }
        
        this.registeredUserName = username;
        this.registeredPassWord = password;
        this.registeredCellNumber = cellnumber;
        System.out.println("User registered successfully.");
        return true;
    } 
    
    
    
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (this.registeredUserName == null || this.registeredPassWord == null) {
            return false;
        }
        return this.registeredUserName.equals(enteredUsername) && this.registeredPassWord.equals(enteredPassword);
    }
    
    public String loginStatus(boolean isLoggedIn, String username) {
        if (isLoggedIn) {
            return "Welcome " + username + ", it's great to see you again.";
        } else {
            return "Username or password is incorrect, please try again.";
        }
    }
}