/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.plugin.Plugin;

import com.dchq.schema.beans.one.security.EntitlementType;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;


import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Abedeen on 04/05/16.
 */

/**
 * Abstracts class for holding test credentials.
 *
 * @author Abedeen.
 * @since 1.0
 */
class Obj {

    int[] testMaxCount;
    int totalTestCase;
    int caseItems;
    int[] counter;
    int[] move;
    String content;
    Object contentObjecct;
    int loopCount = 0;

    public void arrangeCounter() {

        for (int i = caseItems - 2; i >= 0; i--) {
            if (loopCount > 0) {
                if (counter[i] + 1 < testMaxCount[i]) {
                    loopCount = 0;
                    counter[i]++;
                } else if (counter[i] + 1 == testMaxCount[i]) counter[i] = 0;
            }
        }
    }


    public Obj(int[] testMaxCount, int totalTestCase, int caseItems) {
        this.testMaxCount = testMaxCount;
        this.totalTestCase = totalTestCase;
        this.caseItems = caseItems;
        counter = new int[testMaxCount.length];
        move = new int[testMaxCount.length];
        for (int i = 0; i < move.length; i++) {
            if (i == move.length - 1) move[i] = 1;
            else move[i] = 0;
        }
    }

    public int[] getTestMaxCount() {
        return testMaxCount;
    }

    public void setTestMaxCount(int[] testMaxCount) {
        this.testMaxCount = testMaxCount;
    }

    public int getTotalTestCase() {
        return totalTestCase;
    }

    public void setTotalTestCase(int totalTestCase) {
        this.totalTestCase = totalTestCase;
    }

    public int getCaseItems() {
        return caseItems;
    }

