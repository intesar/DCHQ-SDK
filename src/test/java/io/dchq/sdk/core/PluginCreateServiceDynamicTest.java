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
import com.dchq.schema.beans.one.base.Gist;
import com.dchq.schema.beans.one.container.Env;
import com.dchq.schema.beans.one.plugin.Plugin;

import com.dchq.schema.beans.one.security.EntitlementType;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
class TestCaseCobinations {

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


    public TestCaseCobinations(int[] testMaxCount, int totalTestCase, int caseItems) {
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



}

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class PluginCreateServiceDynamicTest extends AbstractServiceTest {

    public static List<Object[]> testCaseGovernance(List<Object[]> governamce, List<Object[]> sampleCase) {
        int[] testMaxCount = getItemcount(sampleCase);
        int testCaseCount = getItemcountFactorial(sampleCase);
        int testCase = testMaxCount.length;

        System.out.println( "Combination of Test Cases :" + testCaseCount);
        System.out.println( "Generating Combination of test cases....");
        int[] counter = new int[testMaxCount.length];
        TestCaseCobinations calculation = new TestCaseCobinations(testMaxCount, testCaseCount, testCase);
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
                Object[] singleCase =result.get(j);
                    singleCase=getExpectedResult(govCase,singleCase);
                //expectedResult.add(getExpectedResult(govCase,singleCase));
                  result.set(j,singleCase);

            }


        }
        return result;
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

    public static TestCaseCobinations getObjectItem(List<Object[]> sampleCase, TestCaseCobinations sc, int pos) {

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
    public static  Object[] getExpectedResult(Object[] govCase,Object[] singleCase)
    {
        Object[] expectTestResult= new Object[singleCase.length+1];
        boolean expectResult=false;

        String govString ="";
        String caseString ="";
         for(int i=0;i<govCase.length;i++)
        {
            govString =govCase[i].toString();
            if(singleCase[i]!=null)
            caseString =singleCase[i].toString();
            else caseString="";
            expectTestResult[i]=singleCase[i];

                if(govString.equals("*")) govString=".+";
                else if (govString.equals("")) govString=".*";
                else if (govString.equals(caseString)) govString=caseString;
                else if (govString.contains(caseString ) && !caseString.equals("")) govString=caseString;

                Pattern p = Pattern.compile(govString);
                Matcher m = p.matcher(caseString);
                //   System.out.println(m.find());

            if(!expectResult)
                    if (!m.matches()) {
                        expectResult = true;
                        //break ruleLoop;
                    }
            }

   //     }

        expectTestResult[singleCase.length]=expectResult;


        return expectTestResult;
    }
    private PluginService appService;
public static Set<Env> getEnviroment(String name,boolean isHidden,String property,String val){
     Set<Env> envs = new HashSet<>();
    envs.add(new Env().withEval(name).withHidden(isHidden).withProp(property).withVal(val));
    return envs;
}

    public static Gist getGist(String name, boolean visible, String property, String val){

        Gist g =new Gist();
        g.setId(name);
        g.setPublicVisible(visible);
        g.setChecksum(property);
        g.setUrl(val);
        return g;
    }
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
        governamce.add(new Object[]{"*", "*","SHELL,PERL,PYTHON,RUBY","Apache License 2.0, EULA","","","","","",""});
      //  governamce.add(new Object[]{"*", "*","Apache License 2.0, EULA","SHELL,PERL,PYTHON,RUBY","","*"});


        List<Object[]> result = new ArrayList<Object[]>();
        result.add(new Object[]{"Dummy_test_Plugin", ""});
        result.add(new Object[]{"pulgin Sript"});
        result.add(new Object[]{"SHELL", "PERL", "PYTHON", "RUBY", ""});
        result.add(new Object[]{"Apache License 2.0", "EULA",""});
        result.add(new Object[]{"This is Sample Descrition", ""});
        result.add(new Object[]{EntitlementType.CUSTOM,EntitlementType.OWNER,null});
        result.add(new Object[]{true,false});
        result.add(new Object[]{null,getEnviroment("Eval1",true,"Property1","val2"),getEnviroment("",true,"Property1","val2"),getEnviroment("Eval1",false,"Property1","val2"),
                getEnviroment("Eval1",true,"","val2"),getEnviroment("Eval1",true,"Property1","")});
        result.add(new Object[]{true,false});
        result.add(new Object[]{null,getGist("Eval1",true,"Property1","val2"),getGist("",true,"Property1","val2"),getGist("Eval1",false,"Property1","val2"),
                getGist("Eval1",true,"","val2"),getGist("Eval1",true,"Property1","")});




        result = testCaseGovernance(governamce, result);
        return result;
    }

