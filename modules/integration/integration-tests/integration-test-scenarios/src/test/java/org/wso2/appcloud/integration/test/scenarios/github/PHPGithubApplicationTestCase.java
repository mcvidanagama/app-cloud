/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.appcloud.integration.test.scenarios.github;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.wso2.appcloud.integration.test.scenarios.AppCloudIntegrationBaseTestCase;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestConstants;
import org.wso2.appcloud.integration.test.utils.AppCloudIntegrationTestUtils;

public class PHPGithubApplicationTestCase extends AppCloudIntegrationBaseTestCase {

    private static final Log log = LogFactory.getLog(PHPGithubApplicationTestCase.class);
    public static final String PHP_SERVER_STARTED_MESSAGE = "apache2 -D FOREGROUND";
    public static final String PHP_APPLICATION_TYPE = "php";
    public static final String GIT_REPO_URL = "https://github.com/amalkasubasinghe/php_sample";
    public static final String GIT_REPO_BRANCH = "master";
    public static final String PROJECT_ROOT = "/";
    public static final String APP_CREATION_METHOD = "github";

    public PHPGithubApplicationTestCase() {
        super(AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.PHP_APP_RUNTIME_ID_KEY),
                null,
                PHP_APPLICATION_TYPE,
                AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.PHP_APP_CONTENT),
                Long.parseLong(AppCloudIntegrationTestUtils
                        .getPropertyValue(AppCloudIntegrationTestConstants.PHP_RUNTIME_START_TIMEOUT)),
                AppCloudIntegrationTestUtils
                        .getPropertyValue(AppCloudIntegrationTestConstants.PHP_AS600M3_APPLICATION_CONTEXT),
                AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.PHP_CONTAINER_SPEC),
                Boolean.parseBoolean(AppCloudIntegrationTestUtils
                        .getPropertyValue(AppCloudIntegrationTestConstants.PHP_SET_DEFAULT_VERSION)),
                AppCloudIntegrationTestUtils.getPropertyValue(AppCloudIntegrationTestConstants.PHP_DEFAULT_VERSION));
        this.gitRepoUrl = GIT_REPO_URL;
        this.gitRepoBranch = GIT_REPO_BRANCH;
        this.projectRoot = PROJECT_ROOT;
        this.appCreationMethod = APP_CREATION_METHOD;
    }

    @Override
    protected void assertLogContent(String logContent) {
        Assert.assertTrue(logContent.contains(PHP_SERVER_STARTED_MESSAGE),
                "Received log:" + logContent + " but expected line: " + PHP_SERVER_STARTED_MESSAGE);
    }
}
