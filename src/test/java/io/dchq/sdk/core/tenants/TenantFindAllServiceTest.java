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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

/**
 * Abstracts class for holding test credentials.
 * Make Sure User has Cloud-Admin rights to create tenants
 * @author SaurabhB.
 *
 * */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class TenantFindAllServiceTest extends AbstractServiceTest {

    private TenantService tenantService;

    private Tenant tenant;
    private boolean error;
    private Tenant tenantCreated;
    private String messageText;
    private int countBeforeCreate=0,countAfterCreate=0,countAfterDelete=0;

    //Building API URL with credentials for authorization.
    @org.junit.Before
    public void setUp() throws Exception {
        tenantService = ServiceFactory.buildTenantService(rootUrl, cloudadminusername, cloudadminpassword);

    }

    //Constructor
    public TenantFindAllServiceTest(String tenantname, boolean error) {

        // random tenantName
        String prefix = RandomStringUtils.randomAlphabetic(3);
        tenantname = prefix + tenantname;

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
                {"TenantFindAllService", false},
        });
    }

    //Create tenant and fetch the Tenant by FindAll.
    @Test
    public void testFindAll() {

        countBeforeCreate=testTenantPosition(null);
        logger.info("Create Tenant with Tenant Name [{}]", tenant.getName());
        ResponseEntity<Tenant> response = tenantService.create(tenant);

        for (Message message : response.getMessages()){
            logger.warn("Error while Create request  [{}] ", message.getMessageText());
            messageText = message.getMessageText();}

        // check response is not null
        assertNotNull(response);
        // check response has no errors
        assertNotNull(response.isErrors());
        // Check for errors
        assertEquals(messageText, error, response.isErrors());

        if (!error) {

            //check result is not null
            assertNotNull(response.getResults());
            // check response has user entity with ID
            assertNotNull(response.getResults().getId());

            this.tenantCreated = response.getResults();
            logger.info("Create request successfully completed for Tenant Name [{}]",tenantCreated.getName());
            //Check created value and passed value are same
            assertEquals(tenant.getName(), tenantCreated.getName());

            logger.info("FindAll Tenant by Id [{}]", tenantCreated.getId());
            this.countAfterCreate = testTenantPosition(tenantCreated.getId());
            //Check difference of total no. of tenants before creation and after creation should be 1
            assertEquals("Count of Find all tenants between before and after create does not have difference of 1 for TenantId :"+tenantCreated.getId(),countBeforeCreate, countAfterCreate-1);
        }

    }

    //Fetching total no. of tenants and position of newly created tenant.
    public int testTenantPosition(String id) {
        ResponseEntity<List<Tenant>> response = tenantService.findAll(0,100);

        String errors = "";
        for (Message message : response.getMessages())
            errors += ("Error while Find All request: " + message.getMessageText() + "\n");

        //check for errors
        Assert.assertEquals(errors ,error, response.isErrors());

        //check response is not null
        assertNotNull(response);
        //check errors is not null
        assertNotNull(response.isErrors());
        //Match error boolean value
        assertThat(false, is(equals(response.isErrors())));
        int position=0;
        if(id!=null) {

            for (Tenant obj : response.getResults()) {
                position++;
                if(obj.getId().equals(id) ){
                    logger.info(" Tenant Object Matched in FindAll {}  at Position : {}", id, position);
                    //Match position of tenant
                    assertEquals("Recently Created Tenant is not at Positon 1 :"+obj.getId(),1, position);
                }
            }
        }

        logger.info(" Total Number of Tenants :{}",response.getResults().size());
        return response.getResults().size();
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
