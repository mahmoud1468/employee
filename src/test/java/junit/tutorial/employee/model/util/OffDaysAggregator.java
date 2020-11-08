package junit.tutorial.employee.model.util;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

public class OffDaysAggregator implements ArgumentsAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
        return accessor.toList().subList(3, accessor.size()).stream()
                .map(arg-> arg.equals("null")? null : LocalDate.parse((String)arg))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
