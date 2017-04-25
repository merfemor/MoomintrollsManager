<?php
$file_name = 'questions.txt';

$question = $_POST['question'];
$email = $_POST['email'];
$myfile = fopen($file_name, "a");
fwrite($myfile, "Email: $email\nQuestion:\n$question\n------------------\n");
fclose($myfile);
echo "Your mesage was successfully sent.<br>We will contact you by $email<br>_________________<br>Technical support of the \"Moomintrolls Manager\"";

?>
