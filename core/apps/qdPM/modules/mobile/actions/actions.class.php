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
        $this->getUser()->setAttribute('id',$this->userMobile->getId());
        $this->getUser()->setAttribute('users_group_id',$this->userMobile->getUsersGroupId());
        return TRUE;
      }
      else{
    
        return FALSE;
      }
     
    }
 
    return FALSE;
   }

   protected function checkProjectsAccess($projects)
  {    
    Projects::checkViewOwnAccess($this,$this->getUser(),$projects);    
  }
  
  protected function checkTasksAccess($access,$tasks=false,$projects=false)
  {
    if($projects)
    {
      Users::checkAccess($this,$access,'tasks',$this->getUser(),$projects->getId());
      if($tasks)
      {
        Tasks::checkViewOwnAccess($this,$this->getUser(),$tasks,$projects);
      }
    }
  }
  protected function checkTicketsAccess($access,$tickets=false,$projects=false)
  {
    if($projects)
    {
      Users::checkAccess($this,$access,'tickets',$this->getUser(),$projects->getId());
      if($tickets)
      {
        Tickets::checkViewOwnAccess($this,$this->getUser(),$tickets,$projects);
      }
    }
    elseif($tickets)
    {
      Users::checkAccess($this,$access,'tickets',$this->getUser());
      Tickets::checkViewOwnAccess($this,$this->getUser(),$tickets);
    }
  }
  protected function checkDiscussionsAccess($access,$discussions=false,$projects=false)
  {
    if($projects)
    {
      Users::checkAccess($this,$access,'discussions',$this->getUser(),$projects->getId());
      if($discussions)
      {
        Discussions::checkViewOwnAccess($this,$this->getUser(),$discussions,$projects);
      }
    }
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
    public function executeTokendelete(sfWebRequest $request)
  {
    
    $this->tokens = Doctrine_Core::getTable('Tokens')
    ->createQuery('tc')   
    ->delete()
    ->execute();
    exit(); 
  }
    public function executeTest(sfWebRequest $request)
  {
    
    
    $message = $request->getParameter('message');
    $data = array();
    
    $data['title'] = 'Google Cloud Messaging';
    $data['message'] = $message;
    if ($image == 'true') {
        $data['image'] = 'http://api.androidhive.info/gcm/panda.jpg';
    } else {
        $data['image'] = '';
    }
    $data['created_at'] = date('Y-m-d G:i:s');

    $fields = array(
        'to' => 'c7cLVKUdslk:APA91bEbpnGbbqn3D3IJVC1OceJokBfff6SulNsa_PaSUWL9ZXGCLJV4pDD0YLFxJCIWJDgAoHcOccjem7u_YCPbMTmNk5NJqHf5SrNoaD64zQ5nUZJ-Hdcyfw4D8IVaAXqPS8tCzGJ3',
        'data' => $data,
    );

    // Set POST variables
    $url = 'https://gcm-http.googleapis.com/gcm/send';

    $headers = array(
        'Authorization: key=' . 'AIzaSyB9Hhiixv_XEDGLes1Wid02CSQiQ9cQtqk',
        'Content-Type: application/json'
    );

    // Open connection
    $ch = curl_init();

    // Set the url, number of POST vars, POST data
    curl_setopt($ch, CURLOPT_URL, $url);

    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    // Disabling SSL Certificate support temporarly
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

    $response = array();

    // Execute post
    $result = curl_exec($ch);
    if ($result === FALSE) {
        $response['error'] = TRUE;
        $response['message'] = 'Unable to send test push notification';
         echo json_encode($response);
        exit;
    }

    // Close connection
    curl_close($ch);

    $response['error'] = FALSE;
    $response['message'] = 'Test push message sent successfully!';
      echo json_encode($response);
      echo json_encode($fields);
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
  /******************** new ticket ******************************************/
  public function executeNewticket(sfWebRequest $request)
  {
     if(!$this->setUserToken($request->getParameter('token'))){
           $this->reponeError(); 
          exit();
    }
   
    
    $projects = Doctrine_Core::getTable('Projects')->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))->fetchOne();
    if(!$projects){
          $this->reponeError();
    }
    $this->checkProjectsAccess($projects);
    $this->checkTicketsAccess('insert',false,$projects);

    $this->form = new TicketsForm(null,array('projects'=>$projects,'sf_user'=>$this->getUser()));

    $this->processFormNewTicket($request, $this->form);

    exit();
  }
  protected function processFormNewTicket(sfWebRequest $request, sfForm $form)
  {
    $form->bind($request->getParameter($form->getName()), $request->getFiles($form->getName()));
    if ($form->isValid())
    {
      if($form->getObject()->isNew()){ $previeous_status = false; }else{ $previeous_status = $form->getObject()->getTicketsStatusId(); }
      if($form->getObject()->isNew()){ $previeous_departmnet = false; }else{ $previeous_departmnet = $form->getObject()->getDepartmentsId(); }
    
      $send_to = $this->getSendTo($form);
      
      if($form->getObject()->isNew()){ $form->setFieldValue('created_at',date('Y-m-d H:i:s')); }
    
      $tickets = $form->save();
      
      ExtraFieldsList::setValues($request->getParameter('extra_fields'),$tickets->getId(),'tickets',$this->getUser(),$request);
      
      Attachments::insertAttachments($request->getFiles(),'tickets',$tickets->getId(),$request->getParameter('attachments_info'),$this->getUser());
      
      $tickets = $this->addCommentIfStatusChanged($previeous_status,$previeous_departmnet,$tickets);
      
      $this->addRelatedItems($tickets,$request);
      
      if($tickets->getUsersId()>0)      
      {        
        Tickets::sendNotification($this,$tickets,$send_to,$this->getUser(),$request->getParameter('extra_notification',array()));
      }
    }
  }
  protected function addRelatedItems($tickets,$request)
  {
    if($request->getParameter('related_tasks_id')>0)
    {
      $o = new TasksToTickets;
      $o->setTasksId($request->getParameter('related_tasks_id'))
        ->setTicketsId($tickets->getId())
        ->save();  
    }
    elseif($request->getParameter('related_discussions_id')>0)
    {
      $o = new TicketsToDiscussions;
      $o->setDiscussionsId($request->getParameter('related_discussions_id'))
        ->setTicketsId($tickets->getId())
        ->save();  
    }
    
  }
  
  protected function addCommentIfStatusChanged($previeous_status,$previeous_departmnet,$tickets)
  {  
              
    if(($previeous_status!=$tickets->getTicketsStatusId() or $previeous_departmnet!=$tickets->getDepartmentsId() ) and $previeous_status>0)
    {
      $c = new TicketsComments;
      if($previeous_status!=$tickets->getTicketsStatusId()){ $c->setTicketsStatusId($tickets->getTicketsStatusId()); }      
      $c->setTicketsId($tickets->getId());
      $c->setCreatedAt(date('Y-m-d H:i:s'));
      $c->setUsersId($this->getUser()->getAttribute('id'));
      $c->save();
                  
      $tickets->save();
    }
     
    return $tickets;    
  }
   protected function getSendTo($form)
  {
    $send_to = array();
    
    $allow_send = ''; 
    
    $departments = Doctrine_Core::getTable('Departments')->find($form['departments_id']->getValue());   
    
    if($form->getObject()->isNew())
    {
      $send_to['new'] = array($departments->getUsersId());
      
      $allow_send = 'new';
    }
    else
    {    
      if($form['departments_id']->getValue()!=$form->getObject()->getDepartmentsId())
      {
        $send_to['new'] = array($departments->getUsersId());
      }
      elseif($form->getObject()->getTicketsStatusId()!=$form['tickets_status_id']->getValue())
      {
        $send_to['status'] = array($departments->getUsersId());
        
        $allow_send = 'status';
      }
    }
    
    if(strlen($allow_send)>0 and sfConfig::get('app_notify_all_tickets')=='on')
    {    
      $send_to[$allow_send] = Projects::getTeamUsersByAccess($form['projects_id']->getValue(),'tickets');                                    
    }
    
    return $send_to;    
  }  
    public function executeTicketform(sfWebRequest $request)
  {
    if(!$this->setUserToken($request->getParameter('token'))){
           $this->reponeError(); 
          exit();
    }
    $projects = Doctrine_Core::getTable('Projects')->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))->fetchOne();
    if(!$projects){
      $this->reponeError(); 
          exit();
    }
    $this->checkProjectsAccess($projects);
    $this->checkTicketsAccess('insert',false,$projects);

     $response = new Response();  
     $response->status = ResponseCode::STATUS_SUCCESS;
     $ticketform = new TicketFormResponse();
     $ticketform->deparments = app::getItemsChoicesByTable('Departments');
     $ticketform->ticketsStatus = app::getItemsChoicesByTable('TicketsStatus');
     $ticketform->ticketsStatusDefault = app::getDefaultValueByTable('TicketsStatus');
     $ticketform->ticketsTypes = app::getItemsChoicesByTable('TicketsTypes');
     $ticketform->ticketsTypesDefault = app::getDefaultValueByTable('TicketsTypes');
     $ticketform->notify = Users::getChoices(array_filter(explode(',',$projects->getTeam())),'tickets');
     if($projects)
    {
      if(Users::hasAccess('edit','projects',$this->getUser(),$projects->getId()))
      {
         $ticketform->users = Users::getChoices(array_merge(array($this->getUser()->getAttribute('id')),array_filter(explode(',',$projects->getTeam()))),'tickets_insert');
      }
      
    }
    $response->data =  $ticketform;
    echo json_encode($response);
    exit();
    $TicketsStatus =  app::getItemsChoicesByTable('TicketsStatus');
    echo json_encode($TicketsStatus);
     $TicketsStatusDefault = app::getDefaultValueByTable('TicketsStatus');
      echo json_encode($TicketsStatusDefault);

    exit(); 
  }
  
  public function executeAddcommenttask(sfWebRequest $request)
  {
      if(!$this->setUserToken($request->getParameter('token'))){
           $this->reponeError(); 
          exit();
      }
    $this->projects = Doctrine_Core::getTable('Projects')
                    ->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))
                    ->fetchOne();
    if(!$this->projects){
       $this->reponeError(); 
        exit();
    }
    $this->tasks = Doctrine_Core::getTable('Tasks')
                   ->createQuery()
                   ->addWhere('id=?',$request->getParameter('tasks_id'))->addWhere('projects_id=?',$request->getParameter('projects_id'))
      ->fetchOne(); 
    if(!$this->tasks){
       $this->reponeError(); 
        exit();
    }
    
    $this->checkProjectsAccess($this->projects);
    $this->checkTasksAccess('view',$this->tasks, $this->projects);
    Users::checkAccess($this,'insert','tasksComments',$this->getUser(),$this->projects->getId());
    $this->form = new TasksCommentsForm(null, array('tasks'=>$this->tasks));
    $this->processForm($request, $this->form);
    
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
  /********************* file **************************************/

   public function executeDownload(sfWebRequest $request)
  {
     
     
     if($this->setUserToken($request->getParameter('token')) == FALSE){
          echo "token";
           $this->reponeError(); 
          exit();
    }
    $attachments = Doctrine_Core::getTable('Attachments')->find($request->getParameter('id'));
      if(!$attachments){
        
            $this->reponeError(); 
          exit();
        }  
    $file_path = sfConfig::get('sf_upload_dir') . '/attachments/' . $attachments->getFile();
                    
    if(is_file($file_path))
    {
      header("Expires: Mon, 26 Nov 1962 00:00:00 GMT");
      header("Last-Modified: " . gmdate("D,d M Y H:i:s") . " GMT");
      header("Cache-Control: no-cache, must-revalidate");
      header("Pragma: no-cache");
      header("Content-Type: Application/octet-stream");
      header("Content-disposition: attachment; filename=" . substr(str_replace(' ','_',$attachments->getFile()),7));
  
      readfile($file_path);
    }
    else
    {
      echo 'File "' . $attachments->getFile() . '" not found';
    }
  
    exit();
  }

  public function executeUpload(sfWebRequest $request)
  {      

    if(!$this->setUserToken($request->getParameter('token'))){
           $this->reponeError(); 
          exit();
    }
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
      $a->setBindType($request->getParameter('bind_type'));            
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
    $this->reponeError();        
    exit();
  }


