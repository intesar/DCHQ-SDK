package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.base.PkEntityBase;
import com.dchq.schema.beans.one.build.Build;
import com.dchq.schema.beans.one.build.BuildTask;
import org.springframework.core.ParameterizedTypeReference;

import java.io.Serializable;
import java.util.List;

/**
 * This class encapsulates Build related methods
 *
 * @author Atef Ahmed
 * @see <a href="https://dchq.readme.io/docs/builds">Build endpoint</a>
 * @since 1.0
 */
public class BuildServiceImpl extends GenericServiceImpl<Build, ResponseEntity<List<Build>>, ResponseEntity<Build>>
        implements BuildService {

    public static final ParameterizedTypeReference<ResponseEntity<List<Build>>> listTypeReference = new ParameterizedTypeReference<ResponseEntity<List<Build>>>() {
    };
    public static final ParameterizedTypeReference<ResponseEntity<Build>> singleTypeReference = new ParameterizedTypeReference<ResponseEntity<Build>>() {
    };

    public static final ParameterizedTypeReference<ResponseEntity<BuildTask>> buildTaskTypeReference = new ParameterizedTypeReference<ResponseEntity<BuildTask>>() {
    };

    public static final String ENDPOINT = "builds/";

    /**
     * @param baseURI  - e.g. https://dchq.io/api/1.0/
     * @param username - registered username with DCHQ.io
     * @param password - password used with the username
     */
    public BuildServiceImpl(String baseURI, String username, String password) {
        super(baseURI, ENDPOINT, username, password,
                new ParameterizedTypeReference<ResponseEntity<List<Build>>>() {
                },
                new ParameterizedTypeReference<ResponseEntity<Build>>() {
                }
        );
    }

    @Override
    public ResponseEntity<BuildTask> buildNow(String buildId) {
        ResponseEntity<Build> buildResponseEntity = findById(buildId);
        Build build = buildResponseEntity.getResults();
        ResponseEntity<BuildTask> buildTaskResponse = (ResponseEntity<BuildTask>) super.post(build, "/buildnow/" + build.getId(), buildTaskTypeReference);
        return buildTaskResponse;
    }

    @Override
    public ResponseEntity<BuildTask> buildNow(Build build) {
        //ResponseEntity<BuildTask> buildTaskResponse = (ResponseEntity<BuildTask>) super.post(build, "/buildnow/" + build.getId(), buildTaskTypeReference);
        ResponseEntity<BuildTask> buildTaskResponse = (ResponseEntity<BuildTask>) super.post(build, "/buildnow", buildTaskTypeReference);
        return buildTaskResponse;
    }

    @Override
    public ResponseEntity<BuildTask> findBuildTaskById(String taskId) {
        ResponseEntity<BuildTask> buildResponseEntity = (ResponseEntity<BuildTask>) find("/buildtask/" + taskId, buildTaskTypeReference);
        return buildResponseEntity;

    }
}
