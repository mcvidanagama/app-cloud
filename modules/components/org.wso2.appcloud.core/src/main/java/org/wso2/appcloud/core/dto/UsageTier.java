/*
 *   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 */

package org.wso2.appcloud.core.dto;

/**
 * Represents tenant subscription plan information.
 */
public class UsageTier {

    private String planName;
    private int maxApplicationCount;
    private int maxDatabaseCount;
    private int maxReplicaCount;
    private int cpuLimit;
    private int ramLimit;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getMaxApplicationCount() {
        return maxApplicationCount;
    }

    public void setMaxApplicationCount(int maxApplicationCount) {
        this.maxApplicationCount = maxApplicationCount;
    }

    public int getMaxDatabaseCount() {
        return maxDatabaseCount;
    }

    public void setMaxDatabaseCount(int maxDatabaseCount) {
        this.maxDatabaseCount = maxDatabaseCount;
    }

    public int getMaxReplicaCount() {
        return maxReplicaCount;
    }

    public void setMaxReplicaCount(int maxReplicaCount) {
        this.maxReplicaCount = maxReplicaCount;
    }

    public int getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(int cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public int getRamLimit() {
        return ramLimit;
    }

    public void setRamLimit(int ramLimit) {
        this.ramLimit = ramLimit;
    }
}