//*********************** add comment ticket **************************************************/
  public function executeAddcommentticket(sfWebRequest $request)
  {
     
    if(!$this->setUserToken($request->getParameter('token'))){
         $this->reponeError(); 
        exit();
    }       
    if($request->getParameter('projects_id')>0)
    {
        $this->projects = Doctrine_Core::getTable('Projects')->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))->fetchOne();
        if(!$this->projects){
            $this->reponeError(); 
          exit();
        }
     $this->tickets = Doctrine_Core::getTable('Tickets')->createQuery()->addWhere('id=?',$request->getParameter('tickets_id'))->addWhere('projects_id=?',$request->getParameter('projects_id'))->fetchOne();
      if(!$this->tickets){
            $this->reponeError(); 
          exit();
        }
      $this->checkProjectsAccess($this->projects);
      $this->checkTicketsAccess('view',$this->tickets, $this->projects);
    }
    else
    {
      $this->projects = null;
      $this->tickets = Doctrine_Core::getTable('Tickets')->find(array($request->getParameter('tickets_id')));
      if(!$this->tickets){
            $this->reponeError(); 
          exit();
        }
      $this->checkTicketsAccess('view',$this->tickets);
    }
    
    Users::checkAccess($this,'insert','ticketsComments',$this->getUser(),$request->getParameter('projects_id'));

    $this->form = new TicketsCommentsForm(null, array('tickets'=>$this->tickets));

    $this->processFormTicket($request, $this->form);

    exit();
  }
  protected function processFormTicket(sfWebRequest $request, sfForm $form)
  {
    $form->bind($request->getParameter($form->getName()), $request->getFiles($form->getName()));
    if ($form->isValid())
    {
      if($form->getObject()->isNew())
      {
        $tickets = Doctrine_Core::getTable('Tickets')->find($request->getParameter('tickets_id'));
        if($form->getValue('tickets_status_id')>0)
        { 
          $tickets->setTicketsStatusId($form->getValue('tickets_status_id'));                                  
        } 
        else 
        { 
          unset($form['tickets_status_id']); 
        }
        
        if($request->getParameter('tickets_types_id')>0){ $tickets->setTicketsTypesId($request->getParameter('tickets_types_id')); }             
        if($request->getParameter('departments_id')>0){ $tickets->setDepartmentsId($request->getParameter('departments_id')); } 
                    
        $tickets->save();
      }
      
      if($form->getObject()->isNew()){ $form->setFieldValue('created_at',date('Y-m-d H:i:s')); }
      
      $tickets_comments = $form->save();
      
      Attachments::insertAttachments($request->getFiles(),'ticketsComments',$tickets_comments->getId(),$request->getParameter('attachments_info'),$this->getUser());          
      TicketsComments::sendNotification($this,$tickets_comments,$this->getUser());
    }
  }
  //*********************** add comment  discussion ticket ****************************************/
   public function executeAddcommentdiscussion(sfWebRequest $request)
  {
     if(!$this->setUserToken($request->getParameter('token'))){
         $this->reponeError(); 
        exit();
    }  
    
    $this->projects = Doctrine_Core::getTable('Projects')->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))->fetchOne();
     if(!$this->projects){
            $this->reponeError(); 
          exit();
        }
    $this->discussions = Doctrine_Core::getTable('Discussions')->createQuery()->addWhere('id=?',$request->getParameter('discussions_id'))->addWhere('projects_id=?',$request->getParameter('projects_id'))->fetchOne();
     if(!$this->discussions){
            $this->reponeError(); 
          exit();
        }
    $this->checkProjectsAccess($this->projects);
    $this->checkDiscussionsAccess('view',$this->discussions, $this->projects);
    Users::checkAccess($this,'insert','discussionsComments',$this->getUser(),$this->projects->getId());

    $this->form = new DiscussionsCommentsForm(null, array('discussions'=>$this->discussions));

    $this->processFormDiscussion($request, $this->form);
    exit();
  }
 protected function processFormDiscussion(sfWebRequest $request, sfForm $form)
  {
    $form->bind($request->getParameter($form->getName()), $request->getFiles($form->getName()));
    if ($form->isValid())
    {
      if($form->getObject()->isNew())
      {
        $discussions = Doctrine_Core::getTable('Discussions')->find($request->getParameter('discussions_id'));            
        if($form->getValue('discussions_status_id')>0){ $discussions->setDiscussionsStatusId($form->getValue('discussions_status_id')); } else { unset($form['discussions_status_id']); }      
        $discussions->save();
      }
      
      if($form->getObject()->isNew()){ $form->setFieldValue('created_at',date('Y-m-d H:i:s')); }
    
      $discussions_comments = $form->save();
      
      Attachments::insertAttachments($request->getFiles(),'discussionsComments',$discussions_comments->getId(),$request->getParameter('attachments_info'),$this->getUser());
      
      DiscussionsComments::sendNotification($this,$discussions_comments,$this->getUser());
    }
  }
