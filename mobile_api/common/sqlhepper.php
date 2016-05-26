<?php 
class SqlWhereHepper{
 	public $where_array;
 	function __construct(){
 		$this->where_array = array();
 	}
 	function pushAND($data){
 		array_push($this->where_array,$data);
 	}
 	function toString(){
 		$result ="";
 		$length = count($this->where_array);
 		for ($i=0; $i <$length  ; $i++) { 
 			if($i == 0){
 				$result.= $this->where_array[$i];
 			}else{
 				$result.= " AND ".$this->where_array[$i];
 			}
 		}
 		return $result;
 	}
}

 
?>