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
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO Account(unique_id, name, email, password, salt, created_at) 
            VALUES('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW())");
        // check for successful store
        if ($result) {
            // get user details 
            $uid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM Account WHERE account_id = $uid");
            $stats = mysql_query("INSERT INTO Statistics(account_id) VALUES($uid)");
            $weapon = mysql_query("INSERT INTO PlayerWeapon(account_id) VALUES($uid)");
            $armour = mysql_query("INSERT INTO PlayerArmour(account_id) VALUES($uid)");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    public function storeUserInTarget($uuid) {
        $result = mysql_query("INSERT INTO Target(player_id) VALUES('$uuid')");
        // check for successful store
        if ($result) {
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    public function changePassword($uuid, $password) {
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash['encrypted'];
        $salt = $hash['salt'];
        $result = mysql_query("UPDATE Account SET password = '$encrypted_password', salt='$salt' WHERE unique_id = '$uuid'");
        if ($result) {
            return true;
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
        $result = mysql_query("SELECT * FROM Account WHERE email = '$email'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /* Checks if the user exist */
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
 
    /**
     * Check if the e-mail already exists in the db
     */
    public function isEmailExisted($email) {
        $result = mysql_query("SELECT email from Account WHERE email = '$email'");
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
     * Check if the name already exists in the db
     */
    public function isNameExisted($name) {
        $result = mysql_query("SELECT name from Account WHERE name = '$name'");
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
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
}
 
?>