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
package org.wso2.appcloud.core.endpoints;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.appcloud.common.AppCloudException;

public class MSSEndpointsExtractor extends EndPointExtractor {

	private static Log log = LogFactory.getLog(ContextBasedEndPointExtractor.class);

	@Override
	public String getEndPoints(String deploymentUrl, String versionId) throws AppCloudException {
		String swaggerUrl = deploymentUrl + "/swagger";
		JSONArray swaggerEndpoints = new JSONArray();
		// check whether accessible
		if(getURL(swaggerUrl) == null) {
			return null;
		}
		JSONObject swaggerEndpointsBody = new JSONObject();
		swaggerEndpointsBody.put("context","/swagger");
		swaggerEndpointsBody.put("url",swaggerUrl);
		swaggerEndpoints.put(swaggerEndpointsBody);
		return createJsonResponse(null, null, null, swaggerEndpoints).toString();
	}
}