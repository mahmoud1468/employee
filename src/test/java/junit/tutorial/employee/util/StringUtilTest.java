package junit.tutorial.employee.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static junit.tutorial.employee.util.StringUtil.isName;
import static junit.tutorial.employee.util.StringUtil.isNationalId;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class StringUtilTest {

    @Tag("parametrized")
    @ParameterizedTest
    @ValueSource(strings = {"ali", "fatemeh-eskandari", "ali mohammad-reza", "qwertyuioplkjhgfdsazxcvbnm"})
    void isName_WHEN_nameIsValid_THEN_returnsTrue(String parameter) {
        assertTrue(isName(parameter));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"maryam ", "-marzieh", "ali  reza", "Alirza"})
    void isName_WHEN_nameIsNotValid_THEN_returnsFalse(String parameter){
        assertFalse(isName(parameter));
    }

    @ParameterizedTest
    @MethodSource("populateValidNationalIds")
    @Disabled
    void isNationalId_WHEN_nationalIdIsValid_THEN_returnsTrue() {
        assertTrue(isNationalId("5300020788"));
        assertTrue(isNationalId("0520258649"));
        assertTrue(isNationalId("0010010041"));
        assertTrue(isNationalId("0017440701"));
    }

    static Stream<String> populateValidNationalIds(){
        return Stream.of("5300020788","0520258649");
    }

    @Test
    @Disabled
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

    @Test
    void isNationalId_WHEN_mocked() {
        try(MockedStatic<StringUtil> mockedStatic = Mockito.mockStatic(StringUtil.class);) {
            mockedStatic.when(()->StringUtil.isNationalId("123")).thenReturn(true);
            Mockito.when(StringUtil.isNationalId("123")).thenReturn(true); // this is the same as above line
            assertTrue(isNationalId("123"));
            mockedStatic.verify(times(1), ()->StringUtil.isNationalId("123"));
        }
    }
}