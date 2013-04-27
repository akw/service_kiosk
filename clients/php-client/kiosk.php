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
    $rest = new \RestRequest($this->action_url($action), 'POST', $input);
    $rest->execute();
    return json_decode($rest->getResponseBody());
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

  private function url($id=null, $input=null) {
    $result = $this->endpoint;
    if($id!=null) {
      $result .= "/" . $id;
    }
    if($input!=null && is_array($input) && sizeof($input) > 0) {
      $result .= "?data=" . urlencode(json_encode($input));
    }
    print "url reulst: " . $result . "<p>";
    return $result;
  }
}
?>
