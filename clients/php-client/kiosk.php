<?php 
namespace ServiceKiosk;

include 'rest_request.php';

class Kiosk {
  public function __construct($url) {
    $this->endpoint = $url;
  }

  public function service($name) {
    return new RemoteService($this->endpoint, $name);
  }
}

class RemoteService {
  public function __construct($endpoint, $service) {
    $this->endpoint = $endpoint . "/" . $service;
  }

  public function call($action, $input=null) {
    $encoded = base64_encode(json_encode($input));
    print "<br>encoded: $encoded<br>\n";
    $rest = new \RestRequest($this->action_url($action), 'POST', $encoded);
    $rest->execute();
    return json_decode(base64_decode($rest->getResponseBody()));
  }

  private function action_url($action) {
    $result = $this->endpoint . "/" . $action;
    print "action url result: " . $result . "<p>";
    return $result;
  }

  public function list_resources($input=null) {
    return $this->call('list', $input);
  }

  public function read_resource($input=null) {
    return $this->call('read', $input);
  }

  public function create_resource($input=null) {
    return $this->call('create', $input);
  }

  public function update_resource($input=null) {
    return $this->call('update', $input);
  }

  public function delete_resource($input=null) {
    return $this->call('delete', $input);
  }
}
?>
