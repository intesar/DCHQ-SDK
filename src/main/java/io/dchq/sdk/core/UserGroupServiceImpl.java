package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.UserGroup;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ user UserGroup calls.
 *
 * @author Atef Ahmed
 * @see <a href="https://dchq.readme.io/docs/usergroups-1">UserGroup endpoint</a>
 * @since 1.0
 */
public class UserGroupServiceImpl extends GenericServiceImpl<UserGroup, ResponseEntity<List<UserGroup>>, ResponseEntity<UserGroup>>
        implements UserGroupService {

    public static final ParameterizedTypeReference<ResponseEntity<List<UserGroup>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<UserGroup>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<UserGroup>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<UserGroup>>() {
    };

    public static final String ENDPOINT = "usergroups/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public UserGroupServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password,
                new ParameterizedTypeReference<ResponseEntity<List<UserGroup>>>() {
                },
                new ParameterizedTypeReference<ResponseEntity<UserGroup>>() {
                }
        );
    }


    @Override
    public ResponseEntity<List<UserGroup>> get() {
        return findAll();
    }
}