/****************** getdetail component *******************************/ 
public function executeDiscussions(sfWebRequest $request){
  $this->projects = Doctrine_Core::getTable('Projects')->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))->fetchOne();
   if(!$this->projects){
      $this->reponeError(); 
          exit();
  }
  $this->discussions = Doctrine_Core::getTable('Discussions')->createQuery()->addWhere('id=?',$request->getParameter('id'))->addWhere('projects_id=?',$request->getParameter('projects_id'))->fetchOne();
  if(!$this->discussions){
      $this->reponeError(); 
          exit();
  }
  //$this->checkDiscussionsAccess('view',$this->discussions, $this->projects);
  $response = new Response();  
  $response->status = ResponseCode::STATUS_SUCCESS;
  $data['description'] = $this->discussions->getDescription();
  $data['id'] = $this->discussions->getId();
  $data['create_at'] = '';
  $create_by  = $this->discussions->getUsers();
  if($create_by){
    $data['users'] = $create_by->getId();
    $data['photo'] = $create_by->getPhoto();
    $data['name'] = $create_by->getName();
  }
    $this->attachments = Doctrine_Core::getTable('Attachments')
              ->createQuery()
              ->addWhere('bind_id=?',$this->discussions->getId())
              ->addWhere('bind_type=?','discussions')
              ->orderBy('id')
              ->fetchArray();
  $data['attachments'] =$this->attachments;
  $detail['id'] = $this->discussions->getId();
  $detail['status'] = $this->discussions->getDiscussionsStatus()->getName();
  $users = Doctrine_Core::getTable('Users')->createQuery('u')
        ->addWhere("find_in_set(u.id,'".$this->discussions->getAssignedTo()."')")
        ->orderBy('u.name')
        ->fetchArray();
  $detail['assigendTo'] = $users;
  $data['detail'] =$detail;
  $response->data =  $data;
  echo json_encode($response);
  exit();  
}
public function executeTickets(sfWebRequest $request){
  if(!$this->setUserToken($request->getParameter('token'))){
           $this->reponeError(); 
          exit();
      }
  $this->projects = Doctrine_Core::getTable('Projects')->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))->fetchOne();
  if(!$this->projects){
      $this->reponeError(); 
          exit();
  }
  $this->tickets = Doctrine_Core::getTable('Tickets')->createQuery()->addWhere('id=?',$request->getParameter('id'))->addWhere('projects_id=?',$request->getParameter('projects_id'))->fetchOne();
   if(!$this->tickets){
      $this->reponeError(); 
          exit();
  }
  $this->checkProjectsAccess($this->projects);
  $this->checkTicketsAccess('view',$this->tickets, $this->projects);
  $response = new Response();  
  $response->status = ResponseCode::STATUS_SUCCESS;
  $data['description'] = $this->tickets->getDescription();
  $data['id'] = $this->tickets->getId();
  $data['create_at'] = $this->tickets->getCreatedAt();
  $create_by  = $this->tickets->getUsers();
  if($create_by){
    $data['users'] = $create_by->getId();
    $data['photo'] = $create_by->getPhoto();
    $data['name'] = $create_by->getName();
  }
  $this->attachments = Doctrine_Core::getTable('Attachments')
              ->createQuery()
              ->addWhere('bind_id=?',$this->tickets->getId())
              ->addWhere('bind_type=?','tickets')
              ->orderBy('id')
              ->fetchArray();
  $data['attachments'] =$this->attachments;
  $detail['id'] = $this->tickets->getId();
  $detail['department'] = $this->tickets->getDepartments()->getName();
  $user = $this->tickets->getDepartments()->getUsers();
  $owner['id'] =  $user->getId();
  $owner['name'] =  $user->getName();
  $owner['photo'] =  $user->getPhoto();
  $detail['user'] = $owner;
  $detail['department'] = $this->tickets->getDepartments()->getName();
  $detail['status'] = $this->tickets->getTicketsStatus()->getName();
  $detail['type'] = $this->tickets->getTicketsTypes()->getName();
  $data['detail'] =$detail;
  $response->data =  $data;
  echo json_encode($response);
  exit();
}
public function executeTasks(sfWebRequest $request)
  {
     if(!$this->setUserToken($request->getParameter('token'))){
           $this->reponeError(); 
          exit();
      }
    $this->projects = Doctrine_Core::getTable('Projects')->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))->fetchOne();
    if(!$this->projects){
        $this->reponeError(); 
          exit();
    }
    $this->tasks = Doctrine_Core::getTable('Tasks')->createQuery()->addWhere('id=?',$request->getParameter('id'))->addWhere('projects_id=?',$request->getParameter('projects_id'))->fetchOne();
     if(!$this->tasks){
        $this->reponeError(); 
          exit();
    }
    $this->checkProjectsAccess($this->projects);
    $this->checkTasksAccess('view',$this->tasks, $this->projects);     

      $response = new Response();  
      $response->status = ResponseCode::STATUS_SUCCESS;
      $data['description'] = $this->tasks->getDescription();
      $data['id'] = $this->tasks->getId();
      $data['create_at'] = $this->tasks->getCreatedAt();
      $create_by  = Doctrine_Core::getTable('Users')->find($this->tasks->getCreatedBy());
      if($create_by){
        $data['users'] = $create_by->getId();
        $data['photo'] = $create_by->getPhoto();
        $data['name'] = $create_by->getName();
      }
      $this->attachments = Doctrine_Core::getTable('Attachments')
                  ->createQuery()
                  ->addWhere('bind_id=?',$this->tasks->getId())
                  ->addWhere('bind_type=?','projects')
                  ->orderBy('id')
                  ->fetchArray();
      $attachments = array();
      $data['attachments'] =$this->attachments;

      $detail['id'] = $this->tasks->getId();
      $detail['label'] = $this->tasks->getTasksLabels()->getName();
      $detail['status'] = $this->tasks->getTasksStatus()->getName();
      $detail['priority'] = $this->tasks->getTasksPriority()->getName();
      $detail['type'] = $this->tasks->getTasksTypes()->getName();
      $users = Doctrine_Core::getTable('Users')->createQuery('u')
        ->addWhere("find_in_set(u.id,'".$this->tasks->getAssignedTo()."')")
        ->orderBy('u.name')
        ->fetchArray();
      $detail['assigendTo'] = $users;
      $data['detail'] = $detail;
      $response->data =  $data;
     echo json_encode($response);
     exit();
  }
