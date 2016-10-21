<?php
// Service health check
$array = array(
        "Name" => "WSO2",
        "Product" => "WSO2 Cloud",
        "Application" => "Healh Check Monitor"
);

// convert the above array to json string using json_encode()
$json = json_encode($array);

// set the json header content-type
header('Content-Type: application/json');

echo $json;
?>