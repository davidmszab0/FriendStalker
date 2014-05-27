<?php 

/**
 * The index page that handles the POST methods regarding player features 
 * and returns JSON data depending on which tag and other parameters are entered
 * @author Mikaela Lidström and Henrik Edholm
 **/

if (isset($_POST['tag']) && !empty($_POST['tag'])) {

	$tag = $_POST['tag'];

	require_once 'DB_Functions.php';
	$db = new DB_Functions();
	$response = array("tag" => $tag, "success" => 0, "error" => 0);

	if($tag == 'set_location') {

		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
			$lat = $_POST['lat'];
			$long = $_POST['long'];
			$status = $_POST['status'];
						
			if($db->storeUserLocation($uuid, $lat, $long, $status)){
				$response["success"] = 1;
                echo json_encode($response);

			} else {
				$response["error"] = 1;
                $response["error_msg"] = "Error occured in storing location";
                echo json_encode($response);
			}


		} else {
			echo "Not existing user";
		}
	}else if($tag == 'update_target') {

		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
			$target = $_POST['targetUuid'];
						
			if($db->storeUserTarget($uuid, $target)){
				$response["success"] = 1;
                echo json_encode($response);

			} else {
				$response["error"] = 1;
                $response["error_msg"] = "Error occured in updating target";
                echo json_encode($response);
			}


		} else {
			echo "Not existing user";
		} 

	}	else if ($tag == 'get_target_location') {
		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
			$data = $db->getTargetLocation($uuid);
			if($data != false) {
				$response["success"] = 1;
				$response["uuid"] = $data["unique_id"];
				$response["lat"] = $data["latitude"];
				$response["long"] = $data["longitude"];

				echo json_encode($response);
			}
		} else {
			echo "No existing user";
		}
		
	} else if ($tag == 'get_name') {
		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
			$data = $db->getName($uuid);
			if($data != false) {
				$response["success"] = 1;
				$response["name"] = $data["name"];

				echo json_encode($response);
			}
		} else {
			echo "No existing user";
		}
	} else if ($tag == 'get_killer') {
		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
			$data = $db->getKiller($uuid);
			if($data != false) {
				$response["success"] = 1;
				$response["killer"] = $data["killedBy"];

				echo json_encode($response);
			}
		} else {
			echo "No existing user";
		}
	} else if ($tag == 'set_online_status') {
		$uuid = $_POST['uuid'];
		$status = $_POST['status'];
		if($db->userExists($uuid)) {
			$data = $db->setOnlineStatus($uuid, $status);
			if ($data != false) {
				$response["success"] = 1;
				$response["status"] = $status;
				echo json_encode($response);
			}
		} else {
			echo "No existing user";
		}
	
	} else if ($tag == 'get_all') {
		 $data = $db->getAllOnline();
		 $return_array = array();
		 if($data != false) {
			 	while($row = mysql_fetch_array($data)) {
					$row_array['name'] = $row['name']; 
					$row_array['uuid'] = $row['unique_id'];
					$row_array['lat'] = $row['latitude'];
					$row_array['long'] = $row['longitude'];

					array_push($return_array, $row_array);
				}
			//echo $result;
			echo json_encode($return_array);
		 } else {
		 	  echo "something went wrong";
		 	 //echo json_encode($return_array);
		 }
	} else if ($tag == 'get_all_ranking') {
		 $data = $db->getAllRanking();
		 $return_array = array();
		 if($data != false) {
			 	while($row = mysql_fetch_array($data)) {
			 		$row_array["name"] = $row["name"];
					$row_array["killDeath"] = $row["killDeath"];

					array_push($return_array, $row_array);
				}
			//echo $result;
			echo json_encode($return_array);
		 } else {
		 	  echo "something went wrong";
		 	 //echo json_encode($return_array);
		 }
	} else if ($tag == 'get_friend_ranking') {
		 $uuid = $_POST['uuid'];
		 $data = $db->getFriendRanking($uuid);
		 $return_array = array();
		 if($data != false) {
			 	while($row = mysql_fetch_array($data)) {
			 		$row_array["name"] = $row["name"];
					$row_array["killDeath"] = $row["killDeath"];

					array_push($return_array, $row_array);
				}
			//echo $result;
			echo json_encode($return_array);
		 } else {
		 	  echo "something went wrong";
		 	 //echo json_encode($return_array);
		 }
	} else if ($tag == 'get_all_uid') {
		 $uuid = $_POST['uuid'];
		 $data = $db->getAllOnlineUsers($uuid);
		 $return_array = array();
		 if($data != false) {
			 	while($row = mysql_fetch_array($data)) {
					
					$row_array["uuid"] = $row["unique_id"];

					array_push($return_array, $row_array);
				}
			//echo $result;
			echo json_encode($return_array);
		 } else {
		 	  $row_array['uuid'] = "0";
		 	  array_push($return_array, $row_array);
		 	  echo json_encode($return_array);
		 }
	} else if ($tag == 'update_statistics') {
		$uuid = $_POST['uuid'];
		$target = $_POST['target'];
		if($db->userExists($uuid)) {
			if($db->userExists($target)) {

				$data = $db->updateStatistics($uuid, $target);
				$response["success"] = 1;
				echo json_encode($response);
			}else {
				echo "No existing target";
			}
		}else {
			echo "No existing user";
		}
	}  else {
		echo "Tag not found";
	}

}
?>