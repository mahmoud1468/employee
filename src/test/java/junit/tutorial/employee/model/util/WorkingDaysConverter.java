package junit.tutorial.employee.model.util;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WorkingDaysConverter implements ArgumentConverter {

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        try {
            if (source instanceof String) {
                String[] days = ((String) source).split("-");
                return Arrays.stream(days).map(DayOfWeek::valueOf).collect(Collectors.toList());
            }
        }
        catch (Exception e){
            throw new ArgumentConversionException("Could not convert source string to List<DayOfWeek>", e);
        }
        throw new ArgumentConversionException("Could not convert source object to List<DayOfWeek>");
    }
}
