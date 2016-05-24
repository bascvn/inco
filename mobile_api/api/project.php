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
function get_list()
{	
	$token = $_POST['token'];
	$offset = "";
	$limit = "";
	if(isset($_POST['offset'])){
		$offset = $_POST['offset'];
	}
	if(isset($_POST['limit'])){
		$limit = $_POST['limit'];
	}
	$response = new Response();
	// check parameter
	if(is_string_empty($token)|| is_string_empty($offset) ||is_string_empty($limit)){
		$response->set_statsus(ResponseCode::STATUS_ERROR);
		$response->error_code = ResponseCode::E_C_MISS_PARAMETER;
		$response->error_mess = ResponseMess::E_M_MISS_PARAMETER;
		echo json_encode($response);
		exit();
	}
	 $db     = cm_connect();
	$user = get_user_access($token,$db);
	if($user  == null){
		$response->status = ResponseCode::STATUS_ERROR;
		$response->error_code = ResponseMess::E_M_PERMISSION;
		echo json_encode($response);
	}else{
		$projects = get_list_project($user['user_id'],$user['allow_manage_projects'],$offset,$limit,$db);
		$response->status = ResponseCode::STATUS_SUCCESS;
		$response->data = $projects;
		echo json_encode($response);
	}
	cm_close_connect($db);
	exit();
}

?>