<?php 
    
/**
 * The index page that handles the POST methods regarding friend features and returns JSON data depending on which tag and other parameters are entered
 * @author Elsa Wide
 */
    
    if (isset($_POST['tag']) && !empty($_POST['tag'])) {
        $tag = $_POST['tag'];
        require_once 'DB_Functions.php';
        $db = new DB_Functions();
        
        $response = array("tag" => $tag, "success" => 0, "error" => 0);
        
        // returns friend list json
        if($tag == 'friend_id_tag') {
            $uuid = $_POST['uuid'];
            if($db->userExists($uuid)) {
                $data = $db->getFriendList($uuid);
                $return_array = array();
                if($data != false) {
                    while($row = mysql_fetch_array($data)){
                        $row_array['friend'] = $row['name'];
                        array_push($return_array, $row_array);  
                    }
                    echo json_encode($return_array);
                } else {
                    echo json_encode($return_array);
                }
            } else {
                echo "No existing user";
            }
        }

        // returns friend request
        else if($tag == 'get_friend_requests') {
            $uuid = $_POST['uuid'];
            if($db->userExists($uuid)) {
                $data = $db->getFriendRequest($uuid);
                $return_array = array();
                if($data != false) {
                    while($row = mysql_fetch_array($data)){
                        $row_array['friend'] = $row['name']; 
                        array_push($return_array, $row_array);
                    }
                    echo json_encode($return_array);
                } else {
                    echo json_encode($return_array);
                }
            } else {
                echo "No existing user";
            }
        }

        // returns user name
        else if($tag == 'search_username'){
            $uuid = $_POST['uuid'];
                $data = $db->searchUsername($uuid);
                if($data != false) {
                    $response["success"] = 1;
                    $response["friend"] = $data["name"];
                    echo json_encode($response);
                } else {
                    $response["error"] = 1;
                    $response["error_msg"] = "No user by that name";
                    echo json_encode($response);
            }
        }

        // sets a request of a friend
        else if($tag == 'send_request') {
            $uuid = $_POST['uuid'];
            echo $uuid;
            if($db->userExists($uuid)) {
                $friend = $_POST['friendName'];
                echo $friend;
                $friend = $db->getFriendUID($friend);
                echo "hello";
                echo $friend;

                $data = $db->notFriend($uuid, $friend);
                if($data == false) {
                    if($friend != $uuid){
                        $db->sendRequest($uuid, $friend);
                        $response["success"] = 1;
                        echo json_encode($response); 
                    } else {
                        echo "You can't add yourself";
                    }
                } else{
                    $respone["error"] = 1;
                    $response["error_msg"] = $data;
                    echo json_encode($response); 
                }
            } else {
            echo "Not existing user";
            }
        }

        // sets request as accepted
        else if($tag == 'accept_request') {
            $uuid = $_POST['uuid'];
            if($db->userExists($uuid)){
                $friend = $_POST['friendName'];
                $friend = $db->getFriendUID($friend);
                $db->acceptRequest($uuid, $friend);
                $response["success"] = 1;
                echo json_encode($response);
            } else {
                echo "Not existing user";
            }
        }

        // returns a friend id
        else if($tag == 'get_friend_id') {
            $friend = $_POST['friendName'];
            $friend = $db->getFriendUID($friend);
            $response["success"] = 1;
            $response["friendUID"] = $friend;
            echo json_encode($response);
        }

        // returns online status of a player
        else if($tag == 'is_friend_online'){
            $friend = $_POST['friendName'];
            $data = $db->friendOnline($friend);
            if($data != false){
                $response["success"] = 1;
                $response["onlineStatus"] = "User online";
                echo json_encode($response);
            } else {
                $response["error"] = 1;
                $response["onlineStatus"] = "User offline";
                echo json_encode($response);
            }
        }

        // denies friend request
        else if($tag == 'deny_request') {
            $uuid = $_POST['uuid'];
            if($db->userExists($uuid)){
                $friend = $_POST['friendName'];
                $friend = $db->getFriendUID($friend);
                $db->denyRequest($uuid, $friend);
                
            } else {
                echo "Not existing user";
            }
        }

        // deletes the friend relationship
        else if($tag == 'remove_friend') {
            $uuid = $_POST['uuid'];
            if($db->userExists($uuid)){
                $friend = $_POST['friendName'];
                $friend = $db->getFriendUID($friend);
                $db->removeFriend($uuid, $friend);
            } else {
                echo "Not existing user";
            }
        }
    }

    ?>