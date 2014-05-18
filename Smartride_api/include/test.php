<?php

$mysql_server_name='fdb3.freehostingeu.com';
$mysql_username='1510674_smartride';
$mysql_password='smartride123';
$mysql_database='1510674_srdata';

$conn = mysql_connect($mysql_server_name, $mysql_username, $mysql_password) or die(mysql_errno());

echo "Hello";

mysql_close($conn)

?>	
