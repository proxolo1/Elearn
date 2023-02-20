package com.learn.util;


import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class for validation related operations.
 *
 * <p>
 * This class is not meant to be instantiated as it only contains a collection of static methods for validation.
 * Hence, the constructor has been marked as private and throws an {@link IllegalStateException} if called.
 * </p>
 */
public class ValidationUtil {
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String KEY = "mysecretkey12aaa";
    /**
     * Private constructor to prevent instantiation of the class.
     *
     * @throws IllegalStateException always thrown when this constructor is called.
     */
    private ValidationUtil(){
        throw new IllegalStateException("Utility class");
    }
    /**
     * Checks if a given Long id is empty.
     *
     * @param id the Long id to be checked.
     * @return true if the id is empty, false otherwise.
     */
    public static boolean isBlank(Long id){
        return id.describeConstable().isEmpty();
    }
    /**
     * Checks if a given String is empty or consists only of white space characters.
     *
     * @param name the String to be checked.
     * @return true if the String is empty or consists only of white space characters, false otherwise.
     */
    public static boolean isBlank(String name){
        return name.isBlank();
    }
    /**
     * Checks if any of the getter methods of an object return an empty or white-space only String.
     *
     * @param input the Object whose getter methods are to be checked.
     * @return true if any of the getter methods return an empty or white-space only String, false otherwise.
     *
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws IllegalAccessException if this Method object is enforcing Java language access control and the underlying
     * method is inaccessible.
     */
    public static boolean isBlank(Object input) throws InvocationTargetException, IllegalAccessException {

        Method[] methods = input.getClass().getDeclaredMethods();
        for(Method method:methods){
            if(method.getName().startsWith("get") && StringUtils.isBlank(method.invoke(input).toString())){
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if both email and password are valid.
     *
     * @param email the email to be validated.
     * @param password the password to be validated.
     * @return true if both email and password are valid, false otherwise.
     */
    public static boolean isBlank(String email,String password){
        return validateEmail(email)&&validatePassword(password);
    }
    /**
     * Validates a given email address.
     *
     * @param email the email address to be validated.
     * @return true if the email address is valid, false otherwise.
     */
    public static boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
    /**
     * Validates a given email address.
     *
     * @param phoneNumber the email address to be validated.
     * @return true if the phone number  is valid, false otherwise.
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{10}$");
    }
    /**
     * Validates a given password.
     *
     * @param password the email address to be validated.
     * @return true if the password is valid, false otherwise.
     */
    public static boolean validatePassword(String password){
        return  password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }
    public static String decrypt(String encryptedPassword)  {
        int[] asciiValues = new int[encryptedPassword.length()];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encryptedPassword.length(); i++) {
            asciiValues[i] = (int) encryptedPassword.charAt(i)/10;
            char c = (char) asciiValues[i];
            sb.append(c);
        }
        return sb.toString();
    }
}
