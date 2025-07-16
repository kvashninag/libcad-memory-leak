/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadlib;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author root
 */
public class CadUtil {
    
    public static String toString(Pointer ptr) {
        if (ptr == null) {
            return "";  
        }
        int offset = 0;
        String s = "";
        char c = ptr.getChar(offset);
        while (c != 0) {
            s = s + c;
            offset = offset + 2;
            c = ptr.getChar(offset);
        }
        return s;

    }

    public static byte[] toByte(String str) {
        return toByte(str, (Platform.isWindows() ? "UTF-16LE" : "UTF-8"));
    }

    public static byte[] toByte(String str, String charsetName) {
        try {
            String str0 = str + '\0';
            return str0.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported encoding: " + e.getMessage());
            return new byte[]{0, 0};
        }
    }
    
    public static String toString(byte[] buf) {
        return toString(buf,(Platform.isWindows() ? "UTF-16LE" : "UTF-8"));
    } 
    
    public static String toString(byte[] buf, String charset) {
        int len = (Platform.isWindows() ? buf.length / 2 : buf.length);
        for (int index = 0; index < len; index++) {
            if (Platform.isWindows()) {
                if ((buf[index * 2] == 0) && (buf[index * 2 + 1] == 0)) {
                    len = (index + 1) * 2;
                    if (len == 2) {
                        len = 0;
                    }
                    break;
                }
            } else {
                if (buf[index] == 0) {
                    len = index;
                    break;
                }
            }
        }

        if (len == 0) {
            return "";
        }
        try {
            return new String(buf, 0, len, charset);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported encoding: " + e.getMessage());
            return "";
        }
    }
}
