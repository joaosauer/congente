<?php
include_once("../../../private/api/Authenticator.php");
include_once("../../../private/api/DataBase.php");
include_once("../../../private/api/AuthSendEmail.php");
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
// NAO USAR!!!!!! APENAS PARA REFERENCIA!!!!!
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
//************************************************
$authenticator = new Authenticator();
if($authenticator->isValidRequest()) {
	LOGS("Valid Request! Getting the data...");
	//Getting the data from the request
	$json = json_decode($authenticator->getDecryptedBody(), TRUE);

	//Saving the user data.
	$database = new DataBase();
	$result = $database->saveUserEmail($json['email'], $json['country']);
	if($result == null) {
		LOGS("Problems in reading DB...");
		header("HTTP/1.0 500 Internal server error");
	}
	else {
		//Sending the email
		$subject = "Requested Results from Universal Lotto generator";
		
		$message  = "<html><body>\n";
		$message .= "<a href=\"http://jedevmobile.com\"><img src=\"http://jedevmobile.com/images/JE_logo_medium.png\" alt=\"JEDevMobile\" /></a>\n";
		$message .= "<h1>Thank You for using our app!</h1>\n";
		$message .= "<h2>These are the results requested:</h2>\n";
		$message .= "<table border=\"3\" style=\"border-color: #003250;\" cellpadding=\"10\">\n";
		
		$oddLine=true;
		foreach( $json["draw"] as $draw ){
			$message .= "<tr>\n";
			$color = "#BBD2E0;\">";
			if($oddLine)
				$color="#5893AC;\">";
				
			if(isset($draw["normalNumbers"])) {
				//New Version
				//Checking Normal Numbers
				foreach( $draw["normalNumbers"] as $number ){
					if(is_int($number))
						$message .= "<td style=\" background-color: " . $color . $number . "</td>\n";
					else
						$message .= "<td style=\" background-color: " . $color . "ERROR" . "</td>\n";
				}
				foreach( $draw["extraNumbers"] as $number ){
					if(is_int($number))
						$message .= "<td style=\" background-color: #FFFF00\">" .  $number . "</td>\n";
					else
						$message .= "<td style=\" background-color: #FFFF00\">" . "ERROR" . "</td>\n";
				}
			}
			else {
				//Old Version
				foreach( $draw as $number ){ 
					if(is_int($number))
						$message .= "<td style=\" background-color: " . $color . $number . "</td>\n";
					else
						$message .= "<td style=\" background-color: " . $color . "ERROR" . "</td>\n";
				}
			}	
			$message .= "</tr>\n";
			$oddLine = !$oddLine;
		}
		$message .= "</table>\n";
		$message .= "</body></html>\n";
		
		//Requesting to sending email.
		$authSendEmail = new \api\AuthSendEmail();
		$authSendEmail->sendEmail($json['email'], $subject, $message);
		
		header("HTTP/1.0 200 OK");
		echo json_encode($result);
	}
}
else {
	LOGS("Problems....");
	header("HTTP/1.0 403 Forbidden");
}

?>
