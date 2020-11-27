<?php
    $jsonString = file_get_contents("events.json");

    $events = json_decode($jsonString, true);

    header("content-type: application/json");

    echo json_encode($events);
?>