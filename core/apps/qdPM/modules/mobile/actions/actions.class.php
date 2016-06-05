<?php
/**
*qdPM
*
* NOTICE OF LICENSE
*
* This source file is subject to the Open Software License (OSL 3.0)
* that is bundled with this package in the file LICENSE.txt.
* It is also available through the world-wide-web at this URL:
* http://opensource.org/licenses/osl-3.0.php
* If you did not receive a copy of the license and are unable to
* obtain it through the world-wide-web, please send an email
* to license@qdPM.net so we can send you a copy immediately.
*
* DISCLAIMER
*
* Do not edit or add to this file if you wish to upgrade qdPM to newer
* versions in the future. If you wish to customize qdPM for your
* needs please refer to http://www.qdPM.net for more information.
*
* @copyright  Copyright (c) 2009  Sergey Kharchishin and Kym Romanets (http://www.qdpm.net)
* @license    http://opensource.org/licenses/osl-3.0.php  Open Software License (OSL 3.0)
*/
?>
<?php

/**
 * tasks actions.
 *
 * @package    sf_sandbox
 * @subpackage tasks
 * @author     Your name here
 * @version    SVN: $Id: actions.class.php 23810 2009-11-12 11:07:44Z Kris.Wallsmith $
 */
class mobileActions extends sfActions

{
  protected $getUserMobile;
   public function getUserMobile(){
      return $this->userMobile;
   }
   public function setUserToken($token){
   $user  = Doctrine_Core::getTable('Tokens')
         ->createQuery()
         ->addWhere('token=?',$token)
         ->fetchOne();
     if($user){
       $this->userMobile = Doctrine_Core::getTable('Users')
                    ->createQuery()
                    ->addWhere('id=?',$user->getUserId())
                    ->addWhere('active=1') 
                    ->fetchOne();
      if($this->userMobile){
        $this->getUser()->setAttribute('user',$this->userMobile);
        return true;
      }
      else{
        return false;
      }
    }
    return fasle;
   }
   public function executeIndex(sfWebRequest $request)
  {
     
    $this->tasks_comments = Doctrine_Core::getTable('TasksComments')
      ->createQuery('tc')      
      ->leftJoin('tc.Users u')
      ->addWhere('tc.tasks_id=?',$token)     
      ->orderBy('tc.created_at desc')
      ->fetchArray();
    var_dump($this->tasks_comments );
    exit(); 
  }

   public function executeToken(sfWebRequest $request)
  {
    
    $this->tokens = Doctrine_Core::getTable('Tokens')
    ->createQuery('tc')   
      ->fetchArray();
    var_dump($this->tokens );
    exit(); 
  }
    public function executeFile(sfWebRequest $request)
  {
    
    $this->tokens = Doctrine_Core::getTable('Attachments')
    ->createQuery('tc')   
      ->fetchArray();
    var_dump($this->tokens );
    exit(); 
  }
  public function executeAddcommenttask(sfWebRequest $request)
  {
      if(!$this->setUserToken($request->getParameter('token'))){
          // TODO
          exit();
      }
    $this->projects = Doctrine_Core::getTable('Projects')
                    ->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))
                    ->fetchOne();
    if(!$this->projects){
        exit();
    }
    $this->tasks = Doctrine_Core::getTable('Tasks')
                   ->createQuery()
                   ->addWhere('id=?',$request->getParameter('tasks_id'))->addWhere('projects_id=?',$request->getParameter('projects_id'))
      ->fetchOne(); 
    if(!$this->tasks){
        exit();
    }
    $this->form = new TasksCommentsForm(null, array('tasks'=>$this->tasks));
    $this->processForm($request, $this->form);
    echo "exit";
    exit();
  }
   protected function processForm(sfWebRequest $request, sfForm $form)
  {
    $form->bind($request->getParameter($form->getName()), $request->getFiles($form->getName()));
    if ($form->isValid())
    {
      if($form->getObject()->isNew())
      {
        $tasks = Doctrine_Core::getTable('Tasks')->find($request->getParameter('tasks_id'));
        if($form->getValue('tasks_priority_id')>0){ $tasks->setTasksPriorityId($form->getValue('tasks_priority_id')); } else { unset($form['tasks_priority_id']); }            
        if($request->getParameter('tasks_labels_id')>0){ $tasks->setTasksLabelId($request->getParameter('tasks_labels_id')); }
        if($request->getParameter('tasks_types_id')>0){ $tasks->setTasksTypeId($request->getParameter('tasks_types_id')); }             
        if(strlen($form->getValue('due_date'))>0){ $tasks->setDueDate($form->getValue('due_date')); } else { unset($form['due_date']); }
        if($request->getParameter('progress')>0){ $tasks->setProgress($request->getParameter('progress')); } 
             
        if($form->getValue('tasks_status_id')>0)
        { 
          $tasks->setTasksStatusId($form->getValue('tasks_status_id'));
          
          if(in_array($form->getValue('tasks_status_id'),app::getStatusByGroup('closed','TasksStatus')))
          {
            $tasks->setClosedDate(date('Y-m-d H:i:s'));
            $tasks->save();      
          }
          
          if(!in_array($form->getValue('tasks_status_id'),app::getStatusByGroup('closed','TasksStatus')))
          {
            $tasks->setClosedDate(null);
            $tasks->save();      
          } 
        } 
        else 
        { 
          unset($form['tasks_status_id']); 
        }
      
                                        
        $tasks->save();
      }
      
      if($form->getObject()->isNew() and sfConfig::get('app_allow_adit_tasks_comments_date')!='on'){ $form->setFieldValue('created_at',date('Y-m-d H:i:s')); }
    
      $tasks_comments = $form->save();
       var_dump($request->getParameter('attachments_info')) ;          
      Attachments::insertAttachments($request->getFiles(),'comments',$tasks_comments->getId(),$request->getParameter('attachments_info'),$this->getUser());
      TasksComments::sendNotification($this,$tasks_comments,$this->getUser());

      
    }
}

  public function executeUpload(sfWebRequest $request)
  {      


    $file = $request->getFiles();
    $filename = mt_rand(111111,999999)  . '-' . $file['Filedata']['name'];
    if(move_uploaded_file($file['Filedata']['tmp_name'], sfConfig::get('sf_upload_dir') . '/attachments/'  . $filename))
    {               
      $bind_id = $request->getParameter('bind_id');
      
      if((int)$bind_id==0)
      {
        $bind_id = -$this->getUser()->getAttribute('id');
      }
       
      $a = new Attachments();
      $a->setFile($filename);    
      $a->setBindType("comments");            
      $a->setBindId($bind_id);      
      $a->save();  
      $response = new Response();  
      $response->status = ResponseCode::STATUS_SUCCESS;
      $tmp['id'] = $a->getId();
      $tmp['fileName'] = $a->getFile();
      $response->data = $tmp;
      echo json_encode($response);
      exit();
    }
    reponeError();        
    exit();
  }
  function reponeError(){
    $response = new Response();
    $response->set_statsus(ResponseCode::STATUS_ERROR);
    $response->error_code = ResponseCode::E_C_MISS_PARAMETER;
    $response->error_mess = ResponseMess::E_M_MISS_PARAMETER;
    echo json_encode($response);
    exit();
  }


}
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