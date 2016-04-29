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

package io.dchq.sdk.core.users;

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.base.PkEntityBase;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import com.dchq.schema.beans.one.provider.DataCenter;
import com.dchq.schema.beans.one.security.*;
import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.ProfileSearchServiceTest;
import io.dchq.sdk.core.ServiceFactory;
import io.dchq.sdk.core.UserService;
import io.dchq.sdk.core.clusters.DataCenterSearchServiceTest;
import org.hamcrest.core.Is;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.*;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * <code>UsersService</code> Integration Tests.
 *
 * @author Intesar Mohammed
 * @since 1.0
 * <p/>
 * Users:
 * Create (annonymous & ENABLED, ROLE_TENANT_ADMIN)
 * valid
 * invalid: username, email, role, pass
 * Read   (ROLE_USER for sharing)
 * Update (SELF, ROLE_TENANT_ADMIN)
 * invalid: dup username, email, role, pass
 * Delete (ROLE_TENANT_ADMIN)
 * Disabled (ROLE_TENANT_ADMIN)
 * Change ROLE (ROLE_TENANT_ADMIN)
 * Change Password (SELF, ROLE_TENANT_ADMIN)
 * Search (ROLE_USER for sharing)
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)

public class UsersCreateServiceTest extends AbstractServiceTest {

    private UserService service;

    @org.junit.Before
    public void setUp() throws Exception {
        service = ServiceFactory.buildUserService(rootUrl, username, password);
    }

    public static PkEntityBase searchDataCenter(String term) throws Exception{
        DataCenterSearchServiceTest dsst = new DataCenterSearchServiceTest();
        PkEntityBase pkeb=dsst.searchDataCenter(term);
        return pkeb;
    }

