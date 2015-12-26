package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.security.UserGroup;

import java.util.List;

/**
 * <code>UserGroup</code> API calls.
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface UserGroupService extends GenericService<ResponseEntity<List<UserGroup>>, ResponseEntity<UserGroup>> {

    /**
     * Get user's <code>UserGroup</code>.
     *
     * @return UserGroup response
     */
    ResponseEntity<List<UserGroup>> get();

}
