<?php

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
						FROM usuarios 
						WHERE
							  nome = '%s' AND
							  senha='%s';",
				mysql_real_escape_string($user),
				mysql_real_escape_string($senha));
		
		//Perform Query
		$result = mysql_query($query);
		if (!$result) {
			die( "Could not successfully run query ($query) from DB: " . mysql_error());
		}

		//getting result
		if(mysql_fetch_row($result) == false) {
			mysql_free_result($result);
			return false;
		}
		else {
			mysql_free_result($result);
			return true;
		}
	}
	
	public function createUser($user, $senha) {
		//Creating User in DB
		$query= sprintf("INSERT INTO usuarios(nome, senha) VALUES('%s', '%s');",
				mysql_real_escape_string($user),
				mysql_real_escape_string($senha));
		
		//Perform Query
		$result = mysql_query($query);
		if (!$result) {
			die ('Can\'t access DB: ' . mysql_error());
			return FALSE;
		}
		
		mysql_free_result($result);
		
		return TRUE;
	}
}
?>
