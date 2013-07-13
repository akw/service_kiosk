<?php 

include 'kiosk.php';

class Root {
  public function setLister($lister) {
    $this->lister = $lister;
    error_log("setLister!");
  }
}

$root = new Root();
ServiceKiosk\Kiosk::setup($root);

print "listAll: " . json_encode( $root->lister->listAll()) . "\n";
print "two: " . json_encode( $root->lister->two(3, "key")) . "\n";

?>
