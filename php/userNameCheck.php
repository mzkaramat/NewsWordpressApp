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
die("404:");
}


$querry = "SELECT * FROM `tb_news_user` WHERE `username` = '{$proj_username}'";

if ($result=mysqli_query($conn, $querry)) {
	if(mysqli_num_rows($result)>0){
                die("404");
	}else{
		die("200");
	}
} else {
	echo "Error: " . $querry . "<br>" . mysqli_error($conn);
}
$conn->close();
?>  