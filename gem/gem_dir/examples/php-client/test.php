<?php 

include 'kiosk.php';

print "<hr>Read operation: <p/>";

$kiosk = new ServiceKiosk\Kiosk('http://localhost:9902/com.countabout.yodlee.kiosk');
$service = $kiosk->service('YodleeUserKiosk');

print "svc kiosk:<p>\n";
$input = array("id" => "4", "q" => "Herman", "box" => "Mister", "box?" => "yes", "top" => 12, "name" => "Monty");
print json_encode($service->list_resources($input));

print "<hr>svc kiosk read:<p>\n";
$read_input = array("id" => "4");
print json_encode($service->read_resource($read_input));

print "<hr>svc kiosk create:<p>\n";
print json_encode($service->create_resource($input));

print "<hr>svc kiosk update:<p>\n";
print json_encode($service->update_resource($input));

print "<hr>svc kiosk delete:<p>\n";
print json_encode($service->delete_resource($input));

print "<hr>svc kiosk fix:<p>\n";
print json_encode($service->call('fix', $input));

?>
