package junit.tutorial.employee.model.util;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

import java.time.DayOfWeek;
import java.util.stream.Collectors;

public class WorkingDaysAggregator implements ArgumentsAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
        return accessor.toList()
                .stream()
                .map(d-> DayOfWeek.valueOf((String)d))
                .collect(Collectors.toList());
    }
}
