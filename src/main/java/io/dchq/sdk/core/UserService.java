package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.Users;

import java.util.List;

/**
 * <code>User</code> API calls.
 *
 * @author Intesar Mohammed
 * @since 1.0
 */
public interface UserService extends GenericService<Users, ResponseEntity<List<Users>>, ResponseEntity<Users>> {

    /**
     * Get <code>User</code>.
     *
     * @return User response
     */
    ResponseEntity<List<Users>> get();

}
