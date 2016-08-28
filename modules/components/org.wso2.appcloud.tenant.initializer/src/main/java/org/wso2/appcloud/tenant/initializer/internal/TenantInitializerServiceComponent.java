/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.appcloud.tenant.initializer.internal;

import org.apache.axis2.engine.ListenerManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.stratos.common.beans.TenantInfoBean;
import org.wso2.carbon.tenant.mgt.services.TenantMgtAdminService;

/**
 * @scr.component name="org.wso2.intcloud.tenant.initializer.internal.TenantInitializerServiceComponent"
 * immediate="true"
 * @scr.reference name="listener.manager"
 * interface="org.apache.axis2.engine.ListenerManager"
 * cardinality="1..1"
 * policy="dynamic"
 * bind="setListenerManager"
 * unbind="unsetListenerManager"
 */
public class TenantInitializerServiceComponent {
    private static final Log log = LogFactory.getLog(TenantInitializerServiceComponent.class);

    protected void activate(ComponentContext ctx) {
        TenantMgtAdminService tenantMgtAdminService = new TenantMgtAdminService();

        String TENANT_PASSWORD = System.getenv("TENANT_PASSWORD");
        String TENANT_ID = System.getenv("TENANT_ID");
        String TENANT_DOMAIN = System.getenv("TENANT_DOMAIN");
        String CREATE_TENANT = System.getenv("CREATE_TENANT");

        if (Boolean.parseBoolean(CREATE_TENANT)) {

            if (StringUtils.isEmpty(TENANT_PASSWORD)) {
                TENANT_PASSWORD = "admin123";
            }

            TenantInfoBean tenantInfoBean = new TenantInfoBean();
            tenantInfoBean.setActive(true);
            tenantInfoBean.setAdmin("admin");
            tenantInfoBean.setAdminPassword(TENANT_PASSWORD);
            tenantInfoBean.setFirstname("FirstName");
            tenantInfoBean.setLastname("LastName");
            tenantInfoBean.setEmail("admin@" + TENANT_DOMAIN);
            tenantInfoBean.setTenantDomain(TENANT_DOMAIN);
            tenantInfoBean.setSuccessKey("");
            tenantInfoBean.setTenantId(Integer.parseInt(TENANT_ID));
            tenantInfoBean.setUsagePlan("Demo");
            try {
                tenantMgtAdminService.addTenant(tenantInfoBean);
                log.info("Tenant added with Tenant ID : " + TENANT_ID + " and Tenant Domain : " + TENANT_DOMAIN);
            } catch (Exception e) {
                log.error("Error occurred while activating TenantInitializerServiceComponent. " +
                          "Tenant not created for Tenant ID : " + TENANT_ID + " and Tenant Domain : " + TENANT_DOMAIN,
                          e);
            }
        } else {
            log.info("CREATE_TENANT env variable not set. Hence, not creating tenant");
        }

    }

    protected void setListenerManager(ListenerManager listenerManager) {
    }

    protected void unsetListenerManager(ListenerManager listenerManager) {
    }
}
