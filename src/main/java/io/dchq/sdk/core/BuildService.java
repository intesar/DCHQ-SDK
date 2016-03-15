package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.build.Build;
import com.dchq.schema.beans.one.build.BuildTask;

import java.util.List;

/**
 * <code>Build</code> endpoint API calls.
 *
 * @author Atef Ahmed
 * @since 1.0
 */
public interface BuildService extends GenericService<Build, ResponseEntity<List<Build>>, ResponseEntity<Build>> {

    ResponseEntity<BuildTask> buildNow(String buildId);

    ResponseEntity<BuildTask> buildNow(Build build);

    public ResponseEntity<BuildTask> findBuildTaskById(String taskId);
}
