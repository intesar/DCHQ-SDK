package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.blueprint.RegistryAccount;

import java.util.List;

/**
 * <code>RegistryAccount</code> endpoint API calls.
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface RegistryAccountService extends GenericService<ResponseEntity<List<RegistryAccount>>, ResponseEntity<RegistryAccount>> {

    ResponseEntity<List<RegistryAccount>> get();

    /**
     * Find <code>RegistryAccount</code> by id.
     *
     * @return Specific registry account response
     */
    ResponseEntity<RegistryAccount> findById(String id);

    /**
     * Get managed/authored <code>RegistryAccount</code>.
     *
     * @return RegistryAccount response
     */
    ResponseEntity<List<RegistryAccount>> getManaged();

    /**
     * Find <code>RegistryAccount</code> type by ID.
     *
     * @return RegistryAccount response
     */
    ResponseEntity<RegistryAccount> findRegistryAccountTypeById(String id);


}
