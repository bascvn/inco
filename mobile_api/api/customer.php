<?php
//==============================================================================================================
// this header's code must be embeded in every AJAX controler file
//==============================================================================================================

require_once 'common.php';
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
function check_login()
{
    $db     = cm_connect();
	
	//doing the logic
	cm_close_connect($db);
	echo '{"status":200,"message":"ok","data":""}';
	
}


function forgot_password()
{
    $db     = cm_connect();
	
	//doing the logic
	cm_close_connect($db);
	echo '{"status":200,"message":"ok","data":""}';
}

?>