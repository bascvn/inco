<?php 

//======================================================================================
class MyNewLogger implements ApnsPHP_Log_Interface
{
	public function log($sMessage)
	{
		echo "ApnsPHP_Log_Interface".$sMessage;
	}
}
/**
* 
*/
class IncoAPNS
{
	
	function __construct()
	{
		# code...
	}
//=========================================================================================================
function cm_push_ios($InfoArr,$data,$alert,$badge) 
{
	if(count($InfoArr)==0 )
		return;
	
	//$PemFile = 'INCODEV_Push_PROD_Certificates.pem';
	$PemFile = './apns/INCODEV_Push_DEV_Certificates.pem';
	
	
	
	if(!isset($PemFile) || trim($PemFile)==='') return;
	
	
	$push = new ApnsPHP_Push(
			//ApnsPHP_Abstract::ENVIRONMENT_PRODUCTION,
			ApnsPHP_Abstract::ENVIRONMENT_SANDBOX, 
			$PemFile
	);

	$myNewLogger = new MyNewLogger();
	$push->setLogger($myNewLogger);
	
	// Set the Root Certificate Autority to verify the Apple remote peer
	$push->setRootCertificationAuthority('./apns/entrust_root_certification_authority.pem');

	// Connect to the Apple Push Notification Service
	$push->connect();			


	for($i=0;$i<count($InfoArr);$i++)
	{
		$devideToken = $InfoArr[$i];
		if(!isset($devideToken) || trim($devideToken)==='') continue;
		
		$message = new ApnsPHP_Message($devideToken);
		

		// Set a custom identifier. To get back this identifier use the getCustomIdentifier() method
		// over a ApnsPHP_Message object retrieved with the getErrors() message.
		$message->setCustomIdentifier("MaleviaSync");

		$message->setBadge($badge);
		$message->setSound();
		$message->setCustomProperty('inco', $data);
		$message->setText($alert);
		
		// Set the expiry value to 30 seconds
		$message->setExpiry(30);

		// Add the message to the message queue
		$push->add($message);
	
    }
	
	if(count($InfoArr)>0)
	{
		// Send all messages in the message queue
		$push->send();
		//echo "<br/>sent!!!";
	}
	
	// Disconnect from the Apple Push Notification Service
	$push->disconnect();

	/*
	// Examine the error message container
	$aErrorQueue = $push->getErrors();
	if (!empty($aErrorQueue)) {
			var_dump($aErrorQueue);
	}
	*/
	}
}
?>