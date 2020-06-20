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

$sql = "SELECT name, score FROM crazy_balls_scores";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
  // output data of each row
  while($row = $result->fetch_assoc()) {
    echo "name: " . $row["name"]. " - score: " . $row["score"]. "<br>";
  }
} else {
  echo "0 results";
}
$conn->close();
?>