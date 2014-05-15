    <?php
    
    class DB_Functions {
    	private $db;
    	//put your code here
    	// constructor
    	function __construct() {
    	require_once 'DB_Connect.php';
    	// connecting to database
    	$this->db = new DB_Connect();
    	$this->db->connect();
    	}
    // destructor
    function __destruct() {
    
    	}
    function getFriendList($uuid) {
    	$result = mysql_query("SELECT name FROM Account WHERE unique_id IN(SELECT friend_id FROM FriendList WHERE player_id = '$uuid' AND is_friends = 1)");
    	$no_of_rows = mysql_num_rows($result);
    	if ($no_of_rows > 0) {
		    //$result = mysql_fetch_array($result)
            return $result;
    	} else {
    		return false;
    	}
    }

     function getFriendRequest($uuid) {
        $result = mysql_query("SELECT name FROM Account WHERE unique_id IN(SELECT player_id FROM FriendList WHERE friend_id = '$uuid' AND is_friends = 0)");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            //$result = mysql_fetch_array($result)
            return $result;
        } else {
            return false;
        }
    }

    function searchUsername($uuid) {
        $result = mysql_query("SELECT name FROM Account WHERE name = '$uuid'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            $result = mysql_fetch_array($result);
            return $result;
        } else {
            // user not existed
            return false;
        }
    }

    function sendRequest($uuid, $friend) {
    $test = mysql_query("SELECT unique_id FROM Account WHERE name = '$friend'");
    $nameArray = mysql_fetch_array($test);
    $friend = $nameArray[0];
    $result = mysql_query("INSERT INTO FriendList(player_id, friend_id, is_friends) VALUES('$uuid', '$friend', 0)");
        if ($result) {
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    function acceptRequest($uuid, $fid) {
        $result = mysql_query("UPDATE FriendList SET is_friends = 1 WHERE player_id = '$fid' AND friend_id = '$uuid'");
        $result = mysql_query("INSERT INTO FriendList(player_id, friend_id, is_friends) VALUES('$uuid', '$fid', 1)");
        if ($result) {
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    function denyRequest($uuid, $fid) {
        $result = mysql_query("DELETE FROM FriendList WHERE player_id = '$uuid' AND friend_id = '$fid'");
        if($result) {
            return true;
        } else {
            return false;
        }
    }

    function userExists($uuid) {
    	$result = mysql_query("SELECT * FROM Account WHERE unique_id = '$uuid'");
    	$no_of_rows = mysql_num_rows($result);
    	if ($no_of_rows > 0) {
    		// user existed
    		return true;
    	} else {
    		// user not existed
    		return false;
    	}
    	}
    }
     
    ?>