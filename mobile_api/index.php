<?php

require_once('../core/config/ProjectConfiguration.class.php');

$configuration = ProjectConfiguration::getApplicationConfiguration('qdPM', 'prod', true);


    $from['kienbk1910@gmail.com'] = 'KienPham';
     $to['kienbk1910@gmail.com'] = 'Test'
    Users::sendEmail($from,$to,"fdsfdsfdsf","hello");
