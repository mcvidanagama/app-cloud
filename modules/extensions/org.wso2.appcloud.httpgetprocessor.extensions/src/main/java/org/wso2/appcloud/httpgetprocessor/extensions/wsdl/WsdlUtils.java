/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.appcloud.httpgetprocessor.extensions.wsdl;

import org.wso2.carbon.core.transports.CarbonHttpResponse;

import java.util.Map;
import java.util.Set;

/**
 * Util class for wsdl processors
 */
public class WsdlUtils {
    public static final String tenantInfoRegex = "\\/t\\/\\w+(\\.\\w+|\\w)\\/";
    public static final String tenantInfoReplaceChar = "\\/";
    public static final String tempSuffix = ".dat";

    //WSDL Elements
    public static final String serviceLocalName = "service";
    public static final String portLocalName = "port";
    public static final String addressLocalName = "address";
    public static final String locationLocalName = "location";
    public static final String endpointLocalName = "endpoint";

    //Temp File Prefixes
    public static final String tempPrefixWsdl11 = "_nhttp_wsdl11";
    public static final String tempPrefixWsdl20 = "_nhttp_wsdl20";

    /**
     * Populate headers and other parameters from updated response to the original response
     *
     * @param updatedResponse Updated CarbonHttpResponse which contains generated wsdl info
     * @param httpResponse    Original CarbonHttpResponse which will be sent back to the client
     */
    public static void populateResponse(CarbonHttpResponse updatedResponse, CarbonHttpResponse httpResponse){
        httpResponse.setStatus(updatedResponse.getStatusCode());
        httpResponse.setError(updatedResponse.isError());
        Map<String, String> headers = updatedResponse.getHeaders();
        if (headers != null) {
            Set<String> itr = headers.keySet();
            if(itr != null) {
                for (String elem : itr) {
                    httpResponse.addHeader(elem, headers.get(elem));
                }
            }
        }
    }
}
