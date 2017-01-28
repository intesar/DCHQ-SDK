package io.dchq.sdk.core.tenants;

/**
 * Created by Saurabh Bhatia on 1/24/2017.
 */


import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.Tenant;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.ServiceFactory;
import io.dchq.sdk.core.TenantService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Abstracts class for holding test credentials.
 * Make Sure User has Cloud-Admin rights to create tenants
 * @author SaurabhB.
 *
 * */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class TenantFindServiceTest extends AbstractServiceTest {

    private TenantService tenantService;

    private Tenant tenant;
    private boolean error;
    private Tenant tenantCreated;
    private Tenant tenantFindByID;
    private String messageText;

    //Building API URL with credentials for authorization.
    @org.junit.Before
    public void setUp() throws Exception {
        tenantService = ServiceFactory.buildTenantService(rootUrl, cloudadminusername, cloudadminpassword);

    }

    //Constructor
    public TenantFindServiceTest(String tenantname, boolean error) {

        // random tenantName
        String prefix = RandomStringUtils.randomAlphabetic(3);
        tenantname = prefix + "-" + tenantname;

        this.tenant = new Tenant().withName(tenantname);
        this.error = error;
    }

    /*
    * Test Data creation
    * Name: Not-Empty, Max_Length:Short-Text, Unique per System.
    * */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"TenantFindService", false},
                // Passing Empty string as tenant Name
                //      {"", false}
        });
    }

    //Main test, create tenant and fetch the Tenant ID.
    @org.junit.Test
    public void testFind() throws Exception {

        logger.info("Create Tenant with Tenant Name [{}]", tenant.getName());
        ResponseEntity<Tenant> response = tenantService.create(tenant);

        for (Message message : response.getMessages()) {
            logger.warn("Error while Create request  [{}] ", message.getMessageText());
            messageText = message.getMessageText();
        }

        //check for null response
        assertNotNull(response);
        //check for null error
        assertNotNull(response.isErrors());
        //check for error message
        Assert.assertEquals(messageText ,error, response.isErrors());

        if (!response.isErrors() && response.getResults() != null) {
            tenantCreated = response.getResults();
            //check for null result
            assertNotNull(response.getResults());
            //check for null ID
            assertNotNull(response.getResults().getId());
            //check for tenant name
            Assert.assertNotNull(tenant.getName(), tenantCreated.getName());

            logger.info("Find Request for Tenant with Tenant ID [{}]", tenantCreated.getId());
            response = tenantService.findById(tenantCreated.getId());

            for (Message message : response.getMessages()){
                logger.warn("Error while Find request  [{}] ", message.getMessageText());
                messageText = message.getMessageText();}

            //check for error message
            Assert.assertEquals(messageText ,error, response.isErrors());
            //check for null response
            assertNotNull(response);
            //check for null error
            assertNotNull(response.isErrors());

            if (!response.isErrors()) {
                tenantFindByID=response.getResults();
                //check for null result
                Assert.assertNotNull(response.getResults());
                //Match Tenant name Find by ID
                assertEquals(tenantCreated.getName(), tenantFindByID.getName());

            }
        }
    }

    //Clean-Up script will Delete the above created tenant
    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (tenantCreated != null) {
            ResponseEntity<Tenant> deleteResponse  =  tenantService.delete(tenantCreated.getId());

            for (Message m : deleteResponse.getMessages()){
                logger.warn("[{}]", m.getMessageText());
                messageText = m.getMessageText();}
            //Check for error message
            Assert.assertFalse(messageText ,deleteResponse.isErrors());
        }
    }

}
