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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstracts class for holding test credentials.
 *
 * @author Intesar Mohammed
 * @since 1.0
 */
public abstract class AbstractServiceTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String rootUrl2 = "http://40.112.248.96:8080/api/1.0/";
    protected String rootUrl = "http://localhost:8080/api/1.0/";
    protected String username = "admin@dchq.io";
    protected String password = "admin123";

    // Create another user for entitlement check
    protected static String userId2 = "ff808181542bf58901542cc78cbc00b2";
    protected String username2 = "F9MM2rzkWlmGkRyWwQsx";// accesskey
    protected String password2 = "6O4YYEbJVMXckLd4p5yAgZZwHKPD02MkOIq9JriI";//secret key

    // UserGroup with userId2 entitled user
    protected static String USER_GROUP ="ff808181542bf58901542cc8344a00b3";

    protected int waitTime = 0,maxWaitTime=0;

    public int wait(int milliSeconds) throws Exception {
        logger.info("Waiting for [{}] seconds  ",milliSeconds);
        if (maxWaitTime<=waitTime) {
            logger.info("wait Time Exceeded the Limit [{}]  ", (maxWaitTime / 1000 / 60));
            return 0;
        }
        Thread.sleep(milliSeconds);
        waitTime+=milliSeconds;
        logger.info("Time Wait during Provisioning [{}] Minutes ", (waitTime / 1000 / 60));
        return 1;
    }
}
