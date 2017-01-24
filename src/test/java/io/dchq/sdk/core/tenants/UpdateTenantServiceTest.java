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
 * Abstracts class for holding test credentials.
 * Make Sure User has Cloud-Admin rights to create / update tenants
 * @author SaurabhB.
 *
 * */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class UpdateTenantServiceTest extends AbstractServiceTest {

    private TenantService tenantService;

    private Tenant tenant;
    private boolean error;
    private Tenant tenantCreated;
    private String messageText;
    private String updatedTenantName;

    //Building API URL with credentials for authorization.
    @org.junit.Before
    public void setUp() throws Exception {
        tenantService = ServiceFactory.buildTenantService(rootUrl, cloudadminusername, cloudadminpassword);

    }

    //Constructor
    public UpdateTenantServiceTest(String tenantname, String updatedTenantName, boolean error) {
        this.tenant = new Tenant().withName(tenantname);
        this.updatedTenantName = updatedTenantName;
        this.error = error;
    }

    /**
    * Test Data creation
    * */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //positive case
                {"TenantCreation", "Update", false},
               // Updating with Empty group names
             //    {"Test for Updating with Empty", "", false}

        });
    }

    //Main test, create tenant, update and fetch the results.
    @org.junit.Test
    public void testUpdate() throws Exception {

        //Creating new Tenant
        logger.info("Create Tenant with Tenant Name [{}]", tenant.getName());
        ResponseEntity<Tenant> response = tenantService.create(tenant);

        for (Message message : response.getMessages()){
            logger.warn("Error while Create request  [{}] ", message.getMessageText());
            messageText = message.getMessageText();}

        //Check for response
        assertNotNull(response);
        //Check error is not null
        assertNotNull(response.isErrors());
        //Check for error boolean response
        Assert.assertNotNull(((Boolean) false).toString(), ((Boolean) response.isErrors()).toString());
        //check for errors
        Assert.assertFalse(messageText , response.isErrors());

        if (!response.isErrors() && response.getResults() != null) {
            tenantCreated = response.getResults();
            //check for results
            assertNotNull(response.getResults());
            //check tenant id is not null
            assertNotNull(response.getResults().getId());
            //Match Tenant Name
            Assert.assertNotNull(tenant.getName(), tenantCreated.getName());
            tenantCreated.setName(this.updatedTenantName);

            //Updating Tenant
            logger.info("Update Request for Tenant with Tenant Name [{}]", tenant.getName());
            response = tenantService.update(tenantCreated);

            for (Message message : response.getMessages()){
                logger.warn("Error while Update request  [{}] ", message.getMessageText());
                messageText = message.getMessageText();
            }

            //Check response is not null
            assertNotNull(response);

            if (response.isErrors()){
                Assert.fail(messageText);
            }
            else {
                //check result is not null
                Assert.assertNotNull(response.getResults());
                //check updated tenant name is not null
                Assert.assertNotNull(response.getResults().getName(), updatedTenantName);
            }
        }
    }

    //Clean-Up script will Delete the above created tenant
    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (tenantCreated != null) {
            ResponseEntity<Tenant> deleteResponse =  tenantService.delete(tenantCreated.getId());
            if (deleteResponse.getResults() != null)
                deleteResponse.getResults();
            for (Message m : deleteResponse.getMessages()){
                logger.warn("[{}]", m.getMessageText());
                messageText = m.getMessageText();}

            //check for errors
            Assert.assertFalse(messageText ,deleteResponse.isErrors());
        }
    }

}
