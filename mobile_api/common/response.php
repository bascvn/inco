<?php
 define("STATUS_SUCCESS","sucess"); 
 define("STATUS_ERROR","error");
 define("E_C_MISS_PARAMETER","1");
 define("E_M_MISS_PARAMETER","miss parameter."); 
 define("E_C_LOGIN_ERROR","2");
 define("E_M_LOGIN_ERROR","error email or password");    

class ResponseCode 
{
	const STATUS_SUCCESS = "sucess";
	const STATUS_ERROR = "error";
	const E_C_MISS_PARAMETER = "1";
	const E_C_LOGIN_ERROR ="2";
	const E_C_PERMISSION = "3";
}
/**
* 
*/
class ResponseMess {

	const  E_M_MISS_PARAMETER ="miss parameter.";
	const  E_M_LOGIN_ERROR ="error email or password";
	const  E_M_PERMISSION = "Error permisson";
}
	

class Response
{
	public $status = "";
	public $error_code = "";
	public $error_mess = "";
	public $data = "";
    public function set_statsus($status){
    	$this->status = $status;
    }
   
}
?>