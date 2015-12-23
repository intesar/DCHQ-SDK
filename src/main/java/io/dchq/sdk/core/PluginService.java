package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.plugin.Plugin;

import java.util.List;

/**
 * Created by atefahmed on 12/23/15.
 */
public interface PluginService extends GenericService<ResponseEntity<List<Plugin>>, ResponseEntity<Plugin>> {

    ResponseEntity<List<Plugin>> get();
}
