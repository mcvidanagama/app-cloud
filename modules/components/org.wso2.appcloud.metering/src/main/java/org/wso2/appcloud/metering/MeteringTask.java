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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;

/**
 * Metering Task
 */
class MeteringTask {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void beepForAnHour() {
        final Runnable beeper = new Runnable() {
            public void run() {
                System.out.println("Update Application up time");
                MeasurementsProcessor measurementsProcessor = new MeasurementsProcessor();
                String startTimeStamp = getStartTime();
                String endTimeStamp = getEndTime();
                measurementsProcessor.updateApplicationUptime(startTimeStamp, endTimeStamp);
            }
        };
        final ScheduledFuture<?> beeperHandle =
                scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() { beeperHandle.cancel(true); }
        }, 60 * 60 * 24, SECONDS);
    }

    public void updateUptimeHourly() {

    }

    /**
     * Generate start time stamp
     * @return
     */
    public static String getStartTime(){
        Calendar cst = Calendar.getInstance();
        cst.add(Calendar.HOUR_OF_DAY, -1);
        java.util.Date d = cst.getTime();
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println("Start Time :" + utcFormat.format(d) + ":00:00Z");

        return utcFormat.format(d) + ":00:00Z";
    }

    /**
     * Generate end time stamp
     * @return
     */
    public static String getEndTime(){
        Calendar cst = Calendar.getInstance();
        //cst.add(Calendar.HOUR_OF_DAY, -1);
        java.util.Date d = cst.getTime();
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println("Start Time :" + utcFormat.format(d) + ":00:00Z");

        return utcFormat.format(d) + ":00:00Z";
    }
}