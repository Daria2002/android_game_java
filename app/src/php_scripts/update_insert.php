<?php
$servername = "sql7.freemysqlhosting.net";
$username = "sql7348673";
$password = "JVyfWihe4a";
$dbname = "sql7348673";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$sql = "INSERT INTO crazy_balls_scores (name, score)
VALUES ('Jack', 8)";

if ($conn->query($sql) === TRUE) {
  echo "New record created successfully";
} else {
  echo "Error: " . $sql . "<br>" . $conn->error;
}
$conn->close();
?>