package io.dchq.sdk.core;

import com.dchq.schema.beans.one.provider.DockerServer;
import java.util.List;
import com.dchq.schema.beans.base.ResponseEntity;

/**
 * Created by atefahmed on 12/22/15.
 */
public interface DockerServerService extends GenericService<ResponseEntity<List<DockerServer>>, ResponseEntity<DockerServer>> {

    ResponseEntity<List<DockerServer>> get();

}
