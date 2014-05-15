<?php 
    
    if (isset($_POST['tag']) && !empty($_POST['tag'])) {
    	$tag = $_POST['tag'];
    	require_once 'DB_Functions.php';
    	$db = new DB_Functions();
    	$response = array("tag" => $tag, "success" => 0, "error" => 0);
    	if($tag == 'friend_id_tag') {
    		$uuid = $_POST['uuid'];
            echo $fid;
    		if($db->userExists($uuid)) {
    			$data = $db->getFriendList($uuid);
    			$return_array = array();
                $numbrows = 0;
                if($data != false) {
    				while($row = mysql_fetch_array($data)){
                        $row_array['friend'] = $row['name'];
                        //$row_array['placement'] = $numbrows;
                        $numbrows++;
                        
                        array_push($return_array, $row_array);
                        
                    }
    				echo json_encode($return_array);
    			} else {
                echo "No friends";
                }
            } else {
                echo "No existing user";
            }
        }
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
                    echo "No friend requests";
                }
            } else {
                echo "No existing user";
            }
        }
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
        else if($tag == 'send_request') {
            $uuid = $_POST['uuid'];
            if($db->userExists($uuid)) {
                $friend = $_POST['friendID'];
                $db->sendRequest($uuid, $friend);
                $response["success"] = 1;
                echo json_encode($response);   
            } else {
            echo "Not existing user";
            }
        }
        /*else if($tag == 'accept_request') {
            $uuid = $_POST['uuid'];
            if($db->userExists($uuid)){
                $friend = $_POST['friendID'];
                $data = $db->acceptRequest($uuid, $friend);
                if($data){
                    $response["success"] = 1;
                    echo json_encode($response);
                } else {
                    echo "Error in data";
                }
            } else {
                echo "Not existing user";
            }
        }*/
        else if($tag == 'deny_request') {
            $uuid = $_POST['uuid'];
            if($db->userExists($uuid)){
                $friend = $_POST['friendID'];
                $data = $db->denyRequest($uuid, $friend);
                if($data){
                    $response["success"] = 1;
                    echo json_encode($response);
                } else {
                    echo "Error in data";
                }
            } else {
                echo "Not existing user";
            }
        }
    }

    ?>