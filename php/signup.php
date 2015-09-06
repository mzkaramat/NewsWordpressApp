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
if(isset($_GET['proj_password']))
{
$proj_password = sha1($_GET['proj_password']);
}else{
die("404");
}
if(isset($_GET['proj_email']))
{
$proj_email = $_GET['proj_email'];
}else{
die("404");
}

$querry = "INSERT INTO `tb_news_user`(`username`, `password`, `email`, `is_fb_account`) VALUES ('{$proj_username}','{$proj_password}','{$proj_email}',0)";

if (mysqli_query($conn, $querry)) {
	$querry = "INSERT INTO `tb_news_user_details`(`id`) VALUES ((SELECT id FROM `tb_jms_user` WHERE `username` = '{$proj_username}'),'{$proj_username}')";
        mysqli_query($conn, $querry);
} else {
	echo "Error: " . $querry . "<br>" . mysqli_error($conn);
}

//For returning username corresponding id
$querry = "select * from tb_news_user where username = '{$proj_username}';";
if ($result=mysqli_query($conn, $querry)) {
	$row = mysqli_fetch_assoc($result);
		die("200:".$row["id"]);
} else {
	echo "Error: " . $querry . "<br>" . mysqli_error($conn);
}
$conn->close();
?>  