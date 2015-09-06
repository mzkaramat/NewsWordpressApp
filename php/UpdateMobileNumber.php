<?php
include 'deets.php';

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
	 die("Connection failed: " . $conn->connect_error);
} 

if(isset($_GET['proj_mobile']))
{
$proj_mobile = ($_GET['proj_mobile']);
}else{
die("404:");
}
if(isset($_GET['proj_user']))
{
$proj_user = ($_GET['proj_user']);
}else{
die("404:");
}

$querry = "update `tb_news_user` set username = '{$proj_mobile}' WHERE `username` = '{$proj_user}'";
//echo $querry;
if ($result=mysqli_query($conn, $querry)) {
	//echo substr(sha1($_GET['proj_mobile']),0,6);
        $curl = curl_init();
        //echo $curl;
        // Set some options - we are passing in a useragent too here
        curl_setopt_array($curl, array(
            CURLOPT_RETURNTRANSFER => 1,
            CURLOPT_URL => 'http://api.msg91.com/api/sendhttp.php?authkey=91761A0Q6OzrJc55e9c0d9&mobiles='.$_GET['proj_mobile'].'&message=Your%20code%20is%20'.substr(sha1($_GET['proj_mobile']),0,6).'&sender=OURAPP&route=4&country=0',
            CURLOPT_USERAGENT => 'Codular Sample cURL Request'
        ));
        // Send the request & save response to $resp
        $resp = curl_exec($curl);
        //echo $resp;
        // Close request to clear up some resources
        curl_close($curl);
} else {
	echo "Error: " . $querry . "<br>" . mysqli_error($conn);
}



$conn->close();
?>  