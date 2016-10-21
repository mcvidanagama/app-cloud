<?php
// DNS health check
$ipWso2 = gethostbyname('wso2.com');
$ipIsportal = gethostbyname('isportal.wso2.com');
$ipSupport = gethostbyname('support.wso2.com');
$ipSupporttools = gethostbyname('supporttools.wso2.com');

if ($ipWso2 == 'wso2.com') {
	http_response_code(500);
    die("DNS check failed for: " . $ipWso2);

} elseif ($ipIsportal == 'isportal.wso2.com') {
	http_response_code(500);
	die("DNS check failed for: " . $ipIsportal);

} elseif ($ipSupport == 'support.wso2.com') {
   	http_response_code(500);
    die("DNS check failed for: " . $ipSupport);

} elseif ($ipSupporttools == 'supporttools.wso2.com') {
   	http_response_code(500);
    die("DNS check failed for: " . $ipIsportal);
}

$array = array(
        "ip-wso2" => $ipWso2,
        "ip-isportal" => $ipIsportal,
        "ip-support" => $ipSupport,
        "ip-supporttools" => $ipSupporttools
);

$json = json_encode($array);

// set the json header content-type
header('Content-Type: application/json');
echo $json;
?>