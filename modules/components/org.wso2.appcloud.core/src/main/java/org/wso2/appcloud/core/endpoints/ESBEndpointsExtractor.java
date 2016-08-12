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

import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.appcloud.common.AppCloudException;

public class ESBEndpointsExtractor extends EndPointExtractor{

	@Override
	public String getEndPoints(String deploymentUrl, String versionId) throws AppCloudException {
		String containerApiUrl = deploymentUrl + "/container/endpoints";
		JSONObject endpoints;

		try {
			String endpointsString = getURL(containerApiUrl);
			if(endpointsString == null) {
				return null;
			}
			JSONObject esbEndPointsOutput = new JSONObject(endpointsString);

			JSONArray restEndpoints = new JSONArray();
			JSONArray soapEndpoints = new JSONArray();

			JSONObject data = esbEndPointsOutput.getJSONObject("data");
			JSONObject urls = data.getJSONObject("urls");
			if(urls == null || urls.length() == 0){
				return null;
			}
			Object proxiesObj = urls.get("proxies");
			if (proxiesObj instanceof JSONObject) {
				JSONObject proxy = (JSONObject) proxiesObj;
				if(proxy == null || proxy.length() == 0){
					return null;
				}
				extractSoapEndpoint(deploymentUrl, soapEndpoints, proxy);
			} else if (proxiesObj instanceof JSONArray){
				JSONArray proxies = (JSONArray) proxiesObj;
				if(proxies == null || proxies.length() == 0){
					return null;
				}
				for (Object proxy : proxies) {
					extractSoapEndpoint(deploymentUrl, soapEndpoints, (JSONObject) proxy);
				}
			}

			Object apisObj = urls.get("apis");
			if (apisObj instanceof JSONObject) {
				JSONObject api = (JSONObject) apisObj;
				if(api == null || api.length() == 0){
					return null;
				}
				extractSoapEndpoint(deploymentUrl, soapEndpoints, api);
			} else if (apisObj instanceof JSONArray){
				JSONArray apis = (JSONArray) apisObj;
				if(apis == null || apis.length() == 0){
					return null;
				}
				for (Object api : apis) {
					extractRestEndpoint(deploymentUrl, restEndpoints, (JSONObject) api);
				}
			}
			endpoints = createJsonResponse(restEndpoints, soapEndpoints, null, null);
		} catch(Exception e) {
			return null;
		}

		return endpoints.toString();
	}

	private void extractRestEndpoint(String deploymentUrl, JSONArray restEndpoints, JSONObject api) {
		JSONObject restEndPointBody = new JSONObject();
		if(!api.getString("name").equals("ContainerAPI")) {
			restEndPointBody.put("name", api.getString("name"));
			restEndPointBody.put("url", deploymentUrl + api.getString("context"));
			restEndpoints.put(restEndPointBody);
		}
	}

	private void extractSoapEndpoint(String deploymentUrl, JSONArray soapEndpoints, JSONObject proxy) {
		String wsdl = proxy.getJSONArray("wsdl").get(0).toString();
		String wsdlURL = deploymentUrl + "/" + wsdl.substring(wsdl.indexOf("services"));
		JSONObject soapEndpointsBody = new JSONObject();
		soapEndpointsBody.put("name", proxy.getString("name"));
		soapEndpointsBody.put("wsdl", wsdlURL);
		soapEndpoints.put(soapEndpointsBody);
	}
}
