<?php
include 'deets.php';

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
	 die("Connection failed: " . $conn->connect_error);
} 

if(isset($_GET['proj_password']))
{
$proj_password = sha1($_GET['proj_password']);
}else{
die("404:");
}
if(isset($_GET['proj_email']))
{
$proj_email = $_GET['proj_email'];
}else{
die("404:");
}

$querry = "SELECT * FROM `tb_news_user` WHERE `username` = '{$proj_email}' and `password`='{$proj_password}'";
if ($result=mysqli_query($conn, $querry)) {
	if(mysqli_num_rows($result)>0){
                $row = mysqli_fetch_assoc($result);
                if($row["is_confirmed"] == "0"){
                        $curl = curl_init();
                        //echo $curl;
                        // Set some options - we are passing in a useragent too here
                        curl_setopt_array($curl, array(
                            CURLOPT_RETURNTRANSFER => 1,
                            CURLOPT_URL => 'http://api.msg91.com/api/sendhttp.php?authkey=91761A0Q6OzrJc55e9c0d9&mobiles='.$row["username"].'&message=Your%20code%20is%20'.substr(sha1($row["username"]),0,6).'&sender=OURAPP&route=4&country=0',
                            CURLOPT_USERAGENT => 'Codular Sample cURL Request'
                        ));
                        // Send the request & save response to $resp
                        $resp = curl_exec($curl);
                        //echo $resp;
                        // Close request to clear up some resources
                        curl_close($curl);
                }
                
		die("200:".$row["id"].":".$row["is_confirmed"]);
	}else{
		die("404:");
	}
} else {
	echo "Error: " . $querry . "<br>" . mysqli_error($conn);
}
$conn->close();
?>  