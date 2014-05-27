<?php

/**
 * Class for profile function for the profile
 * @author Mikaela Lidström and Henrik Edholm
 */
 
class DB_Functions {
 
    private $db;

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

    /**
     * Get player stats of kills
     */
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

    /**
     * Get players stats of deaths
     */

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

    /**
     * Checks if playser exists
     */
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

    /**
     * Get a randomly generated weapon
     */
    function getRandomWeapon() {
        $result = mysql_query("SELECT weaponID, weaponName, weaponPicture, weaponBonusKill, weaponBonusSurv 
            FROM Weapon ORDER BY RAND() LIMIT 1");
        $no_of_rows = mysql_num_rows($result);
        
        if ($no_of_rows > 0) {
            return mysql_fetch_array($result);
        } else {
            return false;
        }

    }

    /**
     * Get a randomly generated armour
     */
    function getRandomArmour() {
        $result = mysql_query("SELECT armour_id, armourName, armourPic, armourBonusKill, armourBonusSurv 
            FROM Armour ORDER BY RAND() LIMIT 1");
        $no_of_rows = mysql_num_rows($result);
        
        if ($no_of_rows > 0) {
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    /** 
     * Get a players weapon
     */
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
    
    /**
     * Get a players armour
     */
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

    /**
     * Get the picture path of a player
     */
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

    /**
     * Sets the path of a players picture
     */
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

    /**
     * Gets a players killstreak
     */
    function getKillStreak($uuid) {
        $result = mysql_query("SELECT killstreak FROM Account WHERE unique_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    /**
     * Set the killatreak of a player
     */
    function setKillstreak($uuid, $killstreak) {
        $result = mysql_query("UPDATE Account SET killstreak = '$killstreak' WHERE unique_id = '$uuid'");
        $affected_rows = mysql_affected_rows();
        if ($affected_rows > 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     *  Get all the profile data (kills, deaths, weapon and armour) for profile
     */
    function getProfile($uuid) {
        $result = mysql_query("SELECT sw.noKills, sw.noDeaths, sw.unique_id, sw.weaponName, sw.weaponPicture, 
            sw.weaponBonusKill, sw.weaponBonusSurv, a.armourName, a.armourPic, a.armourBonusKill, a.armourBonusSurv FROM 
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

    /**
     * Checks if a player has an item to collect
     */
    function isItemCollectable($uuid) {
        $result = mysql_query("SELECT item_to_collect FROM Account WHERE unique_id = '$uuid'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    /**
     * Sets if players has an item to collect 
     */
    function setItemCollectable($uuid, $collectable) {
        mysql_query("UPDATE Account SET item_to_collect = $collectable WHERE unique_id = '$uuid'");
        $affected_rows = mysql_affected_rows();
        if ($affected_rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets a new item for a player
     */
    function setItem($uuid, $type, $itemId) {
        if ($type == 'weapon') {
            mysql_query("UPDATE PlayerWeapon AS pw INNER JOIN Account AS a SET pw.weaponID = $itemId
                WHERE a.account_id = pw.account_id AND a.unique_id = '$uuid'");
            $affected_rows = mysql_affected_rows();
            if ($affected_rows > 0) {
                return true;
            } else {
                return false;
            }

        } else if ($type == 'armour') {
            mysql_query("UPDATE PlayerArmour AS pw INNER JOIN Account AS a SET pw.armour_id = $itemId
                WHERE a.account_id = pw.account_id AND a.unique_id = '$uuid'");
            $affected_rows = mysql_affected_rows();
            if ($affected_rows > 0) {
                return true;
            } else {
                return false;
            }            
        } else {
            return false;
        }
    }
}


?>