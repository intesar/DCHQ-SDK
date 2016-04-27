package io.dchq.sdk.core;

import io.dchq.sdk.core.blueprints.BlueprintCreateServiceTest;
import io.dchq.sdk.core.blueprints.BlueprintUpdateServiceTest;
import io.dchq.sdk.core.builds.BuildCreateServiceTest;
import io.dchq.sdk.core.clusters.DataCenterCreateServiceTest;
import io.dchq.sdk.core.clusters.DataCenterUpdateServiceTest;
import io.dchq.sdk.core.groups.UserGroupCreateServiceTest;
import io.dchq.sdk.core.groups.UserGroupUpdateServiceTest;
import io.dchq.sdk.core.plugins.PluginCreateServiceTest;
import io.dchq.sdk.core.plugins.PluginUpdateServiceTest;
import io.dchq.sdk.core.providers.CloudProviderCreateServiceTest;
import io.dchq.sdk.core.providers.CloudProviderUpdateServiceTest;
import io.dchq.sdk.core.providers.RegistryAccountServiceTest;
import io.dchq.sdk.core.users.UsersCreateServiceTest;
import io.dchq.sdk.core.users.UsersUpdateServiceTest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

  /*  public static void main(String[] args) {
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


    }*/
    public static void main(String[] arg){
        System.out.println("{\"TestGroup 1\", false},");
        // Max Short Text :255,256,Speical Characters Charcters Passed

       //System.out.println("{"+StringGenenerator.generateRandomString(256)+", true},");
        // Special Characters
        /*     System.out.println("{"+StringGenenerator.DIACRITICS+", true},");
            System.out.println("{"+StringGenenerator.PUNCTUATION+", true},");
            System.out.println("{"+StringGenenerator.SYMBOL+", true},");
        // checking Empty group names
          System.out.println(" {\"\", true}");*/

    }
}
