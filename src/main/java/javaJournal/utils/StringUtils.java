package javaJournal.utils;

public class StringUtils {
    public static String toHexString(byte[] bytes){
        String str = "";
        
        for(byte b : bytes){
            str += String.format("%02X", b);
        }
        
        return str;
    }
}
