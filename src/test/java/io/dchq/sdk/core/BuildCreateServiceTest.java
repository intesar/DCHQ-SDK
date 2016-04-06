/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dchq.sdk.core;

import com.dchq.schema.beans.base.ResponseEntity;
//import com.dchq.schema.beans.one.blueprint.Blueprint;
//import com.dchq.schema.beans.one.blueprint.BlueprintType;
import com.dchq.schema.beans.one.base.NameEntityBase;
import com.dchq.schema.beans.one.base.Visibility;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import com.dchq.schema.beans.one.blueprint.BlueprintType;
import com.dchq.schema.beans.one.build.Build;
import com.dchq.schema.beans.one.build.BuildTask;
import com.dchq.schema.beans.one.build.BuildTaskStatus;
import com.dchq.schema.beans.one.build.BuildType;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Abedeen on 04/05/16.
 */
/**
 * Abstracts class for holding test credentials.
 *
 * @author Abedeen.
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class BuildCreateServiceTest extends AbstractServiceTest {

    private BuildService buildService;

    @org.junit.Before
    public void setUp() throws Exception {
        buildService = ServiceFactory.buildBuildService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"this is a docker script",BuildType.DOCKER_FILE_CONTENT,false}
        });
    }


    private Build build;
    private boolean success;
    private Build buildCreated;

    public BuildCreateServiceTest(String dockerScript,BuildType buildType, boolean success) {
        this.build = new Build()
                .withDockerScript(dockerScript)
                .withBuildType(buildType);

        this.success = success;


    }
    @org.junit.Test
    public void testCreate() throws Exception {
        logger.info("Script Started..... {}",build.getDockerScript());
        ResponseEntity<Build> response = buildService.create(build);
        if(response.isErrors())
            logger.warn("Message from Server... {}",response.getMessages().get(0).getMessageText());
        Assert.assertNotNull(response.getResults());
        Assert.assertNotNull(response.getResults().getId());

        if (!success) {
            buildCreated = response.getResults();

            Assert.assertNotNull(build.getDockerScript(), buildCreated.getDockerScript());
            Assert.assertNotNull(build.getBuildType().toString(), buildCreated.getBuildType().toString());


        }
    }
    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (!success) {
            buildService.delete(buildCreated.getId());
        }
    }

}
