#!/usr/bin/env bash

apt-get update
apt-get install -y git
apt-get install -y ruby1.9.3
apt-get install -y cucumber
gem install rake
gem install rest-client
gem install rspec
wget --quiet http://jruby.org.s3.amazonaws.com/downloads/1.7.3/jruby-bin-1.7.3.tar.gz
tar zxf jruby-bin-1.7.3.tar.gz
echo "export PATH=\${PATH}:/home/vagrant/jruby-1.7.3/bin" >> /home/vagrant/.profile
export PATH="${PATH}:/home/vagrant/jruby-1.7.3/bin"
jruby -S gem install sinatra
jruby -S gem install cucumber
jruby -S gem install rspec
jruby -S gem install mime-types
jruby -S gem install rest-client
gem install foreman
