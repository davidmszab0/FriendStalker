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

    function setUserPicture($uuid) {
        $filename = 'http://www.davidmszabo.com/maffia/img/profilePictures/' . $uuid . '.png';
        mysql_query("UPDATE Account SET picture = '$filename' WHERE unique_id = '$uuid'");
        $affected_rows = mysql_affected_rows();
        if ($affected_rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Get all the profile data (kills, deaths, weapon and armour) for profile
    function getProfile($uuid) {
        $result = mysql_query("SELECT sw.noKills, sw.noDeaths, sw.unique_id, sw.weaponName, sw.weaponPicture, sw.weaponBonusKill, sw.weaponBonusSurv, a.armourName, a.armourPic, a.armourBonusKill, a.armourBonusSurv FROM 
        (SELECT noKills, noDeaths, s.unique_id, weaponName, weaponPicture, weaponBonusKill, weaponBonusSurv FROM 
        (SELECT noKills, noDeaths, unique_id FROM Statistics
        INNER JOIN Account ON Account.account_id = Statistics.account_id
        WHERE unique_id =  '$uuid') AS s
        INNER JOIN 
        (SELECT weaponName, weaponPicture, weaponBonusKill, weaponBonusSurv, unique_id
        FROM Weapon
        INNER JOIN PlayerWeapon ON Weapon.weaponId = PlayerWeapon.weaponId
        INNER JOIN Account ON Account.account_id = PlayerWeapon.account_id
        WHERE Account.unique_id =  '$uuid') AS w ON s.unique_id = w.unique_id) AS sw
        INNER JOIN
        (SELECT armourName, armourPic, armourBonusKill, armourBonusSurv, unique_id FROM Armour 
        INNER JOIN  PlayerArmour ON Armour.armour_id = PlayerArmour.armour_id 
        INNER JOIN Account ON Account.account_id = PlayerArmour.account_id 
        WHERE Account.unique_id = '$uuid') AS a ON sw.unique_id = a.unique_id");
        $no_of_rows = mysql_num_rows($result);
        if($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            return $result;
        } else {
            return false;
        }
    }
}
 
?>