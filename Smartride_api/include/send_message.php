<?php
if (isset($_GET["regId"]) && isset($_GET["message"])) {
    $regId = $_GET["regId"];
    $message = $_GET["message"];
     
    include_once 'include/GCM_pushnotification.php';
     
    $gcm = new GCM();
 
    $registration_ids = array($regId);
    $message = array("price" => $message);
 
    $result = $gcm->send_notification($registration_ids, $message);
 
    echo $result;
}
?>