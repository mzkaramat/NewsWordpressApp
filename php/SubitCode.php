<?php
include 'deets.php';

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
	 die("Connection failed: " . $conn->connect_error);
} 

if(isset($_GET['proj_code']))
{
$proj_code = ($_GET['proj_code']);
}else{
die("404:");
}
if(isset($_GET['proj_user']))
{
$proj_user = ($_GET['proj_user']);
}else{
die("404:");
}
if(substr(sha1($row["username"]),0,6) == $proj_code){
	$querry = "update `tb_news_user` set is_confirmed = 1 WHERE `username` = '{$proj_username}'";
if ($result=mysqli_query($conn, $querry)) {
	die("100:");
} else {
	echo "Error: " . $querry . "<br>" . mysqli_error($conn);
}
}else{
die("404:");
}


$conn->close();
?>  