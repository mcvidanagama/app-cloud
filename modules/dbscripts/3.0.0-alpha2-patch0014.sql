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

INSERT INTO `AC_APP_TYPE` (`id`, `name`, `description`) VALUES (7, 'custom', 'Allows you to deploy applications using custom docker images');

INSERT INTO `AC_RUNTIME` (`id`, `name`, `image_name`, `tag`, `description`) VALUES (11, 'Custom Docker Image runtime', 'custom', 'customtag', 'OS:Custom, JAVA Version:custom');

INSERT INTO `AC_APP_TYPE_RUNTIME` (`app_type_id`, `runtime_id`) VALUES (7, 11);

INSERT INTO `AC_RUNTIME_TRANSPORT` (`transport_id`, `runtime_id`) VALUES (3, 11),(4, 11);

INSERT INTO `AC_RUNTIME_CONTAINER_SPECIFICATIONS` (`id`, `CON_SPEC_ID`) VALUES (11, 3),(11, 4);

INSERT INTO `AC_CLOUD_APP_TYPE` (`cloud_id`, `app_type_id`) VALUES (1, 7);