package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.Profile;
import org.junit.Assert;

import java.util.List;

/**
 * Created by ${Sujitha} on ${1/5/2016}.
 */
public class ProfileServiceTest extends AbstractServiceTest {
    private ProfileService profileService;

    @org.junit.Before
    public void setUp() throws Exception {
        profileService = ServiceFactory.buildProfileService(rootUrl, username, password);
    }
//   // @org.junit.Test
//    public void testFindById() throws Exception {
//        ResponseEntity<Profile> responseEntity = profileService.findById("402881834d9ee4d1014d9ee5d73f0014");
//        Assert.assertNotNull(responseEntity.getResults());
//        Assert.assertNotNull(responseEntity.getResults().getId());
//
////        logger.info(responseEntity.getResults());
//    }

    @org.junit.Test
    public void testGet() throws Exception {
        ResponseEntity<List<Profile>> responseEntity = profileService.findAll();
        Assert.assertNotNull(responseEntity.getTotalElements());


        }
    }





