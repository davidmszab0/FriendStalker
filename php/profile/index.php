<?php 

if (isset($_POST['tag']) && !empty($_POST['tag'])) {

	$tag = $_POST['tag'];

	require_once 'DB_Functions.php';
	$db = new DB_Functions();
	$response = array("tag" => $tag, "success" => 0, "error" => 0);
	if($tag == 'get_user_kills') {

		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
						
			$data = $db->getUserKills($uuid);
			if($data != false) {
				$response["success"] = 1;
				$response["kills"] = $data["noKills"];
                echo json_encode($response);
			}
			 else {
			echo "No existing kills";
			}
		} else {
			echo "No existing user";
		}
	}

	else if($tag == 'get_user_deaths') {

		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
						
			$data = $db->getUserDeaths($uuid);
			if($data != false) {
				$response["success"] = 1;
				$response["deaths"] = $data["noDeaths"];
                echo json_encode($response);
			}
			 else {
			echo "No existing deaths";
			}
		} else {
			echo "No existing user";
		}

	 }
	 else if($tag == 'get_user_weapon') {

		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
						
			$data = $db->getUserWeapon($uuid);
			if($data != false) {
				$response["success"] = 1;
				$response["weaponName"] = $data["weaponName"];
				$response["picture"] = $data["weaponPicture"];
				$response["bonusKill"] = $data["weaponBonusKill"];
				$response["bonusSurv"] = $data["weaponBonusSurv"];
                echo json_encode($response);
			}
			 else {
			echo " No existing weapon";
			}
		} else {
			echo "No existing user";
		}

	 }
	 else if($tag == 'get_user_armour') {

		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
						
			$data = $db->getUserArmour($uuid);
			if($data != false) {
				$response["success"] = 1;
				$response["armourName"] = $data["armourName"];
				$response["picture"] = $data["armourPic"];
				$response["bonusKill"] = $data["armourBonusKill"];
				$response["bonusSurv"] = $data["armourBonusSurv"];
                echo json_encode($response);
			}
			 else {
			echo "No existing armour";
			}
		} else {
			echo "No existing user";
		}

	 }
	 else if($tag == 'get_user_picture') {

		$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
						
			$data = $db->getUserPicture($uuid);
			if($data != false) {
				$response["success"] = 1;
				$response["picture"] = $data["picture"];
                echo json_encode($response);
			}
			 else {
			echo "No existing picture";
			}
		} else {
			echo "No existing user";
		}

	 }
	 else if ($tag == 'set_user_picture'){
	 	$uuid = $_POST['uuid'];
		if($db->userExists($uuid)) {
			$success = $db->setUserPicture($uuid);
			if($success != false) {
				$response["success"] = 1;
				echo json_encode($response);
			} else {
				$response["error"] = 1;
				echo json_encode($response);
			}
		} else {
			echo "No existing user";
		}
	 }
	 else if ($tag == 'get_profil'){
	 	$uuid = $_POST['uuid'];
	 	if($db->userExists($uuid)) {
	 		$data = $db->getProfile($uuid);
	 		if($data != false) {
	 			$response['success'] = 1;
	 			$response['noKills'] = $data['noKills'];
	 			$response['noDeaths'] = $data['noDeaths'];
	 			$response['weaponName'] = $data['weaponName'];
	 			$response['weaponPicture'] = $data['weaponPicture'];
	 			$response['weaponBonusKill'] = $data['weaponBonusKill'];
	 			$response['weaponBonusSurv'] = $data['weaponBonusSurv'];
	 			$response['armourName'] = $data['armourName'];
	 			$response['armourPic'] = $data['armourPic'];
	 			$response['armourBonusKill'] = $data['armourBonusKill'];
	 			$response['armourBonusSurv'] = $data['armourBonusSurv'];
	 			echo json_encode($response);
	 		} else {
	 			$response['success'] = 0;
	 			$response['error'] = 1;
	 			$response['error_message'] = 'No data from query';
	 			echo json_encode($response);
	 		}
	 	} else {
	 		$response['success'] = 0;
	 		$response['error'] = 1;
	 		$response['error_message'] = 'No existing user';
	 		echo json_encode($response);
	 	}
	 }
	 else if ($tag == 'get_profile') {
	 	$uuid = $_POST['uuid'];
	 	if ($db->userExists($uuid)) {
	 		$response['success'] = 1;
	 		$data = $db->getUserKills($uuid);
	 		if($data != false) {
	 			$response["noKills"] = $data["noKills"];
	 		} else {
	 			$response["noKills"] = 0;
	 		}
	 		$data = $db->getUserDeaths($uuid);
	 		if($data != false) {
	 			$response['noDeaths'] = $data['noDeaths'];
	 		} else {
	 			$response['noDeaths'] = 0;
	 		}
	 		$data = $db->getUserPicture($uuid);
	 		if($data != false) {
	 			$response['picture'] = $data['picture'];
	 		} else {
	 			$response['picture'] = 'http://www.davidmszabo.com/maffia/img/profilePictures/default.png';
	 		}
	 		$data = $db->getUserWeapon($uuid);
	 		if($data != false) {
	 			$response['weaponName'] = $data['weaponName'];
	 			$response['weaponPicture'] = $data['weaponPicture'];
	 			$response['weaponBonusKill'] = $data['weaponBonusKill'];
	 			$response['weaponBonusSurv'] = $data['weaponBonusSurv'];
	 		} else {
	 			$response['weaponName'] = 'Poking stick';
	 			$response['weaponPicture'] = 'http://www.davidmszabo.com/maffia/img/weapon/poking_stick.png';
	 			$response['weaponBonusKill'] = 0;
	 			$response['weaponBonusSurv'] = 0;
	 		}
	 		$data = $db->getUserArmour($uuid);
	 		if($data != false) {
	 			$response['armourName'] = $data['armourName'];
	 			$response['armourPic'] = $data['armourPic'];
	 			$response['armourBonusKill'] = $data['armourBonusKill'];
	 			$response['armourBonusSurv'] = $data['armourBonusSurv'];	
	 		} else {
	 			$response['armourName'] = 'Bandaid';
	 			$response['armourPic'] = 'http://www.davidmszabo.com/maffia/img/armour/bandaid.png';
	 			$response['armourBonusKill'] = 0;
	 			$response['armourBonusSurv'] = 0;
	 		}
	 		echo json_encode($response);

	 	} else {
	 		$response['success'] = 0;
	 		$response['error'] = 1;
	 		$response['error_message'] = 'User does not exist';
	 		echo json_encode($response);
	 	}
	 } 
	 else {
	 	echo 'Tag does not exist';
	 }
}

?>