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
package io.dchq.sdk.core.clusters;

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.base.NameEntityBase;
import com.dchq.schema.beans.one.base.UsernameEntityBase;
import com.dchq.schema.beans.one.plugin.Plugin;
import com.dchq.schema.beans.one.provider.DataCenter;
import com.dchq.schema.beans.one.security.EntitlementType;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.DataCenterService;
import io.dchq.sdk.core.ServiceFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;


/**
 * Created by Abedeen on 04/05/16.
 */
/**
 * Abstracts class for holding test credentials.
 *
 * @author Abedeen.
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class DataCenterEntitledServiceTest extends AbstractServiceTest {

    private DataCenterService dataCenterService;
    private DataCenterService dataCenterService2;

    @org.junit.Before
    public void setUp() throws Exception{
        dataCenterService = ServiceFactory.buildDataCenterService(rootUrl, username, password);
        dataCenterService2 = ServiceFactory.buildDataCenterService(rootUrl, username2, password2);
    }

    DataCenter dataCenter;
    DataCenter dataCenterCreated;
    boolean createError;
    String validationMessage;
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Test Cluster - AA4",Boolean.FALSE,EntitlementType.CUSTOM, true, userId2,"\nAll Input Values are normal. Malfunction in SDK",false},
                {"Test Cluster - AA40",Boolean.FALSE,EntitlementType.CUSTOM, false, USER_GROUP,"\n Auto Flag is set to true ,\n Next required value :Machine Compose id cannot be empty", false},
                {"Testcluster Entitled ",Boolean.FALSE,EntitlementType.CUSTOM, false, null,"\n Empty Cluster Name is Not Valid", false},
                {"Testcluster Entitled ",Boolean.FALSE,EntitlementType.CUSTOM, false, "", "\n Empty Cluster Name is Not Valid", false}
           /*     {"TestPlugin1111", "1.1", "Dummy Script", "PERL", "Apache License 2.0", EntitlementType.CUSTOM, true, userId2, "General Input", false},
                {"TestPlugin11111", "1.1", "Dummy Script", "PERL", "Apache License 2.0", EntitlementType.CUSTOM, false, USER_GROUP, "General Input", false},
                {"TestPlugin2", "1.1", "Dummy Script", "PERL", "EULA", EntitlementType.OWNER, false, null, "General Input", false},
                {"TestPlugin3", "1.1", "Dummy Script", "PERL", "EULA", EntitlementType.OWNER, false, "", "General Input", false}*/
        });
    }
    // Create - Update - Delete

    public DataCenterEntitledServiceTest(String clusterName,Boolean autoScaleFlag,EntitlementType blueprintType, boolean isEntitlementTypeUser, String entitledUserId ,String validationMessage,boolean success) {
        this.dataCenter = new DataCenter().withName(clusterName).withAutoScale(autoScaleFlag).withBlueprintEntitlementType(blueprintType);
        if (!StringUtils.isEmpty(entitledUserId) && isEntitlementTypeUser) {
            UsernameEntityBase entitledUser = new UsernameEntityBase().withId(entitledUserId);
            List<UsernameEntityBase> entiledUsers = new ArrayList<>();
            entiledUsers.add(entitledUser);
            this.dataCenter.setEntitledUsers(entiledUsers);
        } else if (!StringUtils.isEmpty(entitledUserId)) { // assume user-group
            NameEntityBase entitledUser = new NameEntityBase().withId(entitledUserId);
            List<NameEntityBase> entiledUsers = new ArrayList<>();
            entiledUsers.add(entitledUser);
            this.dataCenter.setEntitledUserGroups(entiledUsers);
        }
        this.createError=success;
        this.validationMessage=validationMessage;

    }

    @org.junit.Test
    public void testEntitle() throws Exception {

        logger.info("Create Cluster with Name [{}]", dataCenter.getName());
        if (createError)
            logger.info("Input for create Cluster is Expected to generate Error: [{}]", validationMessage);

        ResponseEntity<DataCenter> response = dataCenterService.create(dataCenter);
        for (Message message : response.getMessages())
            logger.warn("Error while Create request  [{}] ", message.getMessageText());

        if (response.getResults() != null) this.dataCenterCreated = response.getResults();

        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals(validationMessage, ((Boolean) createError).toString(), ((Boolean) response.isErrors()).toString());

        //if(!response.isErrors()  )

        if (!createError) {

            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            this.dataCenterCreated = response.getResults();

            assertEquals(dataCenter.getName(), dataCenterCreated.getName());
            assertEquals(dataCenter.getEntitledBlueprint(), dataCenterCreated.getEntitledBlueprint());
            assertEquals(dataCenter.isAutoScale(), dataCenterCreated.isAutoScale());

            // valid User2 can access plugins
            if (dataCenter.getEntitlementType() == EntitlementType.CUSTOM && !StringUtils.isEmpty(userId2)) {
                ResponseEntity<DataCenter> entitledResponse = dataCenterService2.findById(dataCenterCreated.getId());

                assertNotNull(entitledResponse);
                assertNotNull(entitledResponse.isErrors());
                Assert.assertThat(false, is(equals(entitledResponse.isErrors())));

                assertNotNull(entitledResponse.getResults());

                junit.framework.Assert.assertEquals(dataCenterCreated.getId(), entitledResponse.getResults().getId());
            } else if (dataCenter.getEntitlementType() == EntitlementType.OWNER) {
                ResponseEntity<DataCenter> entitledResponse = dataCenterService2.findById(dataCenterCreated.getId());
                assertNotNull(entitledResponse);
                assertNotNull(entitledResponse.isErrors());
                //Assert.assertThat(true, is(equals(entitledResponse.isErrors())));
                assertNull(entitledResponse.getResults());

            }


        }
    }
    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (dataCenterCreated!=null) {
            dataCenterService.delete(dataCenterCreated.getId());
        }
    }
}
