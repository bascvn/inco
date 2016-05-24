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
		(strcmp($url[0],'auth')==0) //customer.check_login
		
	)
	{
		//$url=explode('.',$_GET['controller']);
		require_once 'api/' . $url[0] .'.php';    
		return;
	}


	//handle access token for some APIs
	if(isset($_POST['token']))
	{	 
		//echo "OK";
		$url=explode('.',$_GET['controller']);
		require_once 'api/' . $url[0] .'.php';    
		return;
	}
}

?>
