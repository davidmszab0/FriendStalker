<?php 

/**
 * The index page that handles the POST methods regarding profile data and features 
 * and returns JSON data depending on which tag and other parameters are entered
 * @author Mikaela Lidström and Henrik Edholm
 **/

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
	 else if ($tag == 'collect_item') {
	 	$uuid = $_POST['uuid'];
	 	if($db->userExists($uuid)){
	 		if (rand(0, 1)) {
	 			$response['success'] = 1;
	 			$response['type'] = 'weapon';
	 			$data = $db->getRandomWeapon();
	 			if ($data != false) {
	 				$response['item_new']['id'] = $data['weaponID'];
	 				$response['item_new']['name'] = $data['weaponName'];
	 				$response['item_new']['picture'] = $data['weaponPicture'];
	 				$response['item_new']['bonus_kill'] = $data['weaponBonusKill'];
	 				$response['item_new']['bonus_surv'] = $data['weaponBonusSurv'];
	 			} else {
	 				$response['success'] = 0;
	 				$response['error'] = 1;
	 				$response['error_msg'] = "Error getting data";
	 			}
	 			$data = $db->getUserWeapon($uuid);
	 			if ($data != false) {
	 				$response['item_old']['name'] = $data['weaponName'];
	 				$response['item_old']['picture'] = $data['weaponPicture'];
	 				$response['item_old']['bonus_kill'] = $data['weaponBonusKill'];
	 				$response['item_old']['bonus_surv'] = $data['weaponBonusSurv'];
	 			} else {
	 				$response['success'] = 0;
	 				$response['error'] = 1;
	 				$response['error_msg'] = "Error getting data";
	 			}

	 		} else {
	 			$response['success'] = 1;
	 			$response['type'] = 'armour';
	 			$data = $db->getRandomArmour();
	 			if ($data != false) {
	 				$response['item_new']['id'] = $data['armour_id'];
	 				$response['item_new']['name'] = $data['armourName'];
	 				$response['item_new']['picture'] = $data['armourPic'];
	 				$response['item_new']['bonus_kill'] = $data['armourBonusKill'];
	 				$response['item_new']['bonus_surv'] = $data['armourBonusSurv'];
	 			} else {
	 				$response['success'] = 0;
	 				$response['error'] = 1;
	 				$response['error_msg'] = "Error getting data";
	 			}
	 			$data = $db->getUserArmour($uuid);
	 			if ($data != false) {
	 				$response['item_old']['name'] = $data['armourName'];
	 				$response['item_old']['picture'] = $data['armourPic'];
	 				$response['item_old']['bonus_kill'] = $data['armourBonusKill'];
	 				$response['item_old']['bonus_surv'] = $data['armourBonusSurv'];
	 			} else {
	 				$response['success'] = 0;
	 				$response['error'] = 1;
	 				$response['error_msg'] = "Error getting data";
	 			}

	 		}
	 		echo json_encode($response);
	 	} else {
	 		$response['success'] = 0;
	 		$response['error'] = 1;
	 		$response['error_message'] = 'User does not exist';
	 		echo json_encode($response);
	 	}
	 }
	 else if ($tag == 'set_item') {
	 	$uuid = $_POST['uuid'];
	 	$type = $_POST['type'];
	 	$itemId = $_POST['item_id'];
	 	if($db->setItem($uuid, $type, $itemId)) {
	 		$response['success'] = 1;
	 		echo json_encode($response);
	 	} else {
	 		$response['error'] = 1;
	 		$response['error_msg'] = "Error updating database";
	 		echo json_encode($response);
	 	}

	 }

	
	else if ($tag == 'get_user_killstreak') {
	 	$uuid = $_POST['uuid'];
	 	$data = $db->getKillStreak($uuid);
	 	if ($data != false) {
	 		$response['success'] = 1;
	 		$response['killstreak'] = $data['killstreak'];
	 		echo json_encode($response);
	 	} else {
	 		$response['error'] = 1;
	 		$response['error_msg'] = 'User not found';
	 		echo json_encode($response);
	 	}
 	}

 	else if ($tag == 'set_killstreak') {
 		$uuid = $_POST['uuid'];
 		$killstreak = $_POST['killstreak'];
 		$data = $db->setKillstreak($uuid, $killstreak);
 		if ($data != false) {
 			$response['success'] = 1;
	 		echo json_encode($response);
 		} else {
 			$response['error'] = 1;
	 		$response['error_msg'] = "Error updating database";
	 		echo json_encode($response);
 		}
 	}

	 else if ($tag == 'is_item_collectable') {
	 	$uuid = $_POST['uuid'];
	 	if($db->userExists($uuid)){
	 		$data = $db->isItemCollectable($uuid);
	 		if ($data != false) {
	 			$response['success'] = 1;
	 			$response['item_to_collect'] = $data['item_to_collect'];
	 			echo json_encode($response);
	 		} else {
	 			$response['error'] = 1;
	 			$response['error_msg'] = "Error in query";
	 			echo json_encode($response);
	 		}

	 	} else {
	 		$response['error'] = 1;
	 		$response['error_msg'] = "User does not exist";
	 		echo json_encode($response);
	 	}

	 }

	 else if ($tag == 'set_item_collectable') {
	 	$uuid = $_POST['uuid'];
	 	$collectable = $_POST['item_to_collect'];
	 	if($db->setItemCollectable($uuid, $collectable)) {
	 		$response['success'] = 1;
	 		echo json_encode($response);
	 	} else {
	 		$response['error'] = 1;
	 		$response['error_msg'] = "Error updating database";
	 		echo json_encode($response);
	 	}
	 }


	 else {
	 	echo 'Tag does not exist';
	 }
}

?>