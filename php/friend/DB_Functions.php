    <?php
    
    /**
     * Functions to get and set data in database for friend functions
     * @author Elsa Wide
     */
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
    // returns friend list for player
    function getFriendList($uuid) {
    	$result = mysql_query("SELECT name FROM Account WHERE unique_id IN(SELECT friend_id FROM FriendList WHERE player_id = '$uuid' AND is_friends = 1)");
    	$no_of_rows = mysql_num_rows($result);
    	if ($no_of_rows > 0) {
            return $result;
    	} else {
    		return false;
    	}
    }

    // returns friend request
     function getFriendRequest($uuid) {
        $result = mysql_query("SELECT name FROM Account WHERE unique_id IN(SELECT player_id FROM FriendList WHERE friend_id = '$uuid' AND is_friends = 0)");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            return $result;
        } else {
            return false;
        }
    }

    // gets the name if it exists in database
    function searchUsername($uuid) {
        //cheking if a user with that name exists
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

    // sets a request from a friend to a player
    function sendRequest($uuid, $friend) {
        $result = mysql_query("INSERT INTO FriendList(player_id, friend_id, is_friends) VALUES('$uuid', '$friend', 0)");
    }

    // sets a request as accepted from a friend
    function acceptRequest($uuid, $friend) {
        $result = mysql_query("UPDATE FriendList SET is_friends = 1 WHERE player_id = '$friend' AND friend_id = '$uuid'");
        $result = mysql_query("INSERT INTO FriendList(player_id, friend_id, is_friends) VALUES('$uuid', '$friend', 1)");
    }

    // removes a friend request
    function denyRequest($uuid, $friend) {
        $result = mysql_query("DELETE FROM FriendList WHERE player_id = '$friend' AND friend_id = '$uuid'");
    }

    // gets the unique id of a friend by name
    function getFriendUID($friend){
        $getid = mysql_query("SELECT unique_id FROM Account WHERE name = '$friend'");
        $nameArray = mysql_fetch_array($getid);
        $friend = $nameArray[0];
        return $friend;
    }

    // returns the players relation to an other player
    function notFriend($uuid, $friend) {
        $string;
        $result1 = mysql_query("SELECT * FROM FriendList WHERE player_id = '$uuid' AND friend_id = '$friend' AND is_friends = 1");
        $no_of_rows1 = mysql_num_rows($result1);
        $result2 = mysql_query("SELECT * FROM FriendList WHERE player_id = '$uuid' AND friend_id = '$friend' AND is_friends = 0");
        $no_of_rows2 = mysql_num_rows($result2);
        $result3 = mysql_query("SELECT * FROM FriendList WHERE friend_id = '$uuid' AND player_id = '$friend'");
        $no_of_rows3 = mysql_num_rows($result3);
        if ($no_of_rows1 > 0) {
            $string = "You are already friends!";
            return $string;
        } else if ($no_of_rows2 > 0){
            $string = "A friend request is already sent!";
            return $string;
        } else if ($no_of_rows3 > 0){
            $string = "This user has sent you a friend request!";
            return $string;
        } else {
            // user not existed
            return false;
        }
    }

    // removes the relationship of two players
    function removeFriend($uuid, $friend) {
        $result = mysql_query("DELETE FROM FriendList WHERE player_id = '$uuid' AND friend_id = '$friend'");
        $result = mysql_query("DELETE FROM FriendList WHERE player_id = '$friend' AND friend_id = '$uuid'");
    }

    // returns true if a player is online
    function friendOnline($friend) {
        $result = mysql_query("SELECT * FROM Account WHERE unique_id = '$friend' AND online = 1");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            //user is online
            return true;
        } else {
            //user is offline
            return false;
        }
    }

    // checks if a user exists in database
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