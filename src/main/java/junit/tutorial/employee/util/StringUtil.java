package junit.tutorial.employee.util;

import java.util.Objects;

public class StringUtil {

    private StringUtil(){}

    public static boolean isName(String str){
        if (Objects.isNull(str))
            return false;
        return str.matches("([a-z]+(\\-|\\s))*[a-z]+");
    }

    public static boolean isNationalId(String str){
        if (Objects.isNull(str))
            return false;
        if (!str.matches("\\d{10}"))
            return false;
        int checkSum = str.charAt(9)-'0';
        int s = 0;
        for (int i=0; i<9 ; i++){
            char c = str.charAt(i);
            s += (c-'0') * (10-i);
        }
        s = s % 11;
        return (s < 2 && checkSum == s) || (s >= 2 && checkSum == (11 - s));
    }

}
