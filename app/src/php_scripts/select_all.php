<?php
$servername = "sql7.freemysqlhosting.net";
$username = "sql7348673";
$password = "JVyfWihe4a";
$dbname = "sql7348673";

header('Content-type: application/json');

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT name, score FROM crazy_balls_scores";
$result = $conn->query($sql);

$rows = array();

//retrieve and print every record
while($r = $result->fetch_assoc()){
    // $rows[] = $r; has the same effect, without the superfluous data attribute
    $rows[] = $r;
}

// now all the rows have been fetched, it can be encoded
echo json_encode($rows);
$conn->close();
?>