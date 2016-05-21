<?php

// Require the initialisation file
require_once 'common.php';

if(isset($_GET['controller']))
{
	

	$url=explode('.',$_GET['controller']);
	if(count($url)<2)
		return;

	//passed APIs
	if( 
		(strcmp($url[0],'customer')==0 && strcmp($url[1],'check_login')==0) //customer.check_login
		||(strcmp($url[0],'customer')==0 && strcmp($url[1],'forgot_password')==0) //customer.forgot_password
	)
	{
		//$url=explode('.',$_GET['controller']);
		require_once 'api/' . $url[0] .'.php';    
		return;
	}


	//handle access token for some APIs
	if(isset($_POST['AccessToken']))
	{	 
		//echo "OK";
		$url=explode('.',$_GET['controller']);
		require_once 'api/' . $url[0] .'.php';    
		return;
		
		return;
	}
}

?>
