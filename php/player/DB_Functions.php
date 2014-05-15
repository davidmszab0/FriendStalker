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
 
    function storeUserLocation($uuid, $lat, $long, $status) {
        $result = mysql_query("UPDATE Account SET latitude = $lat, longitude = $long, online = $status WHERE unique_id = '$uuid'");
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    function storeUserTarget($uuid, $targetUser) {
        $result = mysql_query("UPDATE Target SET target_id = '$targetUser' WHERE player_id = '$uuid'");
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    function getTargetId($uuid) {
        $result = mysql_query("SELECT target_id FROM Target WHERE player_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);
        if($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            return $result;
        }else {
            return false;
        }
    }

    function getName($uuid) {
        $result = mysql_query("SELECT name FROM Account WHERE unique_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            return $result;
        } else {
            // user not existed
            return false;
        }
    }

    function userExists($uuid) {
        $result = mysql_query("SELECT * from Account WHERE unique_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            return true;
        } else {
            // user not existed
            return false;
        }
    }

    function getTargetLocation($uuid) {
        $result = mysql_query("SELECT * FROM Account WHERE unique_id = '$uuid'");
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

    function getAllOnline() {
        $result = mysql_query("SELECT * FROM Account WHERE online = 1");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // online users in result
            //$result = mysql_fetch_array($result);
            return $result;
        } else {
            // no one online
            return false;
            
        } 
    }

    function getAllOnlineUsers($uuid) {
        $result = mysql_query("SELECT * FROM Account WHERE online = 1 AND unique_id <> '$uuid'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // online users in result
            //$result = mysql_fetch_array($result);
            return $result;
        } else {
            // no one online
            return false;
        } 
    }

    function setOnlineStatus($uuid, $status) {
        $result = mysql_query("UPDATE Account SET online = $status WHERE unique_id = '$uuid'");
        if ($result) {
            return true;
        } else {
            return false;
        }

    }

}
 
?>