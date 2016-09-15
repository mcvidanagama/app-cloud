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

import org.apache.axiom.ext.io.StreamCopyException;
import org.apache.axiom.util.blob.BlobOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.transports.CarbonHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Set;

/**
 * Util class for wsdl processors which contains url overwrite methods and properties
 */
public class WsdlUtils {
    private static final Log log = LogFactory.getLog(WsdlUtils.class);

    //Temp File Prefixes/suffixes
    public static final String tempSuffix = ".dat";
    public static final String tempPrefixWsdl11 = "_nhttp_wsdl11";
    public static final String tempPrefixWsdl20 = "_nhttp_wsdl20";

    public static final String hostName = System.getenv("APP_HOST");
    public static final String regexPattern = ":\\/\\/" + hostName + "(:?)(\\d*)\\/(.*?)\\/t\\/.*?\\/";
    public static final String replacementString = ":\\/\\/" + hostName + "$1$2\\/$3/";

    /**
     * Overwrite wsdl endpoint urls contained in response and populate updatedResponse with the result
     *
     * @param clientResponse CarbonHttpResponse which contains updated information
     * @param axis2Response CarbonHttpResponse created from axis2 wsdl processors
     * @throws Exception
     */
    public static void updateResponse(CarbonHttpResponse clientResponse, CarbonHttpResponse axis2Response) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (clientResponse.getOutputStream() != null) {
            try {
                ((BlobOutputStream) axis2Response.getOutputStream()).getBlob().writeTo(byteArrayOutputStream);
                byte[] output = removeTenantInfo(byteArrayOutputStream.toByteArray());
                (((BlobOutputStream) clientResponse.getOutputStream()).getBlob()).readFrom(new ByteArrayInputStream(output), output.length);
            } catch (StreamCopyException streamCopyException) {
                log.error("Error in updating WSDL : fail to read response output stream ", streamCopyException);
                throw new Exception(streamCopyException);
            }

            //Populate other properties to new response
            clientResponse.setStatus(axis2Response.getStatusCode());
            clientResponse.setError(axis2Response.isError());
            Map<String, String> headers = axis2Response.getHeaders();
            if (headers != null) {
                Set<String> itr = headers.keySet();
                if (itr != null) {
                    for (String elem : itr) {
                        clientResponse.addHeader(elem, headers.get(elem));
                    }
                }
            }

        }
    }

    /**
     * Remove tenant information (/t/(tenant-id)) from endpoint urls
     *
     * @param data byte array of data to be updated
     * @return byte array of updated data
     */
    private static byte[] removeTenantInfo(byte[] data) {
        String wsdlString = new String(data);
        return wsdlString.replaceAll(regexPattern, replacementString).getBytes();
    }
}
