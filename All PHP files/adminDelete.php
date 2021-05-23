<?php

include "conn.php";

 $response = array();
 
 if($_POST['post_id']){
    
     $postID = $_POST['post_id'];
     
     $stmt = $conn->prepare("DELETE FROM posts WHERE post_id=?");
     $stmt->bind_param("i", $postID);
  
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