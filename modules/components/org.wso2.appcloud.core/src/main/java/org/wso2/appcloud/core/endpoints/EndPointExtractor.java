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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Abstract class for extract endpoints from the application
 */
public abstract class EndPointExtractor {

	private static Log log = LogFactory.getLog(EndPointExtractor.class);

	/**
	 * Get endpoints of the application from the container
	 * @param deploymentUrl url of the application
	 * @param versionId
	 */
	/**
	 -----------------------------------------------------------------
		 Sample JSON message we need to return

		 {
		    "data": {
		        "restEndpoints" : [{"name" : apiName, "url" : apiUrl}],
		        "soapEndpoints": [{"name" : proxyName, "wsdl" : wsdlUrl}],
		        "webEndpoints" : [{"context" : context, "url" : webUrl}],
		        "swaggerEndpoints" : [{"context" : context, "url" : swaggerUrl}]
		    }
		 }
	 -----------------------------------------------------------------
	 */
	public abstract String getEndPoints(String deploymentUrl, String versionId) throws AppCloudException;

	public JSONObject createJsonResponse (JSONArray restEndpoints, JSONArray soapEndpoints, JSONArray webEndpoints,
	                                      JSONArray swaggerEndpoints) {

		if (restEndpoints != null && restEndpoints.length() < 1) {
			restEndpoints = null;
		}

		if (soapEndpoints != null && soapEndpoints.length() < 1) {
			soapEndpoints = null;
		}

		if (webEndpoints != null && webEndpoints.length() < 1) {
			webEndpoints = null;
		}

		if (swaggerEndpoints != null && swaggerEndpoints.length() < 1) {
			swaggerEndpoints = null;
		}

		// If all four arrays does not contain any data, return null.
		if (restEndpoints == null && soapEndpoints == null && webEndpoints == null && swaggerEndpoints == null) {
			return null;
		}

		JSONObject endpoints = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("restEndpoints", restEndpoints);
		data.put("soapEndpoints", soapEndpoints);
		data.put("webEndpoints", webEndpoints);
		data.put("swaggerEndpoints", swaggerEndpoints);
		endpoints.put("data", data);

		return endpoints;
	};

	public String getURL(String urlString){
		try {
			URL url = new URL(urlString);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.connect();
			if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				log.warn("Connection to URL : " + urlString + " didnot return 200.");
				return null;
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
			StringBuilder outputBuilder = new StringBuilder();
			String output;
			while ((output = bufferedReader.readLine()) != null) {
				outputBuilder.append(output);
			}
			return outputBuilder.toString();
		} catch (ProtocolException e) {
			log.warn("HTTP URL connection throws protocol exception when trying to set request method to get. URL : "
			         + urlString);
		} catch (MalformedURLException e) {
			log.warn("URL is malformed. URL : " + urlString);
		} catch (IOException e) {
			log.warn("Cannot open connection to URL. URL : " + urlString);
		}
		return null;
	}

}