/****************** getdetail component *******************************/ 
public function executeProjects(sfWebRequest $request)
  {
     if(!$this->setUserToken($request->getParameter('token'))){
           $this->reponeError(); 
          exit();
      }
     $this->projects = Doctrine_Core::getTable('Projects')->createQuery()
                  //  ->leftJoin('p.Users')   
                    ->addWhere('id=?',$request->getParameter('projects_id'))->fetchOne();
       if(!$this->projects){
          $this->reponeError();
      }
    $this->checkProjectsAccess($this->projects);
      $response = new Response();  
      $response->status = ResponseCode::STATUS_SUCCESS;
      $data['description'] = $this->projects->getDescription();
      $data['id'] = $this->projects->getId();
      $data['create_at'] = $this->projects->getCreatedAt();
      $data['users'] = $this->projects->getUsers()->getId();
      $data['photo'] = $this->projects->getUsers()->getPhoto();
      $data['name'] = $this->projects->getUsers()->getName();
      $this->attachments = Doctrine_Core::getTable('Attachments')
                  ->createQuery()
                  ->addWhere('bind_id=?',$this->projects->getId())
                  ->addWhere('bind_type=?','projects')
                  ->orderBy('id')
                  ->fetchArray();
      $data['attachments'] =$this->attachments;
      $detail['id'] = $this->projects->getId();
      $detail['status'] = $this->projects->getProjectsStatus()->getName();
      $detail['type'] = $this->projects->getProjectsTypes()->getName();
     
       $users = Doctrine_Core::getTable('Users')->createQuery('u')
        ->addWhere("find_in_set(u.id,'".$this->projects->getTeam()."')")
        ->orderBy('u.name')
        ->fetchArray();
      $detail['team'] = $users;
       $data['detail'] = $detail;
      $response->data =  $data;

     echo json_encode($response);
     exit();
  }
