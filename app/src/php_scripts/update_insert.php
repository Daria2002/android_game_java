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

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  // collect value of input field
  $name = $_POST["name"];
  $score = $_POST["score"];
  $stmt = $conn->prepare("INSERT INTO crazy_balls_scores (name, score) VALUES (?, ?)");
  $stmt->bind_param("ss",$name, $score);
  $stmt->execute();
} else {
    echo "No post";
}

$conn->close();
?>