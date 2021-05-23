<?php

include "conn.php";

 $response = array();
 
 if($_POST['report_text'] && $_POST['Ruser_id']){
    
     $reportText = $_POST['report_text'];
     $userID = $_POST['Ruser_id'];
     
     $stmt = $conn->prepare("INSERT INTO `report`(`report_text`, `Ruser_id`) VALUES (?,?)");
     $stmt->bind_param("ss",$reportText,$userID);
  
   if($stmt->execute() == TRUE){
     
         $response['error'] = false;
         $response['message'] = "course created successfully!";
     } else{
         // if we get any error we are passing error to our object.
         $response['error'] = true;
         $response['message'] = "failed\n ".$conn->error;
     }
 } else{
     // this msethod is called when user
     // donot enter sufficient parameters. 
     $response['error'] = true;
     $response['message'] = "Insufficient parameters";
 }
 // at last we are prinintg our response which we get. 
 echo json_encode($response);
 ?>