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
			echo "No existing user";
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
			echo "No existing user";
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
			echo "No existing user";
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
				$response["picture"] = $data["armourPicture"];
				$response["bonusKill"] = $data["armourBonusKill"];
				$response["bonusSurv"] = $data["armourBonusSurv"];
                echo json_encode($response);
			}
			 else {
			echo "No existing user";
			}
		} else {
			echo "No existing user";
		}

	 }
}

?>