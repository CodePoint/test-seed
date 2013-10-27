package org.codepoint.test.core.listener;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;

/**
 *
 * @author saden
 */
public class TestCoreLogger implements ISuiteListener, IInvokedMethodListener {

    private static final Logger logger = Logger.getLogger("test");

    @Override
    public void onFinish(ISuite suite) {
        logger.log(Level.INFO, "Finised {0}", suite.getName());
    }

    @Override
    public void onStart(ISuite suite) {
        logger.log(Level.INFO, "Started {0}", suite.getName());
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        logger.log(Level.INFO, " > Before {0}.{1}",
                new Object[]{
                    method.getTestMethod().getTestClass().getRealClass().getSimpleName(),
                    method.getTestMethod().getMethodName()
                });
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        logger.log(Level.INFO, " > After {0}.{1}",
                new Object[]{
                    method.getTestMethod().getTestClass().getRealClass().getSimpleName(),
                    method.getTestMethod().getMethodName()
                });
    }
}
