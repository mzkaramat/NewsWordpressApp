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

$querry = "SELECT * FROM `tb_news_user` WHERE `email` = '{$proj_email}' and `password`='{$proj_password}'";

if ($result=mysqli_query($conn, $querry)) {
	if(mysqli_num_rows($result)>0){
                $row = mysqli_fetch_assoc($result);
		die("200:".$row["id"]);
	}else{
		die("404:");
	}
} else {
	echo "Error: " . $querry . "<br>" . mysqli_error($conn);
}
$conn->close();
?>  