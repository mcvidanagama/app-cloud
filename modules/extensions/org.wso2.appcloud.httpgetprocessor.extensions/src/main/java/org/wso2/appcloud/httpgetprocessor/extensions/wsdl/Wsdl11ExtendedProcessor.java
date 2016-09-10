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
import org.wso2.carbon.core.transports.CarbonHttpRequest;
import org.wso2.carbon.core.transports.CarbonHttpResponse;
import org.wso2.carbon.core.transports.util.Wsdl11Processor;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Wsdl11ExtendedProcessor extends Wsdl11Processor {

    public void process(final CarbonHttpRequest request, final CarbonHttpResponse response, final ConfigurationContext configurationContext) throws Exception {
        OverflowBlob temporaryData = new OverflowBlob(256, 4048, WsdlConstants.tempPrefixWsdl11, WsdlConstants.tempSuffix);
        CarbonHttpResponse newResponse = new CarbonHttpResponse(temporaryData.getOutputStream());

        super.process(request, newResponse, configurationContext);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (newResponse.getOutputStream() != null) {
            try {
                ((BlobOutputStream) newResponse.getOutputStream()).getBlob().writeTo(byteArrayOutputStream);
                byte[] data = byteArrayOutputStream.toByteArray();
                String element = new String(data);
                String output = removeTenantInfo(element);
                (((BlobOutputStream) response.getOutputStream()).getBlob()).readFrom(new ByteArrayInputStream(output.getBytes()), output.length());
                response.setStatus(newResponse.getStatusCode());
                response.setError(newResponse.isError());
                Map<String, String> headers = newResponse.getHeaders();
                Set<String> itr = headers.keySet();
                for (String elem : itr) {
                    response.addHeader(elem, headers.get(elem));
                }
            } catch (StreamCopyException e) {
                e.printStackTrace();
            } finally {
                temporaryData.release();
            }
        }
    }

    private String removeTenantInfo(String initialString) {

        try {
            OMElement omElement = AXIOMUtil.stringToOM(initialString);
            Iterator serviceElements = omElement.getChildrenWithLocalName(WsdlConstants.serviceLocalName);
            OMFactory factory = OMAbstractFactory.getOMFactory();
            while (serviceElements.hasNext()) {
                OMElement serviceElement = (OMElement) serviceElements.next();
                Iterator portElements = serviceElement.getChildrenWithLocalName(WsdlConstants.portLocalName);
                while (portElements.hasNext()) {
                    OMElement portElement = (OMElement) portElements.next();
                    Iterator addressElements = portElement.getChildrenWithLocalName(WsdlConstants.addressLocalName);
                    while (addressElements.hasNext()) {
                        OMElement addressElem = (OMElement) addressElements.next();
                        OMAttribute attr = addressElem.getAttribute(new QName(WsdlConstants.locationLocalName));
                        String value = attr.getAttributeValue();
                        String updatedValue = value.replaceAll(WsdlConstants.tenantInfoRegex, WsdlConstants.tenantInfoReplaceChar);
                        addressElem.addAttribute(factory.createOMAttribute(attr.getQName().getLocalPart(), attr.getNamespace(), updatedValue));
                    }
                }
            }
            return omElement.toString();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return initialString;
    }
}