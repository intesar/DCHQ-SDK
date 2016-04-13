package io.dchq.sdk.core;

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.Profile;
import com.dchq.schema.beans.one.security.UserGroup;
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
public class ProfileFindAllServiceTest extends AbstractServiceTest {


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
    private boolean createError;
    private Profile profileCreated;
    private int countBeforeCreate=0,countAfterCreate=0,countAfterDelete=0;
    public int testProfilePosition(String id) {

        ResponseEntity<List<Profile>> response = profileService.findAll();

        String errors = "";
        for (Message message : response.getMessages())
            errors += ("Error while Find All request  " + message.getMessageText() + "\n");


        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertThat(false, is(equals(response.isErrors())));
        int position=0;
        if(id!=null) {
            for (Profile obj : response.getResults()) {
                position++;
                if(obj.getId().equals(id) ){
                    logger.info(" User Group Object Matched in FindAll {}  at Position : {}", id, position);
                    Assert.assertEquals("Recently Created User Group is not at Positon 1 :"+obj.getId(),1, position);
                }
            }
        }

        logger.info(" Total Number of User Groups :{}",response.getResults().size());

        return response.getResults().size();
    }

    public ProfileFindAllServiceTest(String profilename, boolean boolProfile, int containerCapacity, int cpuCapacity, int memoryCapacity, int storageCapacity,boolean success) {
        this.profile = new Profile().withName(profilename).withDefaultProfile(boolProfile).withContainerCap(containerCapacity).withCpuCap(cpuCapacity).withMemoryCap(memoryCapacity).withStorageCap(storageCapacity);
        this.createError = success;


    }
    @org.junit.Test
    public void testFindAll() throws Exception {

        // getting Count of profile before Create
        countBeforeCreate=testProfilePosition(null);

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
            logger.info("Create Profile request, completed successfully with Profile  Name [{}]", profile.getName());

            // Find the Created Profile.
            logger.info("FindAll Profile Position by Id [{}]", profileCreated.getId());
            this.countAfterCreate = testProfilePosition(profileCreated.getId());
            Assert.assertEquals("Count of FInd all user between before and after create does not have diffrence of 1 for UserId :"+profileCreated.getId(),countBeforeCreate, countAfterCreate-1);


        }





    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (profileCreated!=null) {
            profileService.delete(profileCreated.getId());
            logger.info("Find All Users After Delete  User by Id {}",profileCreated.getId());
            countAfterDelete=testProfilePosition(null);
            Assert.assertEquals("Count of FInd all user between before and after delete are not same for UserId :"+profileCreated.getId(),countBeforeCreate, countAfterDelete);
        }
    }
}




