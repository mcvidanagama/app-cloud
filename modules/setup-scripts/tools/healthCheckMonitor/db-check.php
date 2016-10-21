<?php
// Database health check
$servername = "mysql.storage.cloud.wso2.com";
$username = "wso2_9NNMKfTT";
$password = "kpds1729";
$dbname = "health_check_monitor_kasundsilva";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
	http_response_code(500);
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "SELECT * FROM company";
$result = $conn->query($sql);

// set the json header content-type
header('Content-Type: application/json');

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $array = array(
	        "id" => $row["id"],
	        "name" => $row["name"],
	        "address" => $row["address"] . $row["city"]
		);
		// convert the above array to json string using json_encode()
		$json = json_encode($array);
		echo $json;
    }
} else {
	http_response_code(500);
    echo "0 results";
}

$conn->close();
?>