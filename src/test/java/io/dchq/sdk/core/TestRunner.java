package io.dchq.sdk.core;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(BlueprintCreateServiceTest.class,
                BlueprintUpdateServiceTest.class,
                BuildCreateServiceTest.class,
                CloudProviderCreateServiceTest.class,
                CloudProviderUpdateServiceTest.class,
                DataCenterCreateServiceTest.class,
                DataCenterUpdateServiceTest.class,
             //   DockerServerCreateServiceTest.class, //
                PluginCreateServiceTest.class,
                PluginUpdateServiceTest.class,
                ProfileCreateServiceTest.class,
                RegistryAccountServiceTest.class,
                UserGroupCreateServiceTest.class,
                UserGroupUpdateServiceTest.class,
                UsersCreateServiceTest.class,
                UsersUpdateServiceTest.class

                );
        String fail = result.getFailureCount()<=1?" Failure : ":" Failures : ";
        String totalTest = result.getRunCount()<=1?" Total Test : ":" Total Tests : ";
    System.out.println(totalTest+result.getRunCount()+fail+result.getFailureCount());
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }


    }
}
