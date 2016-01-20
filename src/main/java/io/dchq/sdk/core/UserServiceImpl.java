package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.Users;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ User calls.
 *
 * @author Intesar Mohammed
 * @see <a href="https://dchq.readme.io/docs/user-1">User endpoint</a>
 * @since 1.0
 */
public class UserServiceImpl extends GenericServiceImpl<Users, ResponseEntity<List<Users>>, ResponseEntity<Users>>
        implements UserService {

    public static final ParameterizedTypeReference<ResponseEntity<List<Users>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<Users>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<Users>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<Users>>() {
    };

    public static final String ENDPOINT = "users/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public UserServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password,
                new ParameterizedTypeReference<ResponseEntity<List<Users>>>() {
                },
                new ParameterizedTypeReference<ResponseEntity<Users>>() {
                }
        );
    }


    @Override
    public ResponseEntity<List<Users>> get() {
        return findAll();
    }
}
