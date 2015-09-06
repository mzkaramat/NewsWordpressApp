<?php
echo "Start";

$curl = curl_init();
curl_setopt( $curl, CURLOPT_IPRESOLVE, CURL_IPRESOLVE_V4);
echo $curl;
// Set some options - we are passing in a useragent too here
curl_setopt_array($curl, array(
    CURLOPT_RETURNTRANSFER => 1,
    CURLOPT_URL => 'http://google.com'
));
// Send the request & save response to $resp
if(!curl_exec($curl)){
    die('Error: "' . curl_error($curl) . '" - Code: ' . curl_errno($curl));
}
// Close request to clear up some resources
curl_close($curl);

echo "Done";
?>