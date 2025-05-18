package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class LogListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(LogListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ThreadContext.put("testName", testName);  // Set per-thread context
        logger.info("Test Started: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Passed: {}", result.getMethod().getMethodName());
        ThreadContext.clearMap();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test Failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        ThreadContext.clearMap();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test Skipped: {}", result.getMethod().getMethodName());
        ThreadContext.clearMap();
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Suite Started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite Finished: {}", context.getName());
    }
}
