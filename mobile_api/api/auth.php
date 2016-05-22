<?php
//==============================================================================================================
// this header's code must be embeded in every AJAX controler file
//==============================================================================================================

require_once 'common.php';
require_once 'common/response.php';
require_once 'common/utility.php';

$url         = explode('.', $_GET['controller']);
$task        = $url[1];
$varFunction = "$task";
$varFunction();
//==============================================================================================================
?>


<?php

/*
function cm_create_customer_access_token( $CustomerID, $CustomerPassword)
{     
	$AccessToken =  "CUS".$CustomerID."_".password_hash($CustomerPassword, PASSWORD_DEFAULT);
	return $AccessToken;
}
*/

//==============================================================================================================
function login()
{	
	$email="";
	$password="";
	$device_type ="";
	$device_id ="";
	if(isset($_POST['email'])){
		$email = $_POST['email'];
	}
	if(isset($_POST['password'])){
		$password = $_POST['password'];
	}
	if(isset($_POST['device_type'])){
		$device_type = $_POST['device_type'];
	}
	if(isset($_POST['device_id'])){
		$device_id = $_POST['device_id'];
	}
	$response = new Response();
	// check parameter
	if(is_string_empty($email)||is_string_empty($password)
		||is_string_empty($device_type)||is_string_empty($device_id)){
		$response->set_statsus(STATUS_ERROR);
		$response->error_code = E_C_MISS_PARAMETER;
		$response->error_mess = E_M_MISS_PARAMETER;
		echo json_encode($response);
		return;
	}
    $db     = cm_connect();
	$user = get_user($db,$email,$password);
	if($user == null){
		$response->set_statsus(STATUS_ERROR);
		$response->error_code = E_C_LOGIN_ERROR;
		$response->error_mess = E_M_LOGIN_ERROR;
		echo json_encode($response);
	}else{
		$tokens = tokens_generator($email,$device_id);
		insert_tokens($user['id'],$tokens,$device_id,$device_type,$db);
		$info['user'] = $user;
		$info['token'] = $tokens;
		$response->set_statsus(STATUS_SUCCESS);
		$response->data = $info;
		echo json_encode($response);
	}
	//doing the logic
	cm_close_connect($db);
}


function logout()
{
    
	//doing the logic
	$token = "";
	$response = new Response();
	if(isset($_POST['token'])){
		$token = $_POST['token'];
	}
	if(is_string_empty($token) ){
		$response->set_statsus(STATUS_ERROR);
		$response->error_code = E_C_MISS_PARAMETER;
		$response->error_mess = E_M_MISS_PARAMETER;
		echo json_encode($response);
		return;
	}
	$db     = cm_connect();
	detete_token($token,$db);
	cm_close_connect($db);
	$response->set_statsus(STATUS_SUCCESS);
	echo json_encode($response);
}

?>