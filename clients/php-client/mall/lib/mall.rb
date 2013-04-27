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

get '/:kiosk_name/:resource' do
  begin
    results = kiosk(params[:kiosk_name], params[:resource]).list params
  rescue Exception => e
    results = {"_ERROR_" => e.message }
  end
  to_json results
end

get '/:kiosk_name/:resource/:id' do
  begin
    results = kiosk(params[:kiosk_name], params[:resource]).read params[:id], params
  rescue Exception => e
    results = {"_ERROR_" => e.message }
  end
  to_json results
end

post '/:kiosk_name/:resource' do
  begin
    results = kiosk(params[:kiosk_name], params[:resource]).create params
  rescue Exception => e
    results = {"_ERROR_" => e.message }
  end
  to_json results
end

put '/:kiosk_name/:resource/:id' do
  begin
    results = kiosk(params[:kiosk_name], params[:resource]).update params[:id], params
  rescue Exception => e
    results = {"_ERROR_" => e.message }
  end
  to_json results
end

delete '/:kiosk_name/:resource/:id' do
  begin
    results = kiosk(params[:kiosk_name], params[:resource]).delete params[:id]
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

def kiosk(kiosk_name, resource)
  if is_java?
    class_from_string("Java::#{kiosk_name}::#{resource}").new
  else
    class_from_string("#{kiosk_name}::#{resource}").new
  end
end

def to_json(input)
  if is_java?
    JSONValue.toJSONString input
  else
    input.to_json
  end
end

