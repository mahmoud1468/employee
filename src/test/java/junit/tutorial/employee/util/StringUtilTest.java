package junit.tutorial.employee.util;

import org.junit.jupiter.api.Test;

import static junit.tutorial.employee.util.StringUtil.isName;
import static junit.tutorial.employee.util.StringUtil.isNationalId;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void isName_WHEN_nameIsValid_THEN_returnsTrue() {
        assertTrue(isName("ali"));
        assertTrue(isName("fatemeh-eskandari"));
        assertTrue(isName("fatemeh eskandari"));
        assertTrue(isName("ali mohammad-reza"));
        assertTrue(isName("ali-mohammad reza"));
        assertTrue(isName("ali mohammad reza"));
        assertTrue(isName("qwertyuioplkjhgfdsazxcvbnm"));
    }

    @Test
    void isName_WHEN_nameIsNotValid_THEN_returnsFalse(){
        assertFalse(isName(null));
        assertFalse(isName(""));
        assertFalse(isName("ali1"));
        assertFalse(isName("maryam "));
        assertFalse(isName("-marzieh"));
        assertFalse(isName("ali  reza"));
        assertFalse(isName("Alireza"));
    }

    @Test
    void isNationalId_WHEN_nationalIdIsValid_THEN_returnsTrue() {
        assertTrue(isNationalId("5300020788"));
        assertTrue(isNationalId("0520258649"));
        assertTrue(isNationalId("0010010041"));
        assertTrue(isNationalId("0017440701"));
    }

    @Test
    void isNationalId_WHEN_nationalIdIsNotValid_THEN_returnsFalse() {
        assertFalse(isNationalId(null));
        assertFalse(isNationalId(""));
        assertFalse(isNationalId("123456789"));
        assertFalse(isNationalId("alialiali1"));
        assertFalse(isNationalId("001001004a"));
        assertFalse(isNationalId("0017440702"));
        assertFalse(isNationalId("5300020784"));
        assertFalse(isNationalId("5300020783"));
    }
}