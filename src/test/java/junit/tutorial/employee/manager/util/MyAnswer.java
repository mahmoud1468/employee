package junit.tutorial.employee.manager.util;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MyAnswer implements Answer {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Thread.sleep(2000);
        invocation.getMock();
        return null;
    }
}
