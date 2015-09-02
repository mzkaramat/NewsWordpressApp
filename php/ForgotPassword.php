<?php
include 'deets.php';

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
	 die("Connection failed: " . $conn->connect_error);
} 

if(isset($_GET['proj_username']))
{
$proj_username = $_GET['proj_username'];
}else{
die("404");
}


$querry = "SELECT * FROM `tb_news_user` WHERE `email` = '{$proj_username}'";

if ($result=mysqli_query($conn, $querry)) {
	if(mysqli_num_rows($result)>0){
		$to      = $proj_username;
		$subject = 'Password change request';
		$message = 'http://somelink.com';
		$from = "From: FirstName LastName <donotreply@socialbase.com>";
                mail($to,$subject,$message,$from);
		die("200");
	}else{
		die("404");
	}
} else {
	echo "Error: " . $querry . "<br>" . mysqli_error($conn);
}
$conn->close();
?>  