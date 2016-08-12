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

import org.json.JSONObject;
import org.json.JSONArray;
import org.wso2.appcloud.common.AppCloudException;
import org.wso2.appcloud.core.ApplicationManager;

import java.util.List;

public class ContextBasedEndPointExtractor extends EndPointExtractor {

	@Override
	public String getEndPoints(String deploymentUrl, String versionId) throws AppCloudException {
		List<String > applicationContextList = ApplicationManager.getApplicationContexts(Integer.parseInt(versionId));

		String context = "";
		if (applicationContextList != null && applicationContextList.size() > 0 && applicationContextList.get(0) != null){
			if(!applicationContextList.get(0).startsWith("/")){
				context = "/";
			}
			context += applicationContextList.get(0);
		} else {
			context = "/";
		}
		String webUrl = deploymentUrl + context;
		JSONObject webEndpointsBodyJSONObj = new JSONObject();
		webEndpointsBodyJSONObj.append("context", context);
		webEndpointsBodyJSONObj.append("url", webUrl);
		JSONArray webEndpoints = new JSONArray();
		webEndpoints.put(webEndpointsBodyJSONObj);

		// check whether accessible
		if(getURL(webUrl) == null) {
			return null;
		}
		return createJsonResponse(null, null, webEndpoints, null).toString();
	}
}
