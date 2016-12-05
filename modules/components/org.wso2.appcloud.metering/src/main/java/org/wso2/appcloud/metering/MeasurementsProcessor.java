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
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * MeasurementsProcessor
 */
public class MeasurementsProcessor {

    public void updateApplicationUptime(String startTime, String endTime) {
        String dbName = InfluxDBUtil.getInfluxDBName();
        InfluxDB influxDB = InfluxDBUtil.getInfluxDBConnection();
        //Get application version hash IDs up and running in the given time period
        String versionHashIdSQL = "select max(value) from uptime where labels=~ /.*versionHashId.*/ and time >= '"
                + startTime + "' and time < '" + endTime + "' group by labels";
        Query labelsQuery = new Query(versionHashIdSQL, dbName);
        QueryResult labelsResult = influxDB.query(labelsQuery);
        List<ApplicationDTO> applicationDTOList = new ArrayList<ApplicationDTO>();
        for(QueryResult.Series vhIdSeris : labelsResult.getResults().get(0).getSeries()){
            String[] versionHashIds = vhIdSeris.getTags().get("labels").split("versionHashId:");
            String versionHashId = versionHashIds[1];
            String uptimeSQL = "select spread(value) from uptime where labels=~ /.*versionHashId:" + versionHashId +
                    ".*/ and time >= '" + startTime + "' and time < '" + endTime + "' group by pod_id";
            Query uptimesQuery = new Query(uptimeSQL, dbName);
            QueryResult queryResult = influxDB.query(uptimesQuery);
            Double totalUptime = 0.0;
            if(queryResult.getResults().size() > 0 && queryResult.getResults().get(0).getSeries().size() > 0){
                for(QueryResult.Series series : queryResult.getResults().get(0).getSeries()){
                    double uptime = Double.parseDouble(series.getValues().get(0).get(1).toString());
                    totalUptime = totalUptime + uptime;
                }
                System.out.println("Total Uptime " + totalUptime / (1000 * 60) );
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setStartTime(startTime);
                applicationDTO.setEndTime(endTime);
                applicationDTO.setHashId(versionHashId);
                applicationDTO.setUptime(totalUptime.toString());
                applicationDTOList.add(applicationDTO);
            }
        }
        //Batch inset uptime to the MySQL db
        try {
            Connection connection = DBUtil.getDBConnection();
            MeteringDAO meteringDAO = MeteringDAO.getInstance();
            meteringDAO.updateUptime(connection,applicationDTOList);
        } catch (AppCloudMeteringException e) {
            e.printStackTrace();
        }
    }

    public void applicationRxData() {

    }

    public void applicationTxData() {

    }

    public static void main(String[] args){
        MeasurementsProcessor measurementsProcessor = new MeasurementsProcessor();
        measurementsProcessor.updateApplicationUptime("2016-10-31T10:00:00Z","2016-10-31T11:00:00Z");
    }
}
