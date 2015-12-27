package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.Profile;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Encapsulates DCHQ user Profile calls.
 *
 * @author Atef Ahmed
 * @see <a href="https://dchq.readme.io/docs/profiles-1">Profile endpoint</a>
 * @since 1.0
 */
public class ProfileServiceImpl extends GenericServiceImpl<Profile, ResponseEntity<List<Profile>>, ResponseEntity<Profile>>
        implements ProfileService {

    public static final ParameterizedTypeReference<ResponseEntity<List<Profile>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<Profile>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<Profile>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<Profile>>() {
    };

    public static final String ENDPOINT = "profiles/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public ProfileServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password,
                new ParameterizedTypeReference<ResponseEntity<List<Profile>>>() {
                },
                new ParameterizedTypeReference<ResponseEntity<Profile>>() {
                }
        );
    }


    @Override
    public ResponseEntity<List<Profile>> get() {
        return findAll();
    }
}
