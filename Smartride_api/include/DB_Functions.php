<?php

class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
		
    }


    /**
     * Random string which is sent by mail to reset password
     */

public function random_string()
{
    $character_set_array = array();
    $character_set_array[] = array('count' => 7, 'characters' => 'abcdefghijklmnopqrstuvwxyz');
    $character_set_array[] = array('count' => 1, 'characters' => '0123456789');
    $temp_array = array();
    foreach ($character_set_array as $character_set) {
        for ($i = 0; $i < $character_set['count']; $i++) {
            $temp_array[] = $character_set['characters'][rand(0, strlen($character_set['characters']) - 1)];
        }
    }
    shuffle($temp_array);
    return implode('', $temp_array);
}


public function forgotPassword($forgotpassword, $newpassword, $salt){
	$result = mysql_query("UPDATE `users` SET `encrypted_password` = '$newpassword',`salt` = '$salt' 
						  WHERE `email` = '$forgotpassword'");

if ($result) {
 
return true;

}
else
{
return false;
}

}

/**
     * Storing new user
     * returns user details
     */ /*
    public function storeUser($name, $email, $gcm_regid) {
        // insert user into database
        $result = mysql_query("INSERT INTO gcm_users(name, email, gcm_regid, created_at) VALUES('$name', '$email', '$gcm_regid', NOW())");
        // check for successful store
        if ($result) {
            // get user details
            $id = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM gcm_users WHERE id = $id") or die(mysql_error());
            // return user details
            if (mysql_num_rows($result) > 0) {
                return mysql_fetch_array($result);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }*/
	
	/**
     * Getting all users
     */
    public function getAllUsers() {
        $result = mysql_query("select * FROM gcm_users");
        return $result;
    }
	
	/**
     * Adding new user to mysql database
     * returns user details
     */
	
    public function storeUser($fname, $lname, $email, $uname, $password, $weight_gender, $weight_smoker, $pref_gender, $pref_smoker, $latitude, $longitude, $gcm_regid) {
        $uuid = uniqid('', true);
		$uluid = uniqid('',true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
		$sql_user = "INSERT INTO users(firstname, lastname, email, username, encrypted_password, salt, created_at, weight_gender, weight_smoker, pref_gender, pref_smoker) 
		VALUES('$fname', '$lname', '$email', '$uname', '$encrypted_password', '$salt', NOW(), '$weight_gender', '$weight_smoker', '$pref_gender', '$pref_smoker')";
		
		$exec = mysql_query($sql_user);
		if (!$exec) die(mysql_error());
		
    	$result_uid = mysql_insert_id();
		$sql_location = "INSERT INTO userlocation(uid, latitude, longitude, gcm_regid)
		VALUES('$result_uid', '$latitude','$longitude', '$gcm_regid')";
		//'$uluid',id, 
		$exec = mysql_query($sql_location);
		if (!$exec) die(mysql_error());

        // check for successful store
        if ($exec) {
            // get user details  
            $result = mysql_query("SELECT * FROM users WHERE uid = $result_uid");
			
			$row = mysql_fetch_array($result);
			
			mysql_free_result($result);
		   // return user details
            return $row;
        } else {
            return false;
        }
    }
	
	/*
	Get GCM registration id and store
	*/
	
	public function getDevice($uid) {
		$gcm_id = $_POST["regid"];
		
		// update database
		
         $result = "UPDATE `userlocation` SET `gcm_regid` = '$gcm_id' 
						  WHERE `uid` = '$uid'";
		$exec = mysql_query($result);
		if (!$exec) die(mysql_error());		
		
		   if ($exec) {
            // get user details 
            $uid = mysql_insert_id(); 
            $result = mysql_query("SELECT * FROM userlocation WHERE uid = $uid");
		   // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
	}
	
	
	/*
	* Store user location
	*/

	
    public function storeLocation($uid, $latitude, $longitude) {
		
		// update database
         $result = "	UPDATE `userlocation` SET `latitude` = '$latitude',`longitude` = '$longitude' 
						  WHERE `uid` = $uid";
		$exec = mysql_query($result);
		if (!$exec) die(mysql_error());		
		
		   if ($exec) {
            // get user details 
            // $uid = mysql_insert_id(); 
            $result = mysql_query("SELECT * FROM userlocation WHERE uid = $uid");
		   // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }


    /**
     * Verifies user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
        $result = mysql_query("SELECT * FROM users WHERE email = '$email'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

 /**
     * Checks whether the email is valid or fake
     */
public function validEmail($email)
{
   $isValid = true;
   $atIndex = strrpos($email, "@");
   if (is_bool($atIndex) && !$atIndex)
   {
      $isValid = false;
   }
   else
   {
      $domain = substr($email, $atIndex+1);
      $local = substr($email, 0, $atIndex);
      $localLen = strlen($local);
      $domainLen = strlen($domain);
      if ($localLen < 1 || $localLen > 64)
      {
         // local part length exceeded
         $isValid = false;
      }
      else if ($domainLen < 1 || $domainLen > 255)
      {
         // domain part length exceeded
         $isValid = false;
      }
      else if ($local[0] == '.' || $local[$localLen-1] == '.')
      {
         // local part starts or ends with '.'
         $isValid = false;
      }
      else if (preg_match('/\\.\\./', $local))
      {
         // local part has two consecutive dots
         $isValid = false;
      }
      else if (!preg_match('/^[A-Za-z0-9\\-\\.]+$/', $domain))
      {
         // character not valid in domain part
         $isValid = false;
      }
      else if (preg_match('/\\.\\./', $domain))
      {
         // domain part has two consecutive dots
         $isValid = false;
      }
      else if
(!preg_match('/^(\\\\.|[A-Za-z0-9!#%&`_=\\/$\'*+?^{}|~.-])+$/',
                 str_replace("\\\\","",$local)))
      {
         // character not valid in local part unless 
         // local part is quoted
         if (!preg_match('/^"(\\\\"|[^"])+"$/',
             str_replace("\\\\","",$local)))
         {
            $isValid = false;
         }
      }
      if ($isValid && !(checkdnsrr($domain,"MX") || (checkdnsrr($domain,"A"))))
      {
         // domain not found in DNS
         $isValid = false;
      }
   }
   return $isValid;
}

 /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed 
            return true;
        } else {
            // user not existed
            return false;
        }
    }

    /**
     * Encrypting password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>
