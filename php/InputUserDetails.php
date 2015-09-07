<?php
include 'deets.php';

$conn = mysql_connect($servername,$username,$password);
// Check connection
mysql_select_db($dbname, $conn );
if ($conn->connect_error) {
	 die("Connection failed: " . $conn->connect_error);
} 
if(isset($_GET['user_id']))
{
$user_id = $_GET['user_id'];
}else{
die("404");
}
if(isset($_GET['user_address']))
{
$user_address = ($_GET['user_address']);
}else{
die("404");
}
if(isset($_GET['dob']))
{
$dob = $_GET['dob'];
}else{
die("404");
}
if(isset($_GET['age']))
{
$age = $_GET['age'];
}else{
die("404");
}
if(isset($_GET['about']))
{
$about = ($_GET['about']);
}else{
die("404");
}
if(isset($_GET['gender']))
{
$gender = $_GET['gender'];
}else{
die("404");
}
if(isset($_GET['private_mem']))
{
$private_mem = $_GET['private_mem'];
}else{
die("404");
}

$querry = "INSERT INTO `tb_news_user_details`(`id`, `address`, `dob`, `age`, `about`, `gender`, `private`) VALUES ({$user_id},'{$user_address}','{$dob}',{$age},'{$about}','{$gender}',{$private_mem})";

if (mysql_query( $querry)) {
	echo "200";
} else {
	echo "Error: " . $querry . "<br>" . mysqli_error($conn);
}

?>  