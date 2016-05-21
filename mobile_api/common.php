<?php 
     define("API_URI",      "sample_api/"); 
	 define("IMAGE_URL",      "assets/");
	
	require_once('Spyc.php');// import for read file config database
	 
   
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
    
	
?>