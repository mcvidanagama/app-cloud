package org.wso2.appcloud.httpgetprocessor.extensions.wsdl;

public class WsdlConstants {
    public static final String tenantInfoRegex = "\\/t\\/\\w+(\\.\\w+|\\w)\\/";
    public static final String tenantInfoReplaceChar = "\\/";
    public static final String tempSuffix = ".dat";

    //WSDL Elements
    public static final String serviceLocalName = "service";
    public static final String portLocalName = "port";
    public static final String addressLocalName = "address";
    public static final String locationLocalName = "location";
    public static final String endpointLocalName = "endpoint";

    //Temp File Prefixes
    public static final String tempPrefixWsdl11 = "_nhttp_wsdl11";
    public static final String tempPrefixWsdl20 = "_nhttp_wsdl20";
}
