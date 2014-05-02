<?php 

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
	} else if ($tag == 'get_target_location') {
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
			 	while($row = mysql_fetch_array($data))
				{
					$row_array['name'] = $row['name']; 
					$row_array['uuid'] = $row['unique_id'];
					$row_array['lat'] = $row['latitude'];
					$row_array['long'] = $row['longitude'];					
		
					array_push($return_array, $row_array);
				}
			//echo $result;
			echo json_encode($return_array);
		 } else {
		 	 // echo "something"
		 	 echo json_encode($return_array);
		 }


	} else {
		echo "Tag not found";
	}
}

?>