    public PluginCreateServiceDynamicTest(String pluginName, String pluginScript, String scriptType,
                                          String license,String description , EntitlementType entitlementType,boolean inactiveFlag,Set<Env> env,boolean deleted,Gist gist,boolean errors) {
        this.plugin = new Plugin();
        this.plugin.setName(pluginName);
        //    this.plugin.setVersion(version);
        this.plugin.setBaseScript(pluginScript);
        this.plugin.setScriptLang(scriptType);
        this.plugin.setLicense(license);
        this.plugin.setDescription(description);
        this.plugin.setEntitlementType(entitlementType);
        this.plugin.setInactive(inactiveFlag);
        this.plugin.setEnvs(env);
        this.plugin.setDeleted(deleted);
        this.plugin.setGist(gist);

        this.success = errors;

        // this.validityMessage = validityMessage;

    }

    @org.junit.Test
    public void testDummyCreate() throws Exception {
        logger.info("|pluginName[{}] |  pluginScript[{}] | scriptType[{}] | license[{}] | description[{}] | getEntitlementType[{}] |isInactive[{}] |  createError [{}] "
                , plugin.getName(),  plugin.getBaseScript(),  plugin.getScriptLang(),
                plugin.getLicense(), plugin.getDescription() ,  plugin.getEntitlementType(),plugin.isInactive(), success);
    }
    @org.junit.Test
    public void testCreate() throws Exception {
        testDummyCreate();
        logger.info("Create Object Request with Name [{}]", this.plugin.toString());
        ResponseEntity<Plugin> response = appService.create(plugin);

        if (response.isErrors())
            logger.warn("Message from Server... {}", response.getMessages().get(0).getMessageText());

        if(response.getResults()!=null)
            pluginCreated = response.getResults();

        assertNotNull(response);
        assertNotNull(response.isErrors());
    //    Assert.assertEquals(success, response.isErrors());

        if (!response.isErrors() ) {

            logger.info("Create Object Successful with Name [{}], Expected Create Error [{}] / Response Error [{}]", this.plugin.getName(),success,response.isErrors());
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            // Comparing objects agains created Objects.
            Assert.assertEquals("Plugin Name ",plugin.getName(), pluginCreated.getName());
            Assert.assertEquals(plugin.getName(), pluginCreated.getName());
//            Assert.assertEquals(plugin.getVersion(), pluginCreated.getVersion());
            Assert.assertEquals(plugin.getBaseScript(), pluginCreated.getBaseScript());
            Assert.assertEquals(plugin.getScriptLang(), pluginCreated.getScriptLang());
            Assert.assertEquals(plugin.getLicense(), pluginCreated.getLicense());
            Assert.assertEquals(plugin.getDescription(), pluginCreated.getDescription());
//            Assert.assertEquals(plugin.isInactive(), pluginCreated.isInactive());
            Assert.assertEquals(plugin.getEnvs(), pluginCreated.getEnvs());
//            Assert.assertNotSame(plugin.getEntitlementType(), pluginCreated.getEntitlementType());

        }

    }

    @After
    public void cleanUp() {


        if (pluginCreated != null) {
            logger.info("cleaning up...");
            appService.delete(pluginCreated.getId());
        }
    }
}
