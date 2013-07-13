<?php 
namespace ServiceKiosk;

include 'rest_request.php';

class Kiosk {
  public static function setup($root) {
    $config = KioskConfig::create();
    $injector = Injector::instance();
    Log::info("// setup");
    $injector->createAllResources($root, $config, "");
    //$injector->injectAll($root, $config, $env, "");
    Log::info("//");
  }

  public function __construct($url) {
    $this->endpoint = $url;
  }

  public function service($name) {
    return new RemoteService($this->endpoint, $name);
  }
}

class Injector {
  static $instance;
  public static function instance() {
    $instance = new Injector();
    $instance->resources = array();
    return $instance;
  }

  public function createAllResources($root, $config, $prefix) {
    foreach($config->dependencies() as $dependency => $value) {
      $url =$config->dependencyUrl($dependency);
      if(null==$url) {
        Log::fatal("No env " . $config->urlEnvName($dependency));
        continue;
      }
      if($this->isInternalResource($url)) {
        Log::fatal("Internal resources are unsupported");
        continue;
      } else {
        //$resource = $this->createResource($dependency, $config, $prefix);
        $resource = new RemoteResource($url);
        $this->injectResource($root, $dependency, $resource);
        //$this->resources[$dependency] = $resource;
      }
    }
  }

  public function injectResource($root, $dependency, $resource) {
    $injector = "set" . ucwords($dependency);
    $rootClass = get_class($root);
    $method = new \ReflectionMethod($rootClass, $injector);
    $method->invoke($root, $resource);
  }

  public function isInternalResource($url) {
    $pos = strpos($url, 'kiosk:');
    return($pos===0);
  }
}

class KioskConfig {
  public static function readConfig() {
    return array( "name" => "test kiosk", "dependencies" => array("lister" => "bar"));
  }

  public static function env() {
    //return $_ENV; // to read from actual env vars, but doesn't make sense with php
    // need to read env vars from file
    return array("KIOSK_LISTER" => "http://localhost:4001/lister");
  }

  public static function create() {
    return new KioskConfig(self::readConfig(), self::env());
  }

  public function __construct($data, $env) {
    $this->data = $data;
    $this->env = $env;
  }

  public function dependencies() {
    return $this->data["dependencies"];
  }

  public function urlEnvName($name) {
    return('KIOSK_' . strtoupper($name));
  }

  public function dependencyUrl($name) {
    return $this->env[$this->urlEnvName($name)];
  }
}

class RemoteResource {
  public function __construct($url) {
    error_log("new REmoteResource: " . $url);
    $this->url = $url;
  }

  public function __call($action, $arguments) {
error_log("__call: " . $action);

    $encoded = base64_encode(json_encode($arguments));
    error_log("<br>action: " . $action);
    error_log("<br>args: " . count($arguments));
    error_log("<br>json: " . json_encode($arguments));
    $rest = new \RestRequest($this->action_url($action), 'POST', $encoded);
    $rest->execute();
    $values = json_decode(base64_decode($rest->getResponseBody()));
    if(sizeof($values) == 1) {return $values[0];}
    if(sizeof($values) == 2 && null===$values[0]) {return $values[1];}
    return null;
  }

  public function action_url($action) {
    return $this->url . '/' . $action;
  }
}

class Log {
  const FINEST=0;
  const FINER=1;
  const FINE=2;
  const DEBUG=3;
  const INFO=4;
  const WARN=5;
  const ERROR=6;
  const FATAL=7;
  public static $visible = self::DEBUG;

  public static function log($level, $message) {
    //error_log("[Kiosk] " . $level);
    error_log("[Kiosk] " . $message);
    //error_log("[Kiosk] " . self::label($level) . $message);
  }

  static function label($level) {
    if($level==self::WARN) {return "WARNING: ";}
    if($level==self::ERROR) {return "ERROR: ";}
    if($level==self::FATAL) {return "FATAL ERROR: ";}
    return "";
  }

  public static function debug($message) {self::log(self::DEBUG, $message);}
  public static function info($message) {self::log(self::INFO, $message);}
  public static function warn($message) {self::log(self::WARN, $message);}
  public static function error($message) {self::log(self::ERROR, $message);}
  public static function fatal($message) {self::log(self::FATAL, $message);}
}
?>
