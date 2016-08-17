<?php
/**
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class GCM {

    // constructor
    function __construct() {
        
    }

    // sending push message to single user by gcm registration id
    public function send($to, $message) {
        $fields = array(
            'to' => $to,
            'data' => $message,
        );
        return $this->sendPushNotification($fields);
    }

    // Sending message to a topic by topic id
    public function sendToTopic($to, $message) {
        $fields = array(
            'to' => '/topics/' . $to,
            'data' => $message,
        );
        return $this->sendPushNotification($fields);
    }

    // sending push message to multiple users by gcm registration ids
    public function sendMultiple($registration_ids, $message) {
       // var_dump($registration_ids);
        if( count($registration_ids['ios']) >0){
            $inco = new IncoAPNS();
            $inco->cm_push_ios($registration_ids['ios'],json_encode($message),$message['parent'],1);
        }
        if (count($registration_ids['android']) == 0 ){
            return;
        }
        $fields = array(
            'registration_ids' => $registration_ids['android'],
            'data' => $message,
        );
       // echo json_encode($fields);
        return $this->sendPushNotification($fields);
    }

    // function makes curl request to gcm servers
    private function sendPushNotification($fields) {

        // include config
        //include_once __DIR__ . '/../../include/config.php';

        // Set POST variables
        $url = 'https://gcm-http.googleapis.com/gcm/send';
       // $key ='
        $headers = array(
            'Authorization: key=' . 'AIzaSyDRhfKZtk3oEAmS3lk1VlWXqxUzquACziw',
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

        // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
        // Close connection
        curl_close($ch);

        return $result;
    }

}

?>