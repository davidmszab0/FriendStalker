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

    function getUserKills($uuid) {
        $result = mysql_query("SELECT noKills FROM Statistics INNER JOIN Account ON 
            Account.account_id = Statistics.account_id WHERE unique_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);

        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            return $result;    
        } else {
            return false;
        }
    }

    function getUserDeaths($uuid) {
        $result = mysql_query("SELECT noDeaths FROM Statistics INNER JOIN Account ON 
            Account.account_id = Statistics.account_id WHERE unique_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);

        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            return $result;    
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

    function getUserWeapon($uuid) {
        $result = mysql_query("SELECT * FROM Weapon 
            INNER JOIN  PlayerWeapon ON Weapon.weaponId = PlayerWeapon.weaponId 
            INNER JOIN Account ON Account.account_id = PlayerWeapon.account_id
            WHERE Account.unique_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);

        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            return $result;    
        } else {
            return false;
        }
    }
    
    function getUserArmour($uuid) {
        $result = mysql_query("SELECT * FROM Armour 
            INNER JOIN  PlayerArmour ON Armour.armour_id = PlayerArmour.armour_id 
            INNER JOIN Account ON Account.account_id = PlayerArmour.account_id
            WHERE Account.unique_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);

        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            return $result;    
        } else {
            return false;
        }
    }

    function getUserPicture($uuid) {
        $result = mysql_query("SELECT picture FROM Account WHERE unique_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);

        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            return $result;    
        } else {
            return false;
        }
    }


}
 
?>