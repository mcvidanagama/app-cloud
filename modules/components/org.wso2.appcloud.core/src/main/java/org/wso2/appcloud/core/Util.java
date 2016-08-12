/**
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.appcloud.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.appcloud.common.AppCloudException;
import org.wso2.appcloud.core.endpoints.EndPointExtractor;

public class Util {

    private static final long[] byteTable = createLookupTable();
    private static final long HSTART = 0xBB40E64DA205B064L;
    private static final long HMULT = 7664345821815920749L;
    private static Log log = LogFactory.getLog(Util.class);

    public static long hash(byte[] data) {
        long h = HSTART;
        final long hmult = HMULT;
        final long[] ht = byteTable;
        for (int len = data.length, i = 0; i < len; i++) {
            h = (h * hmult) ^ ht[data[i] & 0xff];
        }
        return h;
    }

    private static final long[] createLookupTable() {
        long[] byteTable = new long[256];
        long h = 0x544B2FBACAAF1684L;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 31; j++) {
                h = (h >>> 7) ^ h;
                h = (h << 11) ^ h;
                h = (h >>> 10) ^ h;
            }
            byteTable[i] = h;
        }
        return byteTable;
    }

    public static long hash(CharSequence cs) {
        long h = HSTART;
        final long hmult = HMULT;
        final long[] ht = byteTable;
        final int len = cs.length();
        for (int i = 0; i < len; i++) {
            char ch = cs.charAt(i);
            h = (h * hmult) ^ ht[ch & 0xff];
            h = (h * hmult) ^ ht[(ch >>> 8) & 0xff];
        }
        return h < 0 ? h*-1 : h;
    }

    public static String getVersionHashId (String applicationName, String versionName, int tenantId){
        if(applicationName == null || versionName == null || applicationName.isEmpty() || versionName.isEmpty()){
            return null;
        }
        String idString = tenantId + applicationName + versionName;
        return Long.toString(Util.hash(idString));
    }

    public static String getApplicationHashId (String applicationName, int tenantId){
        if(applicationName == null || applicationName.isEmpty()){
            return null;
        }
        String idString = tenantId + applicationName;
        return Long.toString(Util.hash(idString));
    }

    public static String getEndpoints(String className, String deploymentURL, String versionId) throws AppCloudException {
        try {
            Class endPointExtractorClass = Class.forName(className);
            Object endPointExtractor = endPointExtractorClass.newInstance();
            if(endPointExtractor instanceof  EndPointExtractor){
                return ((EndPointExtractor) endPointExtractor).getEndPoints(deploymentURL, versionId);
            } else {
                String msg = "Endpoint Extractor has to be an implementation of " + EndPointExtractor.class.getName();
                log.error(msg);
                throw new AppCloudException(msg);
            }
        } catch (ClassNotFoundException e) {
            String msg = "Class not found : " + className;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } catch (InstantiationException e) {
            String msg = "Error while creating an instance of class : " + className;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = "Error while creating an instance of class : " + className;
            log.error(msg, e);
            throw new AppCloudException(msg, e);
        }
    }
}
