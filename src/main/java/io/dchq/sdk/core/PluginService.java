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
public interface PluginService extends GenericService<Plugin, ResponseEntity<List<Plugin>>, ResponseEntity<Plugin>> {

    /**
     * Find starred <code>Plugin</code>.
     *
     * @return Starred plugins
     */
    ResponseEntity<List<Plugin>> findByStarred();
}
