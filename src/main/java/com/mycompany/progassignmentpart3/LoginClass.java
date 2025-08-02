/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progassignmentpart3;

/**
 *
 * @author lab_services_student
 */
public class LoginClass {
     public static boolean checkUsername(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public static boolean checkPasswordComplexity(String password) {
        return password.length() >= 8
            && password.matches(".*[A-Z].*")
            && password.matches(".*[^a-zA-Z0-9].*")
            && password.matches(".*\\d.*");
    }

    public static boolean checkCellphoneNumber(String cellphoneNumber) {
       
        return cellphoneNumber.matches("\\+[0-9]{11}$");
    }

    public static boolean verifyUsernameandPassword(String loginUsername, String loginPassword,
            String correctUsername, String correctPassword) {
        return loginUsername.equals(correctUsername) && loginPassword.equals(correctPassword);
    }
}
