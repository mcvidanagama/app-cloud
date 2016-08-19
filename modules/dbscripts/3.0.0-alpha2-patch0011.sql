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

-- --------------------------------------------------------
-- Migration - Adding MSF4J 1.0.0 runtime deprecate notice
-- --------------------------------------------------------
UPDATE `AC_RUNTIME`
SET `name` = 'OpenJDK 8 + WSO2 MSF4J 1.0.0 - Deprecated Runtime (will continue to work until September 30th 2016)'
WHERE `id`= 2;