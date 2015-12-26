package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.plugin.Plugin;

import java.util.List;

/**
 * This class encapsulates Plugin related methods
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface PluginService extends GenericService<ResponseEntity<List<Plugin>>, ResponseEntity<Plugin>> {

    /**
     * Get plugins page entitled <code>Plugins</code>
     *
     * @return ResponseEntity, list of plugins.
     */
    ResponseEntity<List<Plugin>> get();

    /**
     * Find <code>Plugin</code> by id.
     *
     * @return Specific Plugin response
     */
    ResponseEntity<Plugin> findById(String id);

    /**
     * Find <code>Plugin</code> by id.
     *
     * @return Managed Plugin response
     */
    ResponseEntity<Plugin> findManagedById(String id);

    /**
     * Get managed/authored <code>Plugin</code>.
     *
     * @return Plugin response
     */
    ResponseEntity<List<Plugin>> getManaged();

    /**
     * Find starred <code>Plugin</code>.
     *
     * @return Starred plugins
     */
    ResponseEntity<List<Plugin>> findByStarred();
}
