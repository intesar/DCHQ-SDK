package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.Profile;

import java.util.List;

/**
 * <code>Profile</code> API calls.
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface ProfileService extends GenericService<ResponseEntity<List<Profile>>, ResponseEntity<Profile>> {

    /**
     * Get user <code>Profile</code>
     * @return Profile response
     */
    ResponseEntity<List<Profile>> get();
}
