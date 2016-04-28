package io.dchq.sdk.core;

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


        import static junit.framework.TestCase.assertNotNull;

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
public class ProfileSearchServiceTest extends AbstractServiceTest {


    private ProfileService profileService;

    @org.junit.Before
    public void setUp() throws Exception {
        profileService = ServiceFactory.buildProfileService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Profile 6",true,20,2,2000,5000, false},
                {"Profile 6",false,20,2,2000,5000, false},
                // checking Empty Profiles
                {"",true,20,2,2000,5000, true}

        });
    }

    private Profile profile;
    private boolean success;
    private Profile profileCreated;

    public ProfileSearchServiceTest(){}
    public Profile searchProfile(String term) throws Exception{
        setUp();
        ResponseEntity<List<Profile>> response = profileService.findAll();
        for (Profile obj : response.getResults()) {
            if (obj.getName().equals(term))
                return obj;

        }
        if(response.getResults().size()>0){
            profileCreated=response.getResults().get(0);
        }

return profileCreated;
    }


    public ProfileSearchServiceTest(String profilename, boolean boolProfile, int containerCapacity, int cpuCapacity, int memoryCapacity, int storageCapacity, boolean success) {
        this.profile = new Profile().withName(profilename).withDefaultProfile(boolProfile).withContainerCap(containerCapacity).withCpuCap(cpuCapacity).withMemoryCap(memoryCapacity).withStorageCap(storageCapacity);
        this.success = success;

    }
    @org.junit.Test
    public void testCreate() throws Exception {


        ResponseEntity<Profile> response = profileService.create(profile);
        if(response.isErrors())
            logger.warn("Message from Server... {}",response.getMessages().get(0).getMessageText());

        if(success &&  !response.isErrors()){
            profileCreated = response.getResults();
            success = false;
        } else if(response.isErrors()) success =true;
        assertNotNull(response);
        assertNotNull(response.isErrors());

        Assert.assertNotNull(((Boolean)success).toString(), ((Boolean)response.isErrors()).toString());


        if (!success) {
            profileCreated = response.getResults();
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());
            Assert.assertNotNull(profile.getName(), profileCreated.getName());
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




