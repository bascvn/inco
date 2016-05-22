<?php 
     define("API_URI",      "sample_api/"); 
	 define("IMAGE_URL",      "assets/");
	
	require_once('Spyc.php');// import for read file config database
	require_once('../core/lib/PasswordHash.php');
	 
   
	//======================================================================================
    function cm_get_server_uri(){
	
		$actual_link = "http://$_SERVER[HTTP_HOST]/".API_URI;
		//$actual_link = "https://$_SERVER[HTTP_HOST]/".API_URI;
	
		return $actual_link;
	}


	//======================================================================================
    function cm_connect(){
		// Create connection with file config 
		// file config database
		$file_db_config = "../core/config/databases.yml";
		$parsed = $array = Spyc::YAMLLoad($file_db_config);
		$db_info = strstr($parsed['all']['doctrine']['param']['dsn'],'dbname');
		$db_info_array = explode(';', $db_info);
		$db_name = explode("=",$db_info_array[0])[1];
		$db_host = explode("=",$db_info_array[1])[1];
		$username = $parsed['all']['doctrine']['param']['username'];
		$password = eval('?>'.$parsed['all']['doctrine']['param']['password']);
		
		$con=mysqli_connect($db_host,$username,$password,$db_name);

		// Check connection
		if (mysqli_connect_errno()) {
			echo "Failed to connect to MySQL: " . mysqli_connect_error();
		}
		// Change character set to utf8
		mysqli_set_charset($con,"utf8");
		return $con;
    }

    //======================================================================================
    function cm_close_connect($con){

        mysqli_close($con);
    }
     //=========================== get user=========================================
     function get_user($con,$email,$password){
     	
     	$sql = "SELECT * FROM `users` WHERE `users`.`email` = '$email' AND `users`.`active` = 1";
        if ($result=mysqli_query($con,$sql)){
	     	if (mysqli_num_rows($result) > 0) {
	     		 while($row = mysqli_fetch_assoc($result)) {
	     		 	$hasher = new PasswordHash(11, false);
	     		 	if($hasher->CheckPassword($password,$row['password'])){
	     		 		return  $row;
	     		 	}
	     		 	
	    		}	
	     	}
	     	  mysqli_free_result($result);
	     }
     	return null;
     }
     function insert_tokens($user_id,$tokens,$device_id,$device_type,$con){
     	$id = get_device_id($device_id,$device_type,$con);

     	if($id == 0){
     		create_new_tokens($user_id,$tokens,$device_id,$device_type,$con);
     	}else{
     		update_new_token($id,$user_id,$tokens,$device_id,$device_type,$con);
     		
     	}
     }
     function update_new_token($id,$user_id,$tokens,$device_id,$device_type,$con){
     	$time = date("Y-m-d H:i:s"); 
     	$sql = "UPDATE  `mobile_tokens`
     			SET `user_id` = '$user_id' , 
     				`device_id` = '$device_id',
     				 `device_type` = '$device_type',
     				  `token` = '$tokens',
     				  `create_time` = '$time',
     				   `last_access` = '$time'
     			WHERE `id` ='$id'";
     	$result=mysqli_query($con,$sql);
     	return 0;
     }
     function create_new_tokens($user_id,$tokens,$device_id,$device_type,$con){
     	$time = date("Y-m-d H:i:s"); 
     	$sql = "INSERT INTO `mobile_tokens` (`user_id`, `device_id`, `device_type`, `token`, `create_time`, `last_access`) 
     	VALUES ('$user_id', '$device_id', '$device_type', '$tokens', '$time', '$time')";
     	$result=mysqli_query($con,$sql);
	    return 0;

     }
     function get_device_id($device_id,$device_type,$con){
     	$sql = "SELECT * FROM `mobile_tokens` WHERE `mobile_tokens`.`device_id` = '$device_id' AND `mobile_tokens`.`device_type` = '$device_type'";

        if ($result=mysqli_query($con,$sql)){
	     	if (mysqli_num_rows($result) > 0) {
	     		 while($row = mysqli_fetch_assoc($result)) {
	     		 	return $row['id'];
	    		}	
	     	}
	     	  mysqli_free_result($result);
	     }
	     return 0;
     }
     function detete_token($token,$con){
 		$sql = "DELETE FROM `mobile_tokens` WHERE `mobile_tokens`.`token` = '$token'";
 		$result=mysqli_query($con,$sql);
     }
    
	
?>