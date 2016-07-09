<?php
/**
 * @author Ravi Tamada
 * @link URL Tutorial link
 */

class Push{
    // push message title
    private $title;
    private $photo;
    private $message;
    private $component; 
    private $project;
    private $id;
    private $date;
    private $parent;
    private $attach;
    function __construct() {
        
    }
     public function setDate($date){
        $this->date = $date;
    }
    public function setAttach($attach){
        $this->attach = $attach;
    }
    public function setParent($parent){
        $this->parent = $parent;
    }    
    public function setTitle($title){
        $this->title = $title;
    }
    
    public function setPhoto($photo){
        $this->photo = $photo;
    }
    public function setMessage($message){
        $this->message = $message;
    }
    public function setComponent($component){
        $this->component = $component;
    }
    public function setProject($project){
        $this->project = $project;
    }
    public function setID($id){
        $this->id = $id;
    }
    public function getPush(){
        $res = array();
        $res['title'] = $this->title;
        $res['photo'] = $this->photo;
        $res['message'] = $this->message;
        $res['component'] = $this->component;
        $res['project'] = $this->project;
        $res['parent'] = $this->parent;
        $res['attach'] = $this->attach;
        $res['date'] = $this->date;
        $res['id'] = $this->id;
        return $res;
    }
}