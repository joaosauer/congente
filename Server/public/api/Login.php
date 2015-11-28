<?php
include_once("../../../private/congente/api/DataBase.php");

$database = new DataBase();

//Getting the data from the request
$json = json_decode(@file_get_contents('php://input'), TRUE);

//Saving the user data.
$database = new DataBase();
$result = $database->login($json['usuario'], $json['senha']);
if($result == nTRUE) {
	//Sending to response.
	header("HTTP/1.0 200 OK");
}
else {
	header("HTTP/1.0 403 Forbidden");
}

?>
