require 'sinatra'
require 'json'
require '../../lib/json-simple-1.1.1.jar'
java_import org.json.simple.JSONValue

Dir["#{File.dirname(__FILE__)}/**/*.rb"].each { |f| require(f) }
Dir["#{Dir.pwd}/lib/**/*.rb"].each { |f| require(f) }
Dir["#{Dir.pwd}/lib/**/*.jar"].each { |f| require(f) }

host = /inet addr:(\S+)/m.match(`ifconfig eth0`)[1]
set :bind, host
set :port, ENV['KIOSK_PORT']

## New style
post '/:kiosk_name/:service/:action' do
  begin
    puts ">>> #{params[:action]}: body: " + request.body.read
    puts ">>> #{params[:action]}: params: " + request.params.inspect
    data = json_parse(params[:data])
    puts ">>> call: #{params[:kiosk_name]}.#{params[:service]}.#{params[:action]} with input: " + to_json(data)
    results = kiosk(params[:kiosk_name], params[:service]).method(params[:action].to_sym).call(data)
  rescue Exception => e
    results = {"_ERROR_" => e.message }
  end
  to_json results
end


get '/:kiosk_name/:resource' do
  begin
    puts ">>> list: params: " + request.params.inspect
    results = kiosk(params[:kiosk_name], params[:resource]).list json_parse(params[:data])
  rescue Exception => e
    e.backtrace
    results = {"_ERROR_" => e.message }
  end
  to_json results
end

get '/:kiosk_name/:resource/:id' do
  begin
    puts ">>> read: params: " + to_json(json_parse(params[:data]))
    results = kiosk(params[:kiosk_name], params[:resource]).read params[:id], json_parse(params[:data])
  rescue Exception => e
    results = {"_ERROR_" => e.message }
  end
  to_json results
end

post '/:kiosk_name/:resource' do
  begin
    puts ">>> create: params: " + to_json(json_parse(params[:data]))
    results = kiosk(params[:kiosk_name], params[:resource]).create json_parse(params[:data])
  rescue Exception => e
    results = {"_ERROR_" => e.message }
  end
  to_json results
end

put '/:kiosk_name/:resource/:id' do
  begin
    body = request.body.read
    puts ">>> update: params: " + body
    puts ">>> update: params: " + to_json(json_parse(params[:data]))
    results = kiosk(params[:kiosk_name], params[:resource]).update params[:id], json_parse(params[:data])
  rescue Exception => e
    results = {"_ERROR_" => e.message }
  end
  to_json results
end

delete '/:kiosk_name/:resource/:id' do
  begin
    results = kiosk(params[:kiosk_name], params[:resource]).delete params[:id], json_parse(params[:data])
  rescue Exception => e
    results = {"_ERROR_" => e.message }
  end
  to_json results
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
