<?php
function addEvent($dateTime, $notes){
    $current_data = file_get_contents('events.json');  
    $array_data = json_decode($current_data, true);  

    $newId = max(array_map( function($event){ 
                                return $event["id"];}, 
                            $array_data)) + 1;



    $extra = array(
            "id" => $newId,
            "DateTime" => $dateTime,  
            "Notes" => $notes  
    );  

    $array_data[] = $extra;  
    $final_data = json_encode($array_data);  

    if(file_put_contents('events.json', $final_data)){  
            return true;  
    } else {
        return false;
    }
}

// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/x-www-form-urlencoded; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
  
  
// get posted data
//$data = json_decode(file_get_contents("php://input"));
$data = (file_get_contents("php://input"));

if (isset($_POST['DateTime']) && !empty($_POST['DateTime']) &&
    isset($_POST['Notes']) && !empty($_POST['Notes']) )
{
    if(addEvent($_POST['DateTime'], $_POST['Notes'])){
        // set response code - 201 created
        http_response_code(201);
        echo json_encode(array("message" => "Event added."));
    } else{
        // set response code - 503 service unavailable
        http_response_code(503);
        echo json_encode(array("message" => "Unable to add event."));
    }
} else{
    // set response code - 400 bad request
    http_response_code(400);
    echo json_encode(array("message" => "Unable to add event. Recieved data is incomplete."));
}
?>