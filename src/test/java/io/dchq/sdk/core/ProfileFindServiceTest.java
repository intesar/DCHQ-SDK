package io.dchq.sdk.core;

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.Profile;
import org.junit.Assert;

import java.util.List;

/**
 * Created by ${Sujitha} on ${1/5/2016}.
 */



import org.junit.After;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by tahsin on 12/24/15.
 */
/**
 * Abstracts class for holding test credentials.
 *
 * @author Tahsin Usmani
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class ProfileFindServiceTest extends AbstractServiceTest {


    private ProfileService profileService;

    @org.junit.Before
    public void setUp() throws Exception {
        profileService = ServiceFactory.buildProfileService(rootUrl, username, password);
    }
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"My Profile",true,20,2,2000,5000,false}
                // checking Empty Profiles


        });
    }

    private Profile profile,profileFind;
    private boolean createError,updateError;
    private Profile profileCreated;
    private String profileNameUpdated;


    public ProfileFindServiceTest(String profilename, boolean boolProfile, int containerCapacity, int cpuCapacity, int memoryCapacity, int storageCapacity,boolean success) {
        this.profile = new Profile().withName(profilename).withDefaultProfile(boolProfile).withContainerCap(containerCapacity).withCpuCap(cpuCapacity).withMemoryCap(memoryCapacity).withStorageCap(storageCapacity);
        this.createError = success;


    }
    @org.junit.Test
    public void testFind() throws Exception {

        logger.info("Create Profile with Name [{}]", profile.getName());
        ResponseEntity<Profile> response = profileService.create(profile);

        for (Message message : response.getMessages())
            logger.warn("Error while Create request  [{}] ", message.getMessageText());

        if(!response.isErrors() && response.getResults()!=null)
            profileCreated = response.getResults();

        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals("Test case CREATE Response is not as Expected ",createError, response.isErrors());

        if (!response.isErrors()) {
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());
            Assert.assertNotNull(profile.getName(), profileCreated.getName());

            // Find the Created Profile.

            response = profileService.findById(profileCreated.getId());

            // Checking for errors.
            for (Message message : response.getMessages())
                logger.warn("Error while Find request  [{}] ", message.getMessageText());

            // Validating the response
            assertNotNull(response);
            assertNotNull(response.isErrors());
            assertThat("Test case UPDATE Response is not as Expected ",updateError, is(equals(response.isErrors())));

            if(!response.isErrors() && response.getResults()!=null)
            {
                profileFind = response.getResults();
                assertNotNull(response.getResults());
                assertNotNull(response.getResults().getId());
                Assert.assertNotNull(profileCreated.getName(), profileFind.getName());
                logger.info("Profile Find Request returend Result as expected. [{}]", profileFind.getName());

            }
        }





    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (profileCreated!=null) {
            profileService.delete(profileCreated.getId());
        }
    }
}




