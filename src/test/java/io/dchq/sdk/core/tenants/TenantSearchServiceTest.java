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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Saurabh Bhatia on 1/24/2017.
 */


/**
 * Abstracts class for holding test credentials.
 * Make Sure User has Cloud-Admin rights to create tenants
 * @author SaurabhB.
 *
 * */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class TenantSearchServiceTest extends AbstractServiceTest {

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
    public TenantSearchServiceTest(String tenantname, boolean error) {

        // random tenantName
        String prefix = RandomStringUtils.randomAlphabetic(3);
        tenantname = prefix + "-" + tenantname;

        this.tenant = new Tenant().withName(tenantname);
        this.error = error;
    }

    /*
   * Test Data creation
   * Name: Not-Empty, Max_Length:Short-Text.
   * */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"TenantSearchTest", false},
        });
    }

    //Main test, create tenant and search by name.
    @Test
    public void testSearch() throws Exception {

        logger.info("Create Tenant with Tenant Name [{}]", tenant.getName());
        ResponseEntity<Tenant> response = tenantService.create(tenant);

        if (response.getResults() != null)
            tenantCreated = response.getResults();

        //check for null response
        assertNotNull(response);
        //check response error in not null
        assertNotNull(response.isErrors());

        for (Message m : response.getMessages()) {
            logger.warn("[{}]", m.getMessageText());
            messageText = m.getMessageText();
        }

        //Through Error message if error boolean value doesn't match.
        Assert.assertEquals(messageText, error, response.isErrors());

        if (!response.isErrors()) {
            //Check for null value results
            assertNotNull("Response is null ", response.getResults());
            //Check tenant id is not null
            assertNotNull("Tenant Id is null", response.getResults().getId());
            //Check for equal value
            Assert.assertEquals("Tenant Name does not match input value", tenant.getName(), tenantCreated.getName());

        }

        // let's search for the tenant by name
        ResponseEntity<List<Tenant>> tenantResponseEntity = tenantService.search(tenant.getName(), 0, 1);

        //check response in not null
        assertNotNull(tenantResponseEntity);
        //check response error is not null
        assertNotNull(tenantResponseEntity.isErrors());
        //check response has no errors
        assertFalse(tenantResponseEntity.isErrors());

        //check result is not null
        assertNotNull(tenantResponseEntity.getResults());
        //check search result size
        assertEquals(1, tenantResponseEntity.getResults().size());

        //Fetch result
        Tenant searchedEntity = tenantResponseEntity.getResults().get(0);

        //check created tenant ID and search tenant ID are equal
        assertEquals(tenantCreated.getId(), searchedEntity.getId());
        //check created tenant name and search tenant name are equal
        assertEquals(tenantCreated.getName(), searchedEntity.getName());
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
            //Check there is no error while deleting
            Assert.assertFalse(messageText ,deleteResponse.isErrors());
        }
    }
}

