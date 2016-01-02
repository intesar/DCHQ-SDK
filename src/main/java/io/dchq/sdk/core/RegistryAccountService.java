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
public interface RegistryAccountService extends GenericService<RegistryAccount, ResponseEntity<List<RegistryAccount>>, ResponseEntity<RegistryAccount>> {

    /**
     * Find <code>RegistryAccount</code> type by ID.
     *
     * @return RegistryAccount response
     */
    ResponseEntity<RegistryAccount> findRegistryAccountTypeById(String id);
}
