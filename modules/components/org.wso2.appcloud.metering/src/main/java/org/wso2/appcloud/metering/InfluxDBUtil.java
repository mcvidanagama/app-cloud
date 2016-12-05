/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.appcloud.metering;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

/**
 * InfluxDBUtil
 */
public class InfluxDBUtil {

    public static InfluxDB getInfluxDBConnection() {
        //Read IP, Port, Username, Password from a configfile.
        InfluxDB influxDB = InfluxDBFactory.connect("http://10.100.1.94:8086", "root", "root");
        return influxDB;
    }

    public static String getInfluxDBName(){
        //Read Config file and get DB Name
        return "k8s";
    }
}
