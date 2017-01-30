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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Abstracts class for holding test credentials.
 *
 * @author Intesar Mohammed
 * @since 1.0
 */
public abstract class AbstractServiceTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String rootUrl2 = "https://23.99.48.48:443/api/1.0/";
    protected String userId = "2c918086597e5d5f0159b00478420056";


    protected String rootUrl = "https://23.99.48.48:443/api/1.0/";
    protected String username = "iWQpNhAN8V3eskxohoYM";   // access-key for user1
    protected String password = "A3FYox0MlhnJd6BmKHExVHKzpMlXWGAJn2xZevJA";  // secret-key for user1

    protected String cloudadminusername = "OoPqBdQgMV4OpNYALhQ3";   //access key of admin
    protected String cloudadminpassword = "L0PnFJiAW0L2dcaTiXxUSPY6UMor0j33kHlscE6r"; //secret-key for admin

    // Create another user for entitlement check
    //protected String rootUrl2 = "http://40.112.248.96:8080/api/1.0/";
    protected static String userId2 = "2c918086597e5d5f0159b005070e0057";
    protected String username2 = "4QAcORxMse3d4yrKfmoV";// accesskey
    protected String password2 = "anNkl5qSFupdUThrkJw473EUmFIFNl4Z1Bt6YCU7";//secret key

    // UserGroup with userId2 entitled user
    protected static String USER_GROUP = "2c918086597e5d5f0159b00555450058";

    protected int waitTime = 0, maxWaitTime = 0;

    public boolean isNullOrEmpty(Object inObj) {
        if (inObj == null) {
            return true;
        } else if (inObj.getClass().equals(String.class)) {
            String str = inObj.toString();
            if (str == null || str.isEmpty()) return true;
            else return false;
        } else if (inObj != null) return false;

        return false;
    }

    public int wait(int milliSeconds) {
        logger.info("Waiting for [{}]  seconds  ", milliSeconds / 1000);
        if (maxWaitTime <= waitTime) {
            logger.info("wait Time Exceeded the Limit [{}]  ", formatMillis(maxWaitTime));
            return 0;
        }
        try {
            Thread.sleep(milliSeconds);
        } catch (Exception e) {
            logger.warn("Error @ Wait [{}] ", e.getMessage());
        }
        waitTime += milliSeconds;
        logger.info("Time Wait during Provisioning [{}] Hours:Minutes:Seconds ", formatMillis(waitTime));
        return 1;
    }

	public String formatMillis(long val) {
		StringBuilder buf = new StringBuilder(20);
		String sgn = "";
		if (val < 0) {
			sgn = "-";
			val = Math.abs(val);
		}
		append(buf, sgn, 0, (val / 3600000));
		append(buf, ":", 2, ((val % 3600000) / 60000));
		append(buf, ":", 2, ((val % 60000) / 1000));
		// append(buf,".",3,( val %1000));
		return buf.toString();
	}

	private void append(StringBuilder tgt, String pfx, int dgt, long val) {
		tgt.append(pfx);
		if (dgt > 1) {
			int pad = (dgt - 1);
			for (long xa = val; xa > 9 && pad > 0; xa /= 10) {
				pad--;
			}
			for (int xa = 0; xa < pad; xa++) {
				tgt.append('0');
			}
		}
		tgt.append(val);
	}

	public String getDateSuffix(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_hh_mm");
		if (format != null)
			sdf = new SimpleDateFormat(format);
		String date = sdf.format(new Date());
		return date;
	}

}
