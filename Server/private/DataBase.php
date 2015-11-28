<?php
include_once($_SERVER['HOME']."/private/api/Log.php");

class DataBase {
	private $username = "jedev_congente";
	private $password = "C0ng3nt3";
	protected $link;

	public function __construct($database = 'jedev_congente') {
		//Creating the connection
		$link = mysql_connect('localhost', $this->username, $this->password);
		if (!$link) {
			die('Could not connect: ' . mysql_error());
		}

		//Selecting the Database.
		$db_selected = mysql_select_db($database, $link);
		if (!$db_selected) {
			die ('Can\'t access DB: ' . mysql_error());
		}
		
		//Settign the charset
		mysql_set_charset('utf8',$link);
	}
	
	function __destruct() {
		if(null != $link)
			mysql_close($link);
	}
	
	public function login($user, $senha) {
		//Getting the User from DB
		$query= sprintf("SELECT *
						FROM login 
						WHERE
							  user = '%s' AND
							  senha='%s';",
				mysql_real_escape_string($user),
				mysql_real_escape_string($senha));
		
		//Perform Query
		$result = mysql_query($query);
		if (!$result) {
			return FALSE;
		}
		
		mysql_free_result($result);
		
		return TRUE;
	}
	
	private function getCountryID($country) {
		$query= sprintf("INSERT IGNORE INTO countries(name) VALUES('%s');",
				mysql_real_escape_string($country));
		//Perform Query
		$result = mysql_query($query);
		if (!$result) {
			return null;
		}
		$id = mysql_insert_id();
		if ($id==0) {
			$query = sprintf("SELECT id FROM countries WHERE name='%s';",
					mysql_real_escape_string($country));
			$select_result = mysql_query($query);
			$row = mysql_fetch_assoc($select_result);
			$id = $row['id'];
		}
		return $id;
	}
	
	private function getApplicationID($application) {
		$query = sprintf("SELECT id FROM applications WHERE name='%s';",
				mysql_real_escape_string($application));
		$select_result = mysql_query($query);
		if($select_result == null)
			return -1;
		$row = mysql_fetch_assoc($select_result);
		$id = $row['id'];
		return $id;
	}
	
	private function getUserID($email) {
		LOGS("Getting ID for user: " . $email);
		$query= sprintf("INSERT IGNORE INTO users(email) VALUES('%s');",
				mysql_real_escape_string($email));
		LOGS("Query: " . $query);
		//Perform Query
		$result = mysql_query($query);
		$id = mysql_insert_id();
		if ($id==0) {
			$query = sprintf("SELECT id FROM users WHERE email='%s';",
					mysql_real_escape_string($email));
			$select_result = mysql_query($query);
			$row = mysql_fetch_assoc($select_result);
			$id = $row['id'];
		}
		LOGS("Returning ID: " . $id);
		return $id;
	}
	
	public function registerDonation($application, $donation, $email) {
		LOGS("Registering Donation");
		//Getting the app id
		$app_id = $this->getApplicationId($application);
		LOGS("Application ID returned: " . $app_id);
		if($app_id == -1)
			return false;
		//Saving the email and country of user.
		$query= sprintf("INSERT IGNORE INTO donations (application_id , user_id, donation)
						VALUES ( %s, %s, '%s');",
				$app_id,
				$this->getUserId($email),
				mysql_real_escape_string($donation));
		LOGS("Query for user: " . $query);
		//Perform Query
		$result = mysql_query($query);
		if (!$result) {
			LOGS("Error saving the Donation");
			return false;
		}
		LOGS("Donation was saved!");
		return true;
	}
	
	public function checkFullVersion($application, $emails) {
		LOGS("Checking if user have full Version");
		if($emails == null) {
			LOGS("emails are null");
			return false;
		}
		foreach ($emails as $email) {
			LOGS("Checking user: " . $email);
			//Checking if exist in the donations table
			$query= sprintf("SELECT 
							d.user_id 
						FROM donations d, users u, applications a 
						WHERE
							  d.user_id=u.id AND
							  u.email='%s' AND 
							  d.application_id=a.id AND
							  a.name='%s';",
					mysql_real_escape_string($email),
					mysql_real_escape_string($application));
			LOGS("Query: " . $query);
			//Perform Query
			$result = mysql_query($query);
			if (!$result) {
				//We got an error in the DB...
				LOGS("Query error!!!");
				return false;
			}
			LOGS("Checking if we have results…");
			if(!mysql_num_rows($result))
				continue;
			else
				return true;
		}
		return false;
	}

