package junit.tutorial.employee.extention;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.LocalDate;

public class OddDaysExecutionCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        if (LocalDate.now().getDayOfWeek().getValue()%2 == 1)
            return ConditionEvaluationResult.disabled("Today is an even day!");
        return ConditionEvaluationResult.enabled("Today is odd day :D");
    }
}
