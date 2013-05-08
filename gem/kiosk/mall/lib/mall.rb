require 'base64'
require 'sinatra'
require 'json'
require '../../lib/json-simple-1.1.1.jar'
java_import org.json.simple.JSONValue

Dir["#{File.dirname(__FILE__)}/**/*.rb"].each { |f| require(f) }
Dir["#{File.dirname(__FILE__)}/kiosks/**/*.rb"].each { |f| require(f) }
Dir["#{Dir.pwd}/lib/**/*.rb"].each { |f| require(f) }
Dir["#{Dir.pwd}/lib/**/*.jar"].each { |f| require(f) }

host = /inet addr:(\S+)/m.match(`ifconfig eth0`)[1]
set :bind, host
set :port, ENV['KIOSK_PORT']


## New style
post '/:kiosk_name/:service/:action' do
  begin
    data = json_parse(Base64.decode64(params[:data]))
    debug "mall: #{params[:kiosk_name]}.#{params[:service]}.#{params[:action]} with input: " + to_json(data)
    results = kiosk(params[:kiosk_name], params[:service]).method(params[:action].to_sym).call(data)
  rescue Exception => e
    results = {"_ERROR_" => e.message + "\n" + e.backtrace.inspect}
  end
  json_results = to_json results
  debug "  results: #{json_results}"
  Base64.encode64 json_results
end

def class_from_string(str)
  str.split('::').inject(Object) do |mod, class_name|
    mod.const_get(class_name)
  end
end

def is_java?
  ENV['KIOSK_TYPE'] == 'java'
end

def kiosk(kiosk_name, service)
  if is_java?
    java_import(kiosk_name + '.' + service)[0].new
  else
    class_from_string("#{kiosk_name}::#{service}").new
  end
end

def to_json(input)
  if is_java?
    JSONValue.toJSONString input
  else
    input.to_json
  end
end

def json_parse(input)
  return nil unless input
  if is_java?
    JSONValue.parse input
  else
    JSON.parse input
  end
end

def debug(message)
  @log = ServiceKiosk::Kiosk.open('logger').service('Logger') unless @log
  @log.call('log', {'debug' => message})
end

