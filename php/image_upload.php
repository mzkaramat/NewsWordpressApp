<?php

if(isset($_POST['ImageName'])){
$imgname = $_POST['ImageName'];
$imsrc = base64_decode($_POST['base64']);
$fp = fopen("images/".$imgname, 'w');
fwrite($fp, $imsrc);
if(fclose($fp)){
	echo "Image uploaded";
}else{
	echo "Error uploading image";
}
}
?>