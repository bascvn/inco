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
 * TasksComments
 * 
 * This class has been auto-generated by the Doctrine ORM Framework
 * 
 * @package    sf_sandbox
 * @subpackage model
 * @author     Your name here
 * @version    SVN: $Id: Builder.php 7490 2010-03-29 19:53:27Z jwage $
 */
class TasksComments extends BaseTasksComments
{
  public static function sendNotification($c,$comment,$sf_user)
  {
    $to = array();
    $tokens = array();// Kien add
    $push =  new Push();//Kien add
    if(sfConfig::get('app_notify_all_tasks')=='on')
    {
      $users_list = Projects::getTeamUsersByAccess($comment->getTasks()->getProjectsId(),'tasks');                 
    }
    else
    {
      $users_list = explode(',',$comment->getTasks()->getAssignedTo());
    }
      
    foreach($users_list as $v)
    {
      if($u = Doctrine_Core::getTable('Users')->find($v))
      {        
        $to[$u->getEmail()]=$u->getName();
        $tokens[$u->getEmail()]=Tokens::getTokensByUserID($u->getId()); // Kien add            
      }
    }
          
    $user = $sf_user->getAttribute('user');
    
    $from[$user->getEmail()] = $user->getName();  
    $to[$user->getEmail()] = $user->getName();  
    $tokens[$user->getEmail()]=Tokens::getTokensByUserID($user->getId()); // Kien add            

    $to[$comment->getTasks()->getUsers()->getEmail()] = $comment->getTasks()->getUsers()->getName();
    $tokens[$comment->getTasks()->getUsers()->getEmail()]=Tokens::getTokensByUserID($comment->getTasks()->getUsers()->getId()); // Kien add            

    $push->setTitle($user->getName());// kien add
    $push->setPhoto($user->getPhoto());// kiean add
    $push->setProject($comment->getTasks()->getProjectsId());// kiean add
    $push->setID($comment->getTasksId());// kiean add  
    $tokens[$user->getEmail()]=Tokens::getTokensByUserID($user->getId()); // Kien add       

    $tasks_comments = Doctrine_Core::getTable('TasksComments')
      ->createQuery()
      ->addWhere('tasks_id=?',$comment->getTasksId())      
      ->orderBy('created_at desc')
      ->execute();
      
    foreach($tasks_comments as $v)
    { 
      if(!array_key_exists($v->getUsers()->getEmail(),$to)) {   
        $to[$v->getUsers()->getEmail()]=$v->getUsers()->getName();
        $tokens[$v->getUsers()->getEmail()]=Tokens::getTokensByUserID($v->getUsers()->getId()); //kien add
      }      
    }
    
    if(sfConfig::get('app_send_email_to_owner')=='off')
    {
      unset($to[$user->getEmail()]);
      unset($tokens[$user->getEmail()]);// kien add                  
             
    }
    // kien add send gcm 
     $gcm = new GCM();
     $push->setComponent('comments');
    
     $description = $comment->getDescription();
     $description = html_entity_decode($description, ENT_QUOTES, 'UTF-8');
     $description =strip_tags($description) ;
     
     if(strlen($description)>200){
       $description = substr($description,0,200);
        $description = substr($description, 0, strrpos($description, ' '));
     }
     
     
     $push->setMessage($description);
     $push->setParent($comment->getTasks()->getName());
     $attachments = Doctrine_Core::getTable('Attachments')
                  ->createQuery()
                  ->addWhere('bind_id=?',$comment->getId())
                  ->addWhere('bind_type=?','comments')
                  ->orderBy('id')
                  ->fetchArray();
      if(count($attachments)>0){
        $push->setAttach($attachments[0]['file']);
      }
      $push->setDate($comment->getCreatedAt());
     $tokens = Tokens::arrayMergeTokens($tokens);
     if(count($tokens) >0){
       $result = $gcm->sendMultiple($tokens,$push->getPush());  
     }   

    $subject = t::__('New Task Comment') . ': ' . $comment->getTasks()->getProjects()->getName() . ' - ' . $comment->getTasks()->getName() . ($comment->getTasks()->getTasksStatusId()>0 ? ' [' . $comment->getTasks()->getTasksStatus()->getName() . ']':'');
    $body  = $c->getComponent('tasksComments','emailBody',array('tasks'=>$comment->getTasks()));
                
    Users::sendEmail($from,$to,$subject,$body,$sf_user);
  }
  
  public static function getTotalWorkHours($id)
  {  
    $total = Doctrine_Core::getTable('TasksComments')
            ->createQuery()
            ->addSelect("sum(worked_hours) as total")
            ->addWhere('tasks_id=?',$id)
            ->groupBy('tasks_id')
            ->fetchOne(array(), Doctrine::HYDRATE_ARRAY);
          
    return $total['total'];
  }
}