    public void setCaseItems(int caseItems) {
        this.caseItems = caseItems;
    }

}

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class PluginCreateServiceTest extends AbstractServiceTest {

    private PluginService appService;

    @org.junit.Before
    public void setUp() throws Exception {
        appService = ServiceFactory.buildPluginService(rootUrl, username, password);
    }

    private Plugin plugin;
    private boolean success;
    private Plugin pluginCreated;
    private String validityMessage;

    @Parameterized.Parameters(name = "Name: {3}")
    public static Iterable<Object[]> generateParameters() {

        List<Object[]> governamce = new ArrayList<Object[]>();
        governamce.add(new Object[]{"*", "*", "*", "shell,perl,python,ruby"});


        List<Object[]> result = new ArrayList<Object[]>();
        result.add(new Object[]{"Hero", "h1", "h2"});
        result.add(new Object[]{"Dummy_test_Plugin", "", "121"});
        result.add(new Object[]{"pulgin Sript"});
        result.add(new Object[]{"shell", "perl", "python", "ruby", "", "Rails"});

        result = testCaseGovernance(governamce, result);
        return result;
    }

    public PluginCreateServiceTest(String pluginName, String pluginScript, String scriptType,
                                   String validityMessage, boolean errors) {
        this.plugin = new Plugin();
        this.plugin.setName(pluginName);
        //    this.plugin.setVersion(version);
        this.plugin.setBaseScript(pluginScript);
        this.plugin.setScriptLang(scriptType);
        //    this.plugin.setLicense(license);

        this.success = errors;
        this.validityMessage = validityMessage;

    }

    public static List<Object[]> testCaseGovernance(List<Object[]> governamce, List<Object[]> sampleCase) {
        int[] testMaxCount = getItemcount(sampleCase);
        int testCaseCount = getItemcountFactorial(sampleCase);
        int testCase = testMaxCount.length;

        System.out.println( "Combination of Test Cases :" + testCaseCount);
        System.out.println( "Generating Combination of test cases....");
        int[] counter = new int[testMaxCount.length];
        Obj calculation = new Obj(testMaxCount, testCaseCount, testCase);
        List<Object[]> result = new ArrayList<Object[]>();
        for (int i = 0; i < testCaseCount; i++) {
            Object[] temptestCase = new Object[testMaxCount.length];
            for (int j = 0; j < testCase; j++) {
                Object[] ob = sampleCase.get(j);
                calculation = getObjectItem(sampleCase, calculation, j);
                temptestCase[j] = calculation.contentObjecct;
                if (j < testCase - 1) System.out.print(" , ");
            }
            result.add(temptestCase);

        }
        System.out.println("Total Number of for Test Cases : "+result.size());
        System.out.println("Generating Governance for Test Cases");
        for(int i=0;i<governamce.size();i++)
        {
            Object[] govCase =governamce.get(i);
            for(int j=0;j<result.size();j++)
            {
                Object[] singleCase =result.get(i);
                singleCase=getExpectedResult(govCase,singleCase);

            }


        }
        return result;
    }

    public static  Object[] getExpectedResult(Object[] govCase,Object[] singlecase)
    {
        Object[] expectTestResult= new Object[singlecase.length+1];

        return null;
    }
    @org.junit.Test
    public void testDummyCreate() throws Exception {


    }

    public static Obj getItem(List<Object[]> sampleCase, Obj sc, int pos) {

        Object[] test = sampleCase.get(pos);

        int nextCount = sc.counter[pos];
        sc.content = test[nextCount].toString();
        if (sc.counter[pos] == sc.testMaxCount[pos] - 1 && sc.move[pos] == 1) {
            sc.counter[pos] = 0;
            sc.loopCount++;
            sc.arrangeCounter();
        } else if (sc.move[pos] == 1)
            sc.counter[pos]++;
        return sc;
    }

    public static Obj getObjectItem(List<Object[]> sampleCase, Obj sc, int pos) {

        Object[] test = sampleCase.get(pos);

        int nextCount = sc.counter[pos];
        sc.contentObjecct = test[nextCount];
        if (sc.counter[pos] == sc.testMaxCount[pos] - 1 && sc.move[pos] == 1) {
            sc.counter[pos] = 0;
            sc.loopCount++;
            sc.arrangeCounter();
        } else if (sc.move[pos] == 1)
            sc.counter[pos]++;
        return sc;
    }

    public static int[] getItemcount(List<Object[]> sampleCase) {
        Object[] ob = sampleCase.get(0);
        String itemCount = "";
        int count = 1;
        for (int i = 0; i < sampleCase.size(); i++) {
            Object[] obj = sampleCase.get(i);
            itemCount += obj.length + ",";
        }
        String[] numberStrs = itemCount.split(",");
        int[] numbers = new int[numberStrs.length];
        for (int i = 0; i < numberStrs.length; i++)
            numbers[i] = Integer.parseInt(numberStrs[i]);

        return numbers;
    }

    public static int getItemcountFactorial(List<Object[]> sampleCase) {
        Object[] ob = sampleCase.get(0);
        String itemCount = "";
        int count = 1;
        for (int i = 0; i < sampleCase.size(); i++) {
            Object[] obj = sampleCase.get(i);
            itemCount += obj.length + ",";
        }
        String[] numberStrs = itemCount.split(",");
        int numbers = 1;
        for (int i = 0; i < numberStrs.length; i++)
            numbers *= Integer.parseInt(numberStrs[i]);

        return numbers;
    }

    public static int getTestCaseCount(int number) {
        int factorial = number;

        for (int i = (number - 1); i > 1; i--) {
            factorial = factorial * i;
        }
        return factorial;
    }

    //  @org.junit.Test
    public void testCreate() throws Exception {

        logger.info("Create Group with Group Name [{}]", this.plugin.getName());
        ResponseEntity<Plugin> response = appService.create(plugin);

        if (response.isErrors())
            logger.warn("Message from Server... {}", response.getMessages().get(0).getMessageText());

        assertNotNull(response);
        assertNotNull(response.isErrors());

        Assert.assertNotNull(((Boolean) success).toString(), ((Boolean) response.isErrors()).toString());

        if (!response.isErrors() && response.getResults() != null) {
            pluginCreated = response.getResults();
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            // Comparing objects agains created Objects.
            Assert.assertNotNull(plugin.getName(), pluginCreated.getName());
            Assert.assertNotNull(plugin.getName(), pluginCreated.getName());
            Assert.assertNotNull(plugin.getVersion(), pluginCreated.getVersion());
            Assert.assertNotNull(plugin.getBaseScript(), pluginCreated.getBaseScript());
            Assert.assertNotNull(plugin.getScriptLang(), pluginCreated.getScriptLang());
            Assert.assertNotNull(plugin.getLicense(), pluginCreated.getLicense());
            Assert.assertNotNull(plugin.getEntitlementType().toString(), pluginCreated.getEntitlementType().toString());

        }

    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (pluginCreated != null) {
            appService.delete(pluginCreated.getId());
        }
    }
}
