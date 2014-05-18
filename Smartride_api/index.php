<?php

/**
 PHP API for Login, Register, Changepassword, Resetpassword Requests and for Email Notifications.
 **/

if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // Get tag
    $tag = $_POST['tag'];

    // Include Database handler
    require_once 'include/DB_Functions.php';
	//require_once 'include/GCM_pushnotification.php';
	
    $db = new DB_Functions();
	//$gcm = new GCM_pushnotification();
	
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);

    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];

        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
            $response["user"]["fname"] = $user["firstname"];
            $response["user"]["lname"] = $user["lastname"];
            $response["user"]["email"] = $user["email"];
			$response["user"]["uname"] = $user["username"];
            $response["user"]["uid"] = $user["uid"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["weight_gender"] = $user["weight_gender"];
            $response["user"]["weight_smoker"] = $user["weight_smoker"];
			$response["user"]["pref_gender"] = $user["pref_gender"];
            $response["user"]["pref_smoker"] = $user["pref_smoker"];
						
            
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } 
  else if ($tag == 'chgpass'){
  $email = $_POST['email'];

  $newpassword = $_POST['newpas'];
  

  $hash = $db->hashSSHA($newpassword);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
  $subject = "Change Password Notification";
         $message = "Hello User,\n\nYour Password is sucessfully changed.\n\nRegards, ";
          $headers = "From:" . $from;
	if ($db->isUserExisted($email)) {

	$user = $db->forgotPassword($email, $encrypted_password, $salt);
		if ($user) {
			$response["success"] = 1;
			mail($email,$subject,$message,$headers);
			echo json_encode($response);
		}
		else {
		$response["error"] = 1;
		echo json_encode($response);
		}
            // user is already existed - error response
    } 
    else {
		$response["error"] = 2;
		$response["error_msg"] = "User not exist";
		 echo json_encode($response);
	}
}

else if ($tag == 'forpass'){
$forgotpassword = $_POST['forgotpassword'];
$randomcode = $db->random_string();
  
$hash = $db->hashSSHA($randomcode);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
$subject = "Password Recovery";
$message = "Hello User,\n\nYour Password is sucessfully changed. Your new Password is $randomcode . Login with your new Password and change it in the User Panel.\n\nRegards, \n.";
$headers = "From:" . $from;

if ($db->isUserExisted($forgotpassword)) {

	$user = $db->forgotPassword($forgotpassword, $encrypted_password, $salt);

	if ($user) {
         $response["success"] = 1;
          mail($forgotpassword,$subject,$message,$headers);
         echo json_encode($response);
	}
	else {
	$response["error"] = 1;
	echo json_encode($response);
	}
            // user is already existed - error response
                 
} 
else {
	$response["error"] = 2;
	$response["error_msg"] = "User not exist";
	 echo json_encode($response);
	 }

}

else if($tag == 'userlocation'){

		$uid =$_POST['uid'];
		$latitude = $_POST['latitude'];
        $longitude = $_POST['longitude'];
		
		$userlocation = $db->storeLocation($uid, $latitude, $longitude);
		if($userlocation){
			$response["success"] = 1;
			$response["userlocation"]["uid"] = $userlocation["uid"];
			$response["userlocation"]["latitude"] = $userlocation["latitude"];
			$response["userlocation"]["longitude"] = $userlocation["longitude"];
			echo json_encode($response);
		}
		else{
			//userlocation failed to store
			$response["error"]=1;
			$response["error_msg"] = "JSON Error occured in user location";
			echo json_encode($response);
		
         }
	}

else if ($tag == 'register') {
        // Request type is Register new user
        $fname = $_POST['fname'];
		$lname = $_POST['lname'];
        $email = $_POST['email'];
		$uname = $_POST['uname'];
        $password = $_POST['password'];
		$weight_gender = $_POST['weight_gender'];
		$weight_smoker = $_POST['weight_smoker'];
		$pref_gender = $_POST['pref_gender'];
		$pref_smoker = $_POST['pref_smoker'];
		$latitude = $_POST['latitude'];
        $longitude = $_POST['longitude'];
		$gcm_regid = $_POST['gcm_regid'];
		
         $subject = "Registration";
         $message = "Hello $fname,\n\nYou have sucessfully registered to our service.\n\nRegards,\nAdmin.";
         $headers = "From:" . $from;

        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } 
           else if(!$db->validEmail($email)){
            $response["error"] = 3;
            $response["error_msg"] = "Invalid Email Id";
            echo json_encode($response);             
}
/*
else if ($tag == 'ridesearch'){

	//send push notification
		
	if (isset($_GET["regId"]) && isset($_GET["message"])) {
    $regId = $_GET["regId"];
    $message = $_GET["message"];
     
    include_once 'include/GCM_pushnotification.php';
     
    $gcm = new GCM_pushnotification();
 
    $registration_ids = array($regId);
    $message = array("price" => $message);
 
    $result = $gcm->sendGoogleCloudMessage($registation_ids, $message);
 
    echo $result;
	}
}		*/


else {
            // store user
            $user = $db->storeUser($fname, $lname, $email, $uname, $password, $weight_gender, $weight_smoker, $pref_gender, $pref_smoker, $latitude, $longitude, $gcm_regid);
			echo $user;
            if ($user) {
                // user stored successfully
            $response["success"] = 1;
            $response["user"]["fname"] = $user["firstname"];
            $response["user"]["lname"] = $user["lastname"];
            $response["user"]["email"] = $user["email"];
			$response["user"]["uname"] = $user["username"];
            $response["user"]["uid"] = $user["uid"];
            $response["user"]["created_at"] = $user["created_at"];
			$response["user"]["weight_gender"] = $user["weight_gender"];
			$response["user"]["weight_smoker"] = $user["weight_smoker"];
			$response["user"]["pref_gender"] = $user["pref_gender"];
			$response["user"]["pref_smoker"] = $user["pref_smoker"];
			$response["user"]["latitude"] = $user["latitude"];
			$response["user"]["longitude"] = $user["longitude"];
			$response["user"]["gcm_regid"] = $user["gcm_regid"];
			
			//$db->getDevice($uid);
			
			//$registration_ids = array($gcm_regid);
			
		     
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "JSON Error occured in Registartion";
                echo json_encode($response);
            }
        }
    } 
	
	
	else {
         $response["error"] = 3;
         $response["error_msg"] = "JSON ERROR";
        echo json_encode($response);
    }
} else {
    echo "Smartride_api";
}
?>
