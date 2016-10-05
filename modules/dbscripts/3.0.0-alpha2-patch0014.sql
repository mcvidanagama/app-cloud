<<<<<<< HEAD
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


----------------------------------------------------------
-- Custom docker app type related db changes
----------------------------------------------------------

INSERT INTO `AC_APP_TYPE` (`id`, `name`, `description`) VALUES (7, 'custom', 'Allows you to deploy applications using custom docker images');

INSERT INTO `AC_RUNTIME` (`id`, `name`, `image_name`, `tag`, `description`) VALUES (11, 'Custom Docker Image runtime', 'custom', 'customtag', 'OS:Custom, JAVA Version:custom');

INSERT INTO `AC_APP_TYPE_RUNTIME` (`app_type_id`, `runtime_id`) VALUES (7, 11);

INSERT INTO `AC_RUNTIME_TRANSPORT` (`transport_id`, `runtime_id`) VALUES (3, 11),(4, 11);

INSERT INTO `AC_RUNTIME_CONTAINER_SPECIFICATIONS` (`id`, `CON_SPEC_ID`) VALUES (11, 3),(11, 4);

INSERT INTO `AC_CLOUD_APP_TYPE` (`cloud_id`, `app_type_id`) VALUES (1, 7);

