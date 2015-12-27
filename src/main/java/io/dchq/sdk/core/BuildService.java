package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.build.Build;

import java.util.List;

/**
 * <code>Build</code> endpoint API calls.
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface BuildService extends GenericService<Build, ResponseEntity<List<Build>>, ResponseEntity<Build>> {

    /**
     * Get <code>Build</code> page builds
     *
     * @return List of ResponseEntity of type Build
     */
    ResponseEntity<List<Build>> get();

    /**
     * Find <code>Build</code> by id.
     *
     * @return Specific Build response
     */
    ResponseEntity<Build> findById(String id);

    /**
     * Find managed <code>Build</code> by id.
     *
     * @return Managed Build response
     */
    ResponseEntity<Build> findManagedById(String id);

    /**
     * Get a list of managed/authored <code>Build</code>.
     *
     * @return Build response
     */
    ResponseEntity<List<Build>> getManaged();

}