    public static Profile getProfile(String name) {
    try {
        ProfileSearchServiceTest tempProf = new ProfileSearchServiceTest();
        return tempProf.searchProfile(name);
    }catch (Exception e){
        System.out.println("ERROR :"+e.getMessage());
    }
return null;
}
    public static Organization getOrganization(String name,Boolean inActive,Boolean deleted){
      return  new Organization().withName(name).withInactive(inActive).withDeleted(deleted);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws Exception{
        return Arrays.asList(new Object[][]{
             //   {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,"All Null",true},
                {null,null,"","a@b.com",null,null,null,null,null,null,null,null,"",null,null,"Only Username",false},
             /*   {null,"Last","admin",null,null,null,null,null,null,null,null,null,null,null,null,"Only ,ln UserName",true},
                {null,"Last","testUser1",null,null,null,null,null,null,null,null,null,null,null,null,"Only ln,Username",true},
                {null,"Last","testuser1","ituser@dchq.io",null,null,null,null,null,null,null,null,null,null,null,"Only ln,Username,emailid,",true},
               {null,"","testuser1","ituser",null,null,null,getProfile("BASIC"),null,null,null,null,"aaaaaaaa",null,null,"All Mandatory Fields ln,Username,emailid,Profile,password",false},
                {null,"Last","testuser1","ituser@dchq.io","Company 1","Bkb","8019059425",getProfile("BASIC"),null,null,null,null,"aaaaaaaa",null,null,"Non Mandatory Fields ",false},
                {null,"Last","testuser1","ituser@dchq.io","Company 1","Bkb","8019059425",getProfile("BASIC"),"ff808181545040790154514a03b9013d",null,null,null,"aaaaaaaa",null,null,"Non Mandatory Fields ",false},
                {null,"Last","testuser1","ituser@dchq.io","Company 1","Bkb","8019059425",getProfile("BASIC"),null,null,null,null,"aaaaaaaa",null,null,"Non Mandatory Fields ",false},
                {null,"Last","testuser1","ituser@dchq.io","Company 1","Bkb","8019059425",getProfile("BASIC"),null,null,null,null,"aaaaaaaa",searchDataCenter("ClusterMain"),null,"Non Mandatory Fields ",false},
                {null,"Last","testuser1","ituser@dchq.io","Company 1","Bkb","8019059425",getProfile("BASIC"),null,null,null,null,"aaaaaaaa",searchDataCenter("ClusterMain1"),null,"Non Mandatory Fields ",false},
                {null,"Last","testuser1","ituser@dchq.io","Company 1","Bkb","8019059425",getProfile("BASIC"),null,null,new HashSet<String>(Arrays.asList(new String[]{"ff808181545fce5f01545fdfa72c0034","ff808181545fce5f01545fdfa72c0034"})) ,null,"aaaaaaaa",searchDataCenter("ClusterMain1"),null,"Non Mandatory Fields ",false},
                {null,"Last","testuser1","ituser@dchq.io","Company 1","Bkb","8019059425",getProfile("BASIC"),null,null,new HashSet<String>(Arrays.asList(new String[]{"ff808181545fce5f01545fdfa72c0034","ff808181545fce5f01545fdfa72c0034"})) ,null,"aaaaaaaa",searchDataCenter("ClusterMain1"),null,"Non Mandatory Fields ",false},
                {null,"Last","testuser1","ituser@dchq.io","Company 1","Bkb","8019059425",getProfile("BASIC"),null,null,new HashSet<String>(Arrays.asList(new String[]{"ff808181545fce5f01545fdfa72c0034","ff808181545fce5f01545fdfa72c0034"})) ,new ArrayList<String>(Arrays.asList(new String[]{"ROLE_USER"})),"aaaaaaaa",searchDataCenter("ClusterMain1"),null,"Non Mandatory Fields ",false},
                {null,"Last","testuser1","ituser@dchq.io","Company 1","Bkb","8019059425",getProfile("BASIC"),null,null,new HashSet<String>(Arrays.asList(new String[]{"ff808181545fce5f01545fdfa72c0034","ff808181545fce5f01545fdfa72c0034"})) ,null,"aaaaaaaa",searchDataCenter("ClusterMain1"),null,"Non Mandatory Fields ",false},
*/

              //  {null,"Last","testuser1","ituser@dchq.io",null,null,null,getProfile("BASIC"),"202881834d9ee4d1014d9ee5d73f0010",null,null,null,null,null,null,"Only fn,ln",false},
               // {"fn", "ln", "ituser1", "ituser1@dchq.io", "pass1234", "", false},
                //   {"fn", "ln", "ituser2", "ituser2@dchq.io", "pass1234", false},
                // TODO: validate password
          //      {"fn", "ln", "ituser1", "ituser3@dchq.io", "", "System Creating User with Empty Password,\n SDK Malfunction :Creating user with Empty Password", true},
                //        {"", "", "ituser2", "ituser4@dchq.io", "", false}
        });
    }

    private Users users;
    private boolean error;
    private Users userCreated;
    private String errorMessage;

    public UsersCreateServiceTest(String fn, String ln, String username,
                                  String email, String company, String title, String phoneNumber, Profile profile,
                                  String tenant, Organization organization, Set<String> userGroupId, List<String> authorities,
                                  String pass, PkEntityBase cluster, Boolean isActive, String message, boolean success) {
        this.users = new Users().withFirstname(fn).withLastname(ln).withUsername(username).withEmail(email).withPassword(pass);
        this.users.setCompany(company);
        this.users.setJobTitle(title);
        this.users.setPhoneNumber(phoneNumber);
        this.users.setProfile(profile);
        this.users.setTenantPk(tenant);
        this.users.setInactive(false);
        this.users.setOrganization(organization);
        this.users.setUserGroupIds(userGroupId);
        this.users.setAuthorities(authorities);
        this.users.setPreferredDataCenter(cluster);
        this.users.setInactive(isActive);
        this.errorMessage=message;
        this.error = success;


    }

    //@org.junit.Test
  /*  @Ignore
    public void testGet() throws Exception {
        ResponseEntity<List<Users>> responseEntity = service.findAll(0, 2000);
        Assert.assertNotNull(responseEntity.getTotalElements());
        for (Users obj : responseEntity.getResults()) {
            logger.info("User email [{}] first-name [{}] last-name [{}]", obj.getEmail(), obj.getFirstname(), obj.getLastname());
        }
    }*/

    @Test
    public void testCreate() {

        logger.info("Create user fn [{}] ln [{}] username [{}]", users.getFirstname(), users.getLastname(), users.getUsername());
        ResponseEntity<Users> response = service.create(users);

        for (Message message : response.getMessages())
            logger.warn("Error while Create request  [{}] ", message.getMessageText());



        if (response.getResults()!=null) {
            this.userCreated = response.getResults();
            logger.info("Create user Successful..");
        }

        // check response is not null
        // check response has no errors50zc
        // check response has user entity with ID
        // check all data send
        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals("Expected :\n" + errorMessage, error, response.isErrors());
/*
*
*
    public UsersCreateServiceTest(String fn, String ln, String username,
                                  String email, String company, String title, String phoneNumber, Profile profile,
                                  String tenant, Organization organization, Set<String> userGroupId, List<String> authorities,
                                  String pass, PkEntityBase cluster, Boolean isActive, String message, boolean success) {
        this.users = new Users().withFirstname(fn).withLastname(ln).withUsername(username).withEmail(email).withPassword(pass);
* */
        if (!error) {

            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            assertEquals(users.getFirstname(), userCreated.getFirstname());
            assertEquals(users.getLastname() , userCreated.getLastname());
//            assertEquals(users.getUsername() , userCreated.getUsername());
            assertEquals(users.getEmail()    , userCreated.getEmail());
            assertEquals(users.getCompany()    , userCreated.getCompany());
            assertEquals(users.getJobTitle()    , userCreated.getJobTitle());
            assertEquals(users.getProfile()    , userCreated.getProfile());
            assertEquals(users.getPhoneNumber()    , userCreated.getPhoneNumber());
            assertEquals(users.getPhoneNumber()    , userCreated.getPhoneNumber());
            assertEquals(users.getPhoneNumber()    , userCreated.getPhoneNumber());
            assertEquals(users.getPhoneNumber()    , userCreated.getPhoneNumber());
            assertEquals(users.getPhoneNumber()    , userCreated.getPhoneNumber());
            assertEquals(users.getPhoneNumber()    , userCreated.getPhoneNumber());
//            assertEquals(("ORG_ZERO"),userCreated.getOrganization().getName());
            assertEquals(users.getUserGroupIds()    , userCreated.getUserGroupIds());
            assertEquals(users.getAuthorities()    , userCreated.getAuthorities());

            if(isNullOrEmpty(users.getUserGroupIds()) && users.getUserGroups()==null)
                assertNull(userCreated.getUserGroups());

            if(!isNullOrEmpty(users.getUserGroupIds())) {
                assertNotNull(userCreated.getUserGroups());
            }

            if(!isNullOrEmpty(users.getUserGroups())) {
                assertNotNull(userCreated.getUserGroups());
            }


            if(!isNullOrEmpty(users.getPreferredDataCenter()))
            assertEquals(users.getPreferredDataCenter().getId()    ,userCreated.getPreferredDataCenter().getId());
            else
            assertNull(userCreated.getPreferredDataCenter());


            if(isNullOrEmpty(users.getTenantPk()))
            assertNotNull(userCreated.getTenantPk());
            else
                assertEquals(users.getTenantPk()    , userCreated.getTenantPk());


            // Password should always be empty
            assertThat("", is(response.getResults().getPassword()));
        }
    }

    @After
    public void cleanUp() {

        if (userCreated!=null) {
            logger.info("cleaning up...");
            service.delete(userCreated.getId());
        }
    }


}