<?php
/**
 * File to handle all API requests
 * Accepts GET and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data
 * check for POST request 
 **/
if (isset($_POST['tag']) && !empty($_POST['tag'])) {
    // get tag
    $tag = $_POST['tag'];
    // include db handler
    require_once 'DB_Functions.php';
    $db = new DB_Functions();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);

    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } else if ($tag == 'register') {
        // Request type is Register new user
        $name = $_POST['name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // check if user is already existed
        if ($db->isEmailExisted($email)) {
            // email already exists - error response
            $response["error"] = 2;
            $response["error_msg"] = "E-mail already existed";
            echo json_encode($response);
        } else if ($db->isNameExisted($name)) {
            // user name already exists - error response
            $response["error"] = 3;
            $response["error_msg"] = "Name already existed";
            echo json_encode($response);
        } else {
            // store user
            $user = $db->storeUser($name, $email, $password);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                $response["uid"] = $user["unique_id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in Registration";
                echo json_encode($response);
            }
        }
    } else if ($tag == 'storeUserInTarget') {
        $uuid = $_POST['uuid'];
         // store user
        if($db->userExists($uuid)) {

            $user = $db->storeUserInTarget($uuid);
            if ($user != false) {
                 // user stored successfully
                $response["success"] = 1;
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in storing user in Target";
                echo json_encode($response);
             }
        } else {
            echo "Not existing user";
        }  
    } else if ($tag == 'change_password') {
        $uuid = $_POST['uuid'];
        $password = $_POST['password'];
        if($db->userExists($uuid)) {
            if($db->changePassword($uuid, $password)) {
                $response['success'] = 1;
                echo json_encode($response);
            } else {
                $response["error"] = 1;
                $response["error_msg"] = "Error in updating password";
                echo json_encode($response);    
            }
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "User does not exist";
            echo json_encode($response);
        }
    } else if ($tag == 'new_password') {
        $email = $_POST['email'];
        if($db->isEmailExisted($email)) {
            $password = $db->newPassword($email);
            if($password != false) {
                sendMail($email, $password);
                $response['success'] = 1;
                $response['password'] = $password;
                echo json_encode($response);
            } else {
                $response['error'] = 1;
                $response['error_msg'] = "Error setting new password";
                echo json_encode($response);
            }

        } else {
            $response["error"] = 1;
            $response["error_msg"] = "No account found with email " . $email;
            echo json_encode($response);
        }
    }


    else {
        echo "Invalid Request";
    }
} else {
    echo "Access Denied";
}

function sendMail($email, $password) {
    $subject="New password for Assassins"; 
    $header="From: ro-reply@davidmszabo.com"; 
    $content= "Hello,\n\nThe password has been changed to the following:\n\n" . $password . "\n\nPlease login and change to a new personal password"; 
    mail($email, $subject, $content, $header); 
}


?>