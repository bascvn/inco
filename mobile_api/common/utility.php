<?php
	function is_string_empty($string){
		if($string == null){
			return TRUE ;
		}
		if(strlen(trim($string)) == 0){
			return TRUE ;
		}
		return FALSE;
	}
	 function tokens_generator($username,$device_id){
		return md5($username.$device_id."INCOProject".date("D M j G:i:s T Y").rand(0,10000000));
	}
?>