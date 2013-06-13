require '../../lib/kiosk.rb'

def location(url)
  endpoint = host + ":" + port(stall)
  url.sub(":///", "://" + endpoint + '/')
end

def stall
  /\/([^\/]+)$/.match(`pwd`.rstrip)[1]
end

def host
  /inet addr:(\S+)/m.match(`ifconfig eth0`)[1]
end

def port(input)
  File.read('../../Procfile').split("\n").each do |line|
    if(line.match(input))
      result = /PORT=(\d+)/.match(line)
      return result[1]
    end
  end
  return ''
end

Given(/^the kiosk found at '(.*?)'$/) do |url|
  ServiceKiosk::Kiosk.register('the_kiosk', location(url))
  @kiosk = ServiceKiosk::Kiosk.open('the_kiosk')
end

Given(/^the resource '(.*?)' in the kiosk$/) do |name|
  @resource = @kiosk.service(name)
end

When(/^the resource's (\S+) method is called$/) do |action|
  @result = @resource.call(action, {})
end

When(/^the resource's (\S+) method is called with input '(.*?)'$/) do |action, input|
  @result = @resource.call(action, JSON.parse(input))
end

Then(/^the results include '(.*?)' with value '(.*?)'$/) do |key, value|
  @result[key].should == value
end
