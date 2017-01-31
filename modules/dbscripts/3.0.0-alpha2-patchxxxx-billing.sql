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
-- Biling feature db changes
-- --------------------------------------------------------

-- -----------------------------------------------------
-- Table `AppCloudDB`.`AC_TENANT_SUBSCRIPTION_PLANS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AppCloudDB`.`AC_TENANT_SUBSCRIPTION_PLANS` (
  `tenant_id` INT NOT NULL,
  `plan_id` INT NOT NULL,
  CONSTRAINT `fk_Tenant_UsagePlans`
    FOREIGN KEY (`plan_id`)
    REFERENCES `AppCloudDB`.`AC_SUBSCRIPTION_PLANS` (`plan_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


ALTER TABLE AC_SUBSCRIPTION_PLANS ADD CUMULATIVE_CPU int(11);
ALTER TABLE AC_SUBSCRIPTION_PLANS ADD CUMULATIVE_RAM int(11);

-- ALTER TABLE AC_SUBSCRIPTION_PLANS ADD MAX_CPU int(11);
-- ALTER TABLE AC_SUBSCRIPTION_PLANS ADD MAX_RAM int(11);

update AC_SUBSCRIPTION_PLANS set CUMULATIVE_CPU = 1000 , CUMULATIVE_RAM = 2048 where PLAN_NAME ='FREE';
update AC_SUBSCRIPTION_PLANS set CUMULATIVE_CPU = 2000 , CUMULATIVE_RAM = 4096 where PLAN_NAME ='PAID';

-- -----------------------------------------------------
-- Table `AppCloudDB`.`AC_TENANT_SUBSCRIPTION_PLAN`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS AC_TENANT_SUBSCRIPTION (
  `tenant_id` INT NOT NULL,
  `plan` VARCHAR(50) NOT NULL,
  `max_app_count` INT(11) NOT NULL  DEFAULT -1,
  `max_database_count` INT(11) NOT NULL DEFAULT -1,
  `cloud_id` VARCHAR(50) NOT NULL,
  `max_replica_count` INT(11) NOT NULL DEFAULT -1,
  `max_memory` INT(11) NOT NULL DEFAULT -1,
  `max_cpu` INT(11) NOT NULL DEFAULT -1,
  `start_date`DATETIME NOT NULL,
  `end_date` DATETIME NOT NULL,
  `is_white_listed` TINYINT unsigned NOT NULL DEFAULT 0,
  `status` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`tenant_id`, `cloud_id`))
ENGINE = InnoDB;

