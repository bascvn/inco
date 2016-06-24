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
 * Discussions
 * 
 * This class has been auto-generated by the Doctrine ORM Framework
 * 
 * @package    sf_sandbox
 * @subpackage model
 * @author     Your name here
 * @version    SVN: $Id: Builder.php 7490 2010-03-29 19:53:27Z jwage $
 */
class Discussions extends BaseDiscussions
{
  public static function hasViewOwnAccess($sf_user,$discussions,$project)
  {
    if(Users::hasAccess('view_own','discussions',$sf_user,$project->getId()))
    {      
      if(!in_array($sf_user->getAttribute('id'),explode(',',$discussions->getAssignedTo())) and $discussions->getUsersId()!=$sf_user->getAttribute('id'))
      {
        return false;
      }
      else
      {
        return true;
      }
    }
    else
    {
      return true;
    }
  }
  
  public static function checkViewOwnAccess($c,$sf_user,$discussions,$project)
  {
    if(Users::hasAccess('view_own','discussions',$sf_user,$project->getId()))
    {      
      if(!in_array($sf_user->getAttribute('id'),explode(',',$discussions->getAssignedTo())) and $discussions->getUsersId()!=$sf_user->getAttribute('id'))
      {
        $c->redirect('accessForbidden/index');
      }
    }
  }
  
  public static function sendNotification($c,$discussions,$send_to,$sf_user)
  {
    foreach($send_to as $type=>$users)
    {
      switch($type)
      {
        case 'status': $subject = t::__('Discussion Status Updated');
          break;
        default: $subject = t::__('New Discussion');
          break;
      }
      $tokens = array();// Kien add
      $push =  new Push();//Kien add
      $to = array();
      foreach($users as $v)
      {
        if($u = Doctrine_Core::getTable('Users')->find($v))
        {
          $to[$u->getEmail()]=$u->getName();
          $tokens[$u->getEmail()]=Tokens::getTokensByUserID($u->getId()); // Kien add  
        }
      }
                
      $user = $sf_user->getAttribute('user');
      
      $from[$user->getEmail()] = $user->getName();


      $to[$discussions->getUsers()->getEmail()] = $discussions->getUsers()->getName();        
      $to[$user->getEmail()] = $user->getName(); 
      $tokens[$user->getEmail()]=Tokens::getTokensByUserID($user->getId()); // Kien add 
      $tokens[$discussions->getUsers()->getEmail()]=Tokens::getTokensByUserID($discussions->getUsers()->getId()); // Kien add            
    $push->setTitle($user->getName());// kien add
    $push->setPhoto($user->getPhoto());// kiean add
    $push->setProject($discussions->getProjectsId());// kiean add
    $push->setID($discussions->getId());// kiean add       

      if(sfConfig::get('app_send_email_to_owner')=='off')
      {
        unset($to[$user->getEmail()]); 
        unset($tokens[$user->getEmail()]);              
      }
    // kien add send gcm 
     $gcm = new GCM();
     $push->setComponent('discussionsComments');
    
     $description = $discussions->getDescription();
     $description = html_entity_decode($description, ENT_QUOTES, 'UTF-8');
     $description =strip_tags($description) ;
     
     if(strlen($description)>200){
       $description = substr($description,0,200);
        $description = substr($description, 0, strrpos($description, ' '));
     }
     
     
     $push->setMessage($description);
     //  $push->setDate($discussions->getCreatedAt());
     $tokens = Tokens::arrayMergeTokens($tokens);  

      $subject .= ': ' . $discussions->getProjects()->getName() . ' - '  .  $discussions->getName() . ($discussions->getDiscussionsStatusId()>0 ? ' [' . $discussions->getDiscussionsStatus()->getName() . ']':'');

      $push->setParent($subject);
       if(count($tokens) >0){
       $result = $gcm->sendMultiple($tokens,$push->getPush());  
     }   

      $body  = $c->getComponent('discussions','emailBody',array('discussions'=>$discussions));
                                    
      Users::sendEmail($from,$to,$subject,$body,$sf_user);
    }                
  }
    
  public static function addFiltersToQuery($q,$filters)
  {    
    $count_e = 0;
    
    foreach($filters as $table=>$fstr)
    {
      $ids = explode(',',$fstr);
      
      switch($table)
      {
        case 'DiscussionsStatus':
            $q->whereIn('d.discussions_status_id',$ids);
          break;         
        case 'DiscussionsAssignedTo':
            $filter_sql_array = array();
            foreach($ids as $id)
            {
              $filter_sql_array[] = 'find_in_set(' . $id . ',d.assigned_to)';
            }
            
            $q->addWhere(implode(' or ',$filter_sql_array));
          break;
        case 'DiscussionsCreatedBy':
            $filter_sql_array = array();
            foreach($ids as $id)
            {
              $filter_sql_array[] = 'find_in_set(' . $id . ',d.users_id)';
            }
            
            $q->addWhere(implode(' or ',$filter_sql_array));
          break; 
          
        case 'Projects':
            $q->whereIn('d.projects_id',$ids);
          break;         
        case 'ProjectsStatus':
            $q->whereIn('p.projects_status_id',$ids);
          break;
        case 'ProjectsTypes':
            $q->whereIn('p.projects_types_id',$ids);
          break;              
      }
      
    }
          
    return $q;  
  }
  
  public static function getReportType($request)
  {
    if((int)$request->getParameter('projects_id')>0)
    {
      return 'filter' . $request->getParameter('projects_id');
    }
    else
    {
      return 'filter';
    }
  }
  

  public static function getDefaultFilter($request,$sf_user)
  {    
    $f = array();
  
    if(($v = app::getDefaultValueByTable('DiscussionsStatus'))>0)
    {
      $f['TicketsStatus'] = $v;
    }
                
    return $f;
  }
  
  public static function  getProgressChoices()
  {
    $c = array(''=>'');      
            
    for($i=5;$i<=100;$i+=5)
    {
      $c[$i]=$i . '%';
    }
    
    return $c;
  }
  
  public static function  getListingOrderByType($q,$type)
  {
   switch($type)
   {
     case 'date_added':           $q->orderBy('d.id desc');
       break;
     case 'date_last_commented':  $q->orderBy('d.last_comment_date desc');
       break;
     case 'name':                 $q->orderBy('LTRIM(p.name), LTRIM(d.name)');
       break;
     case 'priority':             $q->orderBy('LTRIM(p.name), dp.sort_order,LTRIM(dp.name),  LTRIM(d.name)');
       break;
     case 'status':               $q->orderBy('LTRIM(p.name), ds.sort_order,LTRIM(ds.name),  LTRIM(d.name)');
       break;
     case 'type':                 $q->orderBy('LTRIM(p.name), dt.sort_order,LTRIM(dt.name),  LTRIM(d.name)');
       break;
     case 'group':                $q->orderBy('LTRIM(p.name), dg.sort_order,LTRIM(dg.name),  LTRIM(d.name)');
       break;
     default:                     $q->orderBy('LTRIM(p.name), ds.sort_order,LTRIM(ds.name),  LTRIM(d.name)');
      break;            
   }
   
   return $q;
  }
}
