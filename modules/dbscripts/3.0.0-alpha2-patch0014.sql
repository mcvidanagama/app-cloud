--
--  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
--
--    WSO2 Inc. licenses this file to you under the Apache License,
--    Version 2.0 (the "License"); you may not use this file except
--    in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing,
--    software distributed under the License is distributed on an
--    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
--    KIND, either express or implied.  See the License for the
--    specific language governing permissions and limitations
--    under the License.
--
-----------------------------------------------------------
-- Updates to the AC_RUNTIMES
-----------------------------------------------------------
UPDATE `AC_RUNTIME` SET `name` = 'Apache Tomcat 8.0.28 / WSO2 Application Server 6.0.0-M1 - Deprecating on 2016/12/31' WHERE `id`= 1;
UPDATE `AC_RUNTIME` SET `name` = 'OpenJDK 8 + WSO2 MSF4J 1.0.0 - Deprecating on 2016/12/31' WHERE `id`= 2;
UPDATE `AC_RUNTIME` SET `name` = 'Jaggery 0.11.0' WHERE `id`= 5;
UPDATE `AC_RUNTIME` SET `name` = 'Apache Tomcat 8.0.28 / WSO2 Application Server 6.0.0-M2 - Deprecating on 2016/12/31' WHERE `id`= 6;
UPDATE `AC_RUNTIME` SET `name` = 'Apache Tomcat 8.0.36 / WSO2 Application Server 6.0.0-M3 - Deprecating on 2016/12/31' WHERE `id`= 10;

-----------------------------------------------------------
-- Remove wso2as600m3 from jaggery app type
-----------------------------------------------------------
DELETE FROM `AC_APP_TYPE_RUNTIME` WHERE `app_type_id` = 4 AND `runtime_id` = 10;

