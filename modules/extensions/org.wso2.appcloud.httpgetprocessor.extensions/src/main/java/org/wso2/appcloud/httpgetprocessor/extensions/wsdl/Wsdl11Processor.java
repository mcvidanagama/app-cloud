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
package org.wso2.appcloud.httpgetprocessor.extensions.wsdl;

import org.apache.axiom.ext.io.StreamCopyException;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.util.blob.BlobOutputStream;
import org.apache.axiom.util.blob.OverflowBlob;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.transports.CarbonHttpRequest;
import org.wso2.carbon.core.transports.CarbonHttpResponse;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

/**
 * Wsdl generator for wsdl 1.1 requests
 */
public class Wsdl11Processor extends org.wso2.carbon.core.transports.util.Wsdl11Processor {
    private static final Log log = LogFactory.getLog(Wsdl11Processor.class);

    /**
     * Process wsdl 1.1 request, generate wsdl from axis2 and update tenant information
     * @param request CarbonHttpRequest with request information
     * @param response CarbonHttpResponse with generated wsdl stream
     * @param configurationContext Axis2 Configuration Context related to the request
     * @throws Exception
     */
    public void process(final CarbonHttpRequest request, final CarbonHttpResponse response, final ConfigurationContext configurationContext) throws Exception {
        OverflowBlob temporaryData = new OverflowBlob(256, 4048, WsdlUtils.tempPrefixWsdl11, WsdlUtils.tempSuffix);
        CarbonHttpResponse updatedResponse = new CarbonHttpResponse(temporaryData.getOutputStream());
        //Generate wsdl from axis2 wsdl 1.1 processor
        super.process(request, updatedResponse, configurationContext);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (updatedResponse.getOutputStream() != null) {
            try {
                ((BlobOutputStream) updatedResponse.getOutputStream()).getBlob().writeTo(byteArrayOutputStream);
                byte[] data = byteArrayOutputStream.toByteArray();
                byte[] output = removeTenantInfo(data);
                (((BlobOutputStream) response.getOutputStream()).getBlob()).readFrom(new ByteArrayInputStream(output), output.length);
                WsdlUtils.populateResponse(updatedResponse, response);
            } catch (StreamCopyException streamCopyException) {
                log.error("Error in updating WSDL ", streamCopyException);
                throw new Exception(streamCopyException);
            } finally {
                temporaryData.release();
            }
        }
    }

    private byte[] removeTenantInfo(byte[] data) {
        OMFactory factory = OMAbstractFactory.getOMFactory();
        try {
            OMElement omElement = AXIOMUtil.stringToOM(new String(data));
            Iterator serviceElements = omElement.getChildrenWithLocalName(WsdlUtils.serviceLocalName);

            while (serviceElements != null && serviceElements.hasNext()) {
                OMElement serviceElement = (OMElement) serviceElements.next();
                if(serviceElement != null) {
                    Iterator portElements = serviceElement.getChildrenWithLocalName(WsdlUtils.portLocalName);
                    while (portElements != null && portElements.hasNext()) {
                        OMElement portElement = (OMElement) portElements.next();
                        OMElement addressElement = portElement.getFirstElement();
                        if(addressElement != null) {
                            OMAttribute attr = addressElement.getAttribute(new QName(WsdlUtils.locationLocalName));
                            if (attr != null && attr.getAttributeValue() != null) {
                                String updatedValue = attr.getAttributeValue().replaceAll(WsdlUtils.tenantInfoRegex, WsdlUtils.tenantInfoReplaceChar);
                                addressElement.addAttribute(factory.createOMAttribute(attr.getQName().getLocalPart(), attr.getNamespace(), updatedValue));
                            }
                        }
                    }
                }
            }
            return omElement.toString().getBytes();
        } catch (XMLStreamException xmlStreamException) {
            log.error("Error removing tenant info from WSDL : ", xmlStreamException);
        }
        return data;
    }
}