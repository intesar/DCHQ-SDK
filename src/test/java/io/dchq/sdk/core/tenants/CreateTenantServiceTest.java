package io.dchq.sdk.core.tenants;

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

/**
 * Created by Saurabh Bhatia on 1/23/2017.
 */

/**
 * Abstracts class for holding test credentials.
 * Make Sure User has Cloud-Admin rights to create tenants
 * @author SaurabhB.
 *
 * */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class CreateTenantServiceTest extends AbstractServiceTest{

    private TenantService tenantService;

    private Tenant tenant;
    private boolean error;
    private Tenant tenantCreated;
    private String messageText;

    //Building API URL with credentials for authorization.
    @org.junit.Before
    public void setUp() throws Exception {
        tenantService = ServiceFactory.buildTenantService(rootUrl, cloudadminusername, cloudadminpassword);

    }

    //Constructor
    public CreateTenantServiceTest(String tenantname, boolean error) {

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
                {"Tenant", false},
                // Passing Empty string as tenant Name
          //      {"", false}
        });
    }

    //Main test, create tenant and fetch the results.
    @org.junit.Test
    public void testCreate() throws Exception {

        logger.info("Create Tenant with Tenant Name [{}]", tenant.getName());
        ResponseEntity<Tenant> response = tenantService.create(tenant);

        if (response.getResults() != null)
            tenantCreated = response.getResults();

        //check response is not null
        assertNotNull(response);
        //check error is not null
        assertNotNull(response.isErrors());

        for (Message m : response.getMessages()) {
            logger.warn("[{}]", m.getMessageText());
            messageText = m.getMessageText();
        }
        //check for errors
        Assert.assertEquals(messageText ,error, response.isErrors());


        if (!response.isErrors()) {
            //check for results
            assertNotNull("Response is null ", response.getResults());
            //check for Tenand ID
            assertNotNull("Tenant Id is null", response.getResults().getId());
            //Match Tenant Name
            Assert.assertEquals("Tenant Name does not match input value", tenant.getName(), tenantCreated.getName());

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
             //check for errors
             Assert.assertFalse(messageText ,deleteResponse.isErrors());
             }
    }
}
