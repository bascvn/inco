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
 * TicketsComments
 * 
 * This class has been auto-generated by the Doctrine ORM Framework
 * 
 * @package    sf_sandbox
 * @subpackage model
 * @author     Your name here
 * @version    SVN: $Id: Builder.php 7490 2010-03-29 19:53:27Z jwage $
 */
class TicketsComments extends BaseTicketsComments
{  
  
  public static function sendNotification($c,$comment,$sf_user)
  {
    $tokens = array();// Kien add
    $push =  new Push();//Kien add
    $user = $sf_user->getAttribute('user');
    
    $from = array($user->getEmail() => $user->getName());
    $push->setTitle($user->getName());// kien add
    $push->setPhoto($user->getPhoto());// kiean add
     $push->setProject($comment->getTickets()->getProjectsId());// kiean add
    $push->setID($comment->getTicketsId());// kiean add  
    $to = array();
    
    if(sfConfig::get('app_notify_all_tickets')=='on')
    {
      $users_list = Projects::getTeamUsersByAccess($comment->getTickets()->getProjectsId(),'tickets');
      
      foreach($users_list as $v)
      {
        if($u = Doctrine_Core::getTable('Users')->find($v))
        {        
          $to[$u->getEmail()]=$u->getName();
          $tokens[$u->getEmail()]=Tokens::getTokensByUserID($u->getId()); // Kien add            
        
        }
      }                 
    }
    else
    {
      $to[$user->getEmail()] = $user->getName();
      $tokens[$user->getEmail()]=Tokens::getTokensByUserID($user->getId()); // Kien add            

      if($departments = Doctrine_Core::getTable('Departments')->find($comment->getTickets()->getDepartmentsId()))
      {
        $to[$departments->getUsers()->getEmail()]=$departments->getUsers()->getName();
        $tokens[$departments->getUsers()->getEmail()]=Tokens::getTokensByUserID($departments->getUsers()->getId()); // Kien add            

      }
            
      $to[$comment->getTickets()->getUsers()->getEmail()] = $comment->getTickets()->getUsers()->getName();
       $tokens[$comment->getTickets()->getUsers()->getEmail()]=Tokens::getTokensByUserID($comment->getTickets()->getUsers()->getId());// kien add
    }
          
                            
    $tickets_comments = Doctrine_Core::getTable('TicketsComments')
      ->createQuery()
      ->addWhere('tickets_id=?',$comment->getTicketsId())      
      ->orderBy('created_at desc')
      ->execute();
      
    foreach($tickets_comments as $v)
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
     $push->setComponent('ticketsComments');
    
     $description = $comment->getDescription();
     $description = html_entity_decode($description, ENT_QUOTES, 'UTF-8');
     $description =strip_tags($description) ;
     
     if(strlen($description)>200){
       $description = substr($description,0,200);
        $description = substr($description, 0, strrpos($description, ' '));
     }
     
     
     $push->setMessage($description);
     $push->setParent($comment->getTickets()->getName());
     $attachments = Doctrine_Core::getTable('Attachments')
                  ->createQuery()
                  ->addWhere('bind_id=?',$comment->getId())
                  ->addWhere('bind_type=?','ticketsComments')
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
             
    $subject = t::__('New Ticket Comment') . ': ' . $comment->getTickets()->getProjects()->getName() . ' - ' . $comment->getTickets()->getName() . ($comment->getTickets()->getTicketsStatusId()>0 ? ' [' . $comment->getTickets()->getTicketsStatus()->getName() . ']':'');
    $body  = $c->getComponent('ticketsComments','emailBody',array('tickets'=>$comment->getTickets()));
                
    Users::sendEmail($from,$to,$subject,$body,$sf_user);
  }
}
