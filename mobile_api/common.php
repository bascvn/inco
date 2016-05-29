<?php 
     define("API_URI",      "sample_api/"); 
	 define("IMAGE_URL",      "assets/");
	
	require_once('Spyc.php');// import for read file config database
	require_once('../core/lib/PasswordHash.php');
	require_once('./common/Component.php'); 
   require_once('./common/UserAccess.php'); 
   require_once('./common/sqlhepper.php'); 
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
      function get_user_access($token,$con){
     	$sql ="SELECT * FROM `mobile_tokens` LEFT JOIN `users` ON `mobile_tokens`.`user_id` = `users`.`id` LEFT JOIN `users_groups` ON `users`.`users_group_id`= `users_groups`.`id` WHERE `mobile_tokens`.`token` = '$token'AND `users`.`active` = '1'";
     	  if ($result=mysqli_query($con,$sql)){
	     	if (mysqli_num_rows($result) > 0) {
	     		 while($row = mysqli_fetch_assoc($result)) {
	     		 	mysqli_free_result($result);
	     		 	return $row;

	    		}	
	     	}
	     	  mysqli_free_result($result);
	     }
	     return null;
     }
      function get_list_project($user_id,$access_control,$search,$offset,$limit,$con){
     	$main_sql ="SELECT projects.id,projects.name, projects_status.name AS status,projects_types.name AS type,
     	users.name AS created_by, DATE_FORMAT(projects.created_at,'%m-%d-%Y')  AS created_at FROM `projects` LEFT JOIN  projects_status ON projects.projects_status_id = projects_status.id LEFT JOIN projects_types ON projects.projects_types_id = projects_types.id LEFT JOIN users ON projects.created_by = users.id ";
     	if($access_control == UserAccess::VIEW_OWN_ONLY){
     		$main_sql .="WHERE (find_in_set('$user_id',projects.team) > 0 OR projects.created_by = $user_id) AND projects.name LIKE '%$search%' ";
     	}else{
     		$main_sql .="WHERE projects.name LIKE '%$search%' ";
     	}
     	
     	$projects=array();
     	$main_sql .= "ORDER BY projects.name LIMIT $limit OFFSET $offset";
     	 if ($result=mysqli_query($con,$main_sql)){
	     	if (mysqli_num_rows($result) > 0) {
	     		 while($row = mysqli_fetch_assoc($result)) {

	     		 	array_push($projects,$row);
	     		 	
	    		}	
	     	}
	     	  mysqli_free_result($result);
	     }
	     return $projects;
     }

     function get_list_task($user,$project_id,$search,$offset,$limit,$con){
     	$main_sql ="SELECT 
     		tasks.id AS id,
     		tasks.name AS name,
     		projects.name AS projects,
     		tasks_priority.name AS tasks_priority,
     		users.name AS assigned_to,
     		tasks_status.name AS tasks_status
     		 FROM tasks 
     		LEFT JOIN projects ON tasks.projects_id = projects.id 
     		LEFT JOIN tasks_priority ON tasks.tasks_type_id=tasks_priority.id 
     		LEFT JOIN users ON tasks.assigned_to=users.id 
     		LEFT JOIN users AS users2 ON tasks.created_by=users2.id 
     		LEFT JOIN tasks_status oN tasks.tasks_status_id = tasks_status.id ";

        $user_id = $user['id'];
        $access_project  = $user['allow_manage_projects'];
        $access_task = $user['allow_manage_tasks'];
     	$where_arr = new SqlWhereHepper();
     	if(strlen($project_id)>0){
     		$where_arr->pushAND("tasks.projects_id = $project_id");
            if( $access_task == UserAccess::VIEW_OWN_ONLY){
                $where_arr->pushAND("( find_in_set($user_id,tasks.assigned_to) >0 OR tasks.created_by = $user_id)");
            }
     	} else{
            if($access_project == UserAccess::VIEW_OWN_ONLY){
                $where_arr->pushAND("(find_in_set('$user_id',projects.team) > 0 OR projects.created_by = $user_id)");
            }
            if($access_task == UserAccess::VIEW_OWN_ONLY){
                $where_arr->pushAND("( find_in_set($user_id,tasks.assigned_to) >0 OR tasks.created_by = $user_id)");
            }
        }	
        $where_arr->pushAND("tasks.name LIKE '%$search%'");
     	$projects=array();
     
     	$main_sql .= " WHERE ".$where_arr->toString()." ORDER BY tasks.name LIMIT $limit OFFSET $offset";

     	 if ($result=mysqli_query($con,$main_sql)){
	     	if (mysqli_num_rows($result) > 0) {
	     		 while($row = mysqli_fetch_assoc($result)) {

	     		 	array_push($projects,$row);
	     		 	
	    		}	
	     	}
	     	  mysqli_free_result($result);
	     }
	     return $projects;
     }
     function get_task_comments($task_id,$search,$offset,$limit,$con){
        $main_sql ="SELECT 
            tasks_comments.id AS id,
            tasks_comments.description AS description,
            users.photo AS avatar,
            tasks_comments.created_at AS created_at,
            users.name AS name 
            FROM tasks_comments 
            LEFT JOIN users ON tasks_comments.created_by = users.id";
            $main_sql .= " WHERE tasks_comments.tasks_id = $task_id AND tasks_comments.description LIKE '%$search%' ORDER BY tasks_comments.created_at desc LIMIT $limit OFFSET $offset";
        $projects=array();
        if ($result=mysqli_query($con,$main_sql)){
            if (mysqli_num_rows($result) > 0) {
                 while($row = mysqli_fetch_assoc($result)) {
                    $row['attachments'] = get_attachments('comments',$row['id'],$con);
                    array_push($projects,$row);
                }   
            }
              mysqli_free_result($result);
         }
         return $projects;
     }
     function get_attachments($bind_type,$bind_id,$con){
        $main_sql ="SELECT id,file FROM attachments WHERE bind_type = '$bind_type' AND bind_id = $bind_id";
        $item=array();

        if ($result=mysqli_query($con,$main_sql)){
            if (mysqli_num_rows($result) > 0) {
                 while($row = mysqli_fetch_assoc($result)) {
                    array_push($item,$row);
                }   
            }
              mysqli_free_result($result);
         }
         return $item;
     }
     function get_list_tickets($user,$project_id,$search,$offset,$limit,$con){
        $main_sql ="SELECT 
                tickets.id AS id,
                tickets.name AS name,
                departments.name AS departments,
                tickets_status.name AS tickets_status,
                users.name AS create_by,
                projects.name AS project 
                FROM tickets
                LEFT JOIN departments ON tickets.departments_id = departments.id 
                LEFT JOIN tickets_status ON tickets_status.id= tickets.tickets_status_id 
                LEFT JOIN users ON users.id = tickets.users_id 
                LEFT JOIN projects ON projects.id= tickets.projects_id";

        $user_id = $user['id'];
        $access_project  = $user['allow_manage_projects'];
        $access_ticket = $user['allow_manage_tickets'];
        $where_arr = new SqlWhereHepper();
        if(strlen($project_id)>0){
            $where_arr->pushAND("tickets.projects_id = $project_id");
            if( $access_ticket == UserAccess::VIEW_OWN_ONLY){
                $where_arr->pushAND("( departments.users_id = $user_id OR  tickets.users_id  = $user_id )");
            }
        } else{
            if($access_project == UserAccess::VIEW_OWN_ONLY){
                $where_arr->pushAND("(find_in_set('$user_id',projects.team) > 0 OR projects.created_by = $user_id)");
            }
            if($access_ticket == UserAccess::VIEW_OWN_ONLY){
                    $where_arr->pushAND("( departments.users_id = $user_id OR  tickets.users_id  = $user_id )");
            }
        }   
        $where_arr->pushAND("tickets.name LIKE '%$search%'");
        $projects=array();
       
        $main_sql .= " WHERE ".$where_arr->toString()." ORDER BY tickets.name LIMIT $limit OFFSET $offset";
         if ($result=mysqli_query($con,$main_sql)){
            if (mysqli_num_rows($result) > 0) {
                 while($row = mysqli_fetch_assoc($result)) {

                    array_push($projects,$row);
                    
                }   
            }
              mysqli_free_result($result);
         }
         return $projects;
     }
    function get_list_discussions($user,$project_id,$search,$offset,$limit,$con){
        $main_sql ="SELECT
             discussions.id AS id, 
             discussions.name AS name, 
             discussions_status.name AS status,
             projects.name AS projects,
             users.name AS create_by 
             FROM discussions 
             LEFT JOIN projects ON projects.id = discussions.projects_id
             LEFT JOIN users ON users.id = discussions.users_id 
             LEFT JOIN discussions_status ON discussions.discussions_status_id = discussions_status.id";

        $user_id = $user['id'];
        $access_project  = $user['allow_manage_projects'];
        $access_discussion = $user['allow_manage_discussions'];
        $where_arr = new SqlWhereHepper();
        if(strlen($project_id)>0){
            $where_arr->pushAND("discussions.projects_id = $project_id");
            if( $access_discussion == UserAccess::VIEW_OWN_ONLY){
                $where_arr->pushAND("( find_in_set('$user_id',discussions.assigned_to) > 0 discussions.users_id  = $user_id )");
            }
        } else{
            if($access_project == UserAccess::VIEW_OWN_ONLY){
                $where_arr->pushAND("(find_in_set('$user_id',projects.team) > 0 OR projects.created_by = $user_id)");
            }
            if($access_discussion == UserAccess::VIEW_OWN_ONLY){
                      $where_arr->pushAND("( find_in_set('$user_id',discussions.assigned_to) > 0 discussions.users_id  = $user_id )");
            }
        }   
        $where_arr->pushAND("discussions.name LIKE '%$search%'");
        $projects=array();
       
        $main_sql .= " WHERE ".$where_arr->toString()." ORDER BY discussions.name LIMIT $limit OFFSET $offset";
         if ($result=mysqli_query($con,$main_sql)){
            if (mysqli_num_rows($result) > 0) {
                 while($row = mysqli_fetch_assoc($result)) {

                    array_push($projects,$row);
                    
                }   
            }
              mysqli_free_result($result);
         }
         return $projects;
     }
     // check acess API
     function check_acess_project_by_ticket_id($user,$ticket_id,$con){
         $access_project  = $user['allow_manage_projects'];
            $user_id = $user['id'];
         if($access_project == UserAccess::VIEW_OWN_ONLY){
            $sql = "SELECT tickets.id FROM tickets 
            LEFT JOIN projects ON tickets.projects_id = projects.id 
            WHERE tickets.id = 4 
            AND find_in_set('$user_id',projects.team) > 0 OR projects.created_by = $user_id)";
            $result = mysql_query( $sql, $con);
            $num_rows = mysql_num_rows($result);
            if($num_rows == 0){
                return false;
            }
         }
 
         return true;
     }
    
?>