/****** get projects comments ******************/
public function executeProjectscomments(sfWebRequest $request){
   if(!$this->setUserToken($request->getParameter('token'))){
           $this->reponeError(); 
          exit();
    }
  $offset = $request->getParameter('offset');
  $search = $request->getParameter('search');
  $limit = $request->getParameter('limit');
  $projects_id = $request->getParameter('projects_id');
  if($search == null ){
    $search ='';
  }
  if($offset == null || $limit == null || $projects_id == null ){
    $this->reponeError();
  }
  $this->projects = Doctrine_Core::getTable('Projects')->createQuery()->addWhere('id=?',$projects_id)->fetchOne();
    if(!$this->projects){
          $this->reponeError();
      }
   $this->checkProjectsAccess($this->projects);
        
    $this->projects_comments = Doctrine_Core::getTable('ProjectsComments')
      ->createQuery('pc')                        
      ->leftJoin('pc.Users u')
      ->addWhere('projects_id=?',$projects_id)
        ->addWhere('description LIKE ?', '%'.$search.'%')
      ->orderBy('created_at desc')
      ->limit($limit)
      ->offset( $offset)
      ->fetchArray();
       
       $comments_array = array();
       foreach ($this->projects_comments as $comments) {
         $row['id'] = $comments['id'];
         $row['description'] = $comments['description'];
         $row['avatar'] = $comments['Users']['photo'];
         $row['created_at'] = $comments['created_at'];
         $row['name'] = $comments['Users']['name'];
         $attachments = Doctrine_Core::getTable('Attachments')
                  ->createQuery()
                  ->addWhere('bind_id=?',$comments['id'])
                  ->addWhere('bind_type=?','projectsComments')
                  ->orderBy('id')
                  ->fetchArray();
         $row['attachments'] = $attachments;
         array_push($comments_array,$row);
       }
       $response = new Response();
        $response->status = ResponseCode::STATUS_SUCCESS;
        $response->data = $comments_array;
       echo json_encode($response);
      exit();
}
/******************* projects comments ********************/
public function executeAddcommentproject(sfWebRequest $request)
  {
    if(!$this->setUserToken($request->getParameter('token'))){
           $this->reponeError(); 
          exit();
      }
  $this->projects = Doctrine_Core::getTable('Projects')->createQuery()->addWhere('id=?',$request->getParameter('projects_id'))->fetchOne();
  if(!$this->projects){
          $this->reponeError();
      }
   // $this->checkProjectsAccess($this->projects);
  //  Users::checkAccess($this,'insert','projectsComments',$this->getUser(),$this->projects->getId());
    
  

    $this->form = new ProjectsCommentsForm(null, array('projects'=>$this->projects,'sf_user'=>$this->getUser()));

    $this->processFormProjectComment($request, $this->form);

    exit();
  }
   protected function processFormProjectComment(sfWebRequest $request, sfForm $form)
  {
    $form->bind($request->getParameter($form->getName()), $request->getFiles($form->getName()));
    if ($form->isValid())
    {  
      if($form->getObject()->isNew())
      {    
        $project = Doctrine_Core::getTable('Projects')->find($request->getParameter('projects_id'));            
        if($request->getParameter('projects_types_id')>0){ $project->setProjectsTypesId($request->getParameter('projects_types_id')); }       
        if($request->getParameter('projects_status_id')>0){  $project->setProjectsStatusId($request->getParameter('projects_status_id'));}
        $project->save();
      }
      
      if($form->getObject()->isNew()){ $form->setFieldValue('created_at',date('Y-m-d H:i:s')); }
                            
      $projects_comments = $form->save();
                              
      Attachments::insertAttachments($request->getFiles(),'projectsComments',$projects_comments->getId(),$request->getParameter('attachments_info'),$this->getUser());
      
      ProjectsComments::sendNotification($this,$projects_comments,$this->getUser());
        
    }
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
  class TicketFormResponse{
    public $deparments;
    public $ticketsStatus;
    public $ticketsStatusDefault;
    public $ticketsTypes;
    public $ticketsTypesDefault;
    public $users;
    public $notify;
  }