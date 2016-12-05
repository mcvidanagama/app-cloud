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

import java.sql.*;
import java.util.List;

/**
 * MeteringDAO
 */
public class MeteringDAO {

    private static final MeteringDAO meteringDAO = new MeteringDAO();

    public static MeteringDAO getInstance() {
        return meteringDAO;
    }

    public void updateUptime(Connection dbConnection, List<ApplicationDTO> applications) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        final int batchSize = 5;
        int count = 0;

        try {
            preparedStatement = dbConnection.prepareStatement(SQLQueryConstants.UPDATE_APPLICATION_UPTIME);

            for(ApplicationDTO applicationDTO : applications) {
                preparedStatement.setString(1, applicationDTO.getStartTime());
                preparedStatement.setString(2, applicationDTO.getEndTime());
                preparedStatement.setString(3, applicationDTO.getHashId());
                preparedStatement.setString(4, applicationDTO.getUptime());
                preparedStatement.addBatch();
                //preparedStatement.execute();
                if(++count % batchSize == 0) {
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            String msg = "";
            e.printStackTrace();
        }finally {
            //DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
        }
    }

    public void updateRxData(){

    }

    public void updateTxData(){

    }
}