	public function saveUserEmail($email, $country) {
//echo "Saving the user :" . $email . "  country: " . $country;
		//Saving the email and country of user.
		$query= sprintf("INSERT IGNORE INTO users (countries_id , email) 
						VALUES ( %s, TRIM('%s'));",
				$this->getCountryID($country),
				mysql_real_escape_string($email));
//echo "Query for user: " . $query;
		//Perform Query
		$result = mysql_query($query);
		if (!$result) {
//echo "Error saving the user";
			return false;
		}
//echo "User was saved!";
		return true;
	}
	
	public function saveFeedback($app, $email, $country, $review) {
		//Saving the user data.
		if(!$this->saveUserEmail($email, $country))
			return false;
		//Getting the User from DB
		$query= sprintf("REPLACE INTO feedback (app_id, user_id, review_id, date_sent)
						VALUES ((SELECT id FROM applications WHERE name='%s'), (SELECT id FROM users WHERE email='%s'), (SELECT id FROM review WHERE name='%s'), NOW());",
				mysql_real_escape_string($app),
				mysql_real_escape_string($email),
				mysql_real_escape_string($review));
		//Perform Query
		$result = mysql_query($query);
		if (!$result) {
			return false;
		}
		return true;
	}
	
	public function checkForAnnoucenments( $lastShowedId, $language, $application ) {
		//Getting the Message from DB
		$query = sprintf("SELECT 
							a.id as id, 
							a.title as title, 
							a.message as message, 
							a.image as image 
						FROM languages l, announcements a
						WHERE 
							NOT EXISTS ( SELECT * 
									     FROM announcement_application aa, 
											  applications app 
										 WHERE
										 	  app.name = '%s'
										 	  AND app.id = aa.application_id
										 	  AND a.id = aa.announcement_id)
							AND DATE(NOW()) > DATE(a.expire_date) 
							AND a.id > %s 
							AND l.locale = '%s'
						ORDER BY a.expire_date;",
				mysql_real_escape_string($application),
				mysql_real_escape_string($lastShowedId),
				mysql_real_escape_string($language));

		//Perform Query
		//echo "announcements query" . $query;
		$result = mysql_query($query);
		if (!$result) {
			die( "Could not successfully run query ($query) from DB: " . mysql_error());
		}
		
		//getting result
		$response['announcements'] = array();
		$announcementsCount = 0;
		
		while ($row = mysql_fetch_assoc($result)) {
			$response['announcements'][$announcementsCount]["id"] = $row['id'];
			$response['announcements'][$announcementsCount]["title"] = $row['title'];
			$response['announcements'][$announcementsCount]["message"] = $row['message'];
			$response['announcements'][$announcementsCount]["image"]  = $row['image'];
			$announcementsCount++;
		}
		
		mysql_free_result($result);
		
		return $response;
	}
	
	
	public function check_user($username, $password) {
		//Validating user against DB
		$query = sprintf("SELECT * FROM username WHERE username='%s' AND password=MD5('%s')",
				mysql_real_escape_string($username),
				mysql_real_escape_string($password));

		//Perform Query
		$result = mysql_query($query);
		if (!$result) {
			die( "Could not successfully run query ($query) from DB: " . mysql_error());
		}

		//getting result
		if(mysql_fetch_row($result) == false)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public function getAllApplications() {
		//Getting the User from DB
		$query = "SELECT name, link FROM applications WHERE link IS NOT NULL ORDER BY name";

		//Perform Query
		$result = mysql_query($query);

		//getting result
		$tmp = array();
		while ($row = mysql_fetch_array($result)) {
			$tmp[$row[0]] = $row[1];
		}
		return $tmp;
	}
	
	public function getExpiredTasks() {
		//Getting the User from DB
		$query = "SELECT name FROM schedule_tasks WHERE TIMESTAMPDIFF(HOUR, last_run, NOW()) > timeout";

		//Perform Query
		$result = mysql_query($query);
		if (!$result) {
			die( "Could not successfully run query ($query) from DB: " . mysql_error());
		}
		//getting result
		$tmp = array();
		while ($row = mysql_fetch_array($result)) {
			$tmp[] = $row[0];
		}
		return $tmp;
	}
	
	public function resetLastRunTask($task) {
		//Getting the User from DB
		$query = "UPDATE schedule_tasks SET last_run=NOW() WHERE name='$task'";

		//Perform Query
		$result = mysql_query($query);
	}
}
?>
