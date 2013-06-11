require 'json'
require 'base64'

module ServiceKiosk
  class InternalService
    def initialize(kiosk, service)
      @service = connect(kiosk, service)
    end

    def connect(kiosk, service)
      kiosk_class = class_from_string(class_name_from_locator(kiosk, service))
      kiosk_class.new
    end

    def class_name_from_locator(kiosk_locator, service_name)
      #puts ">>> locator: #{kiosk_locator}, svc: #{service_name}"
      parts = /ruby:\/\/\/(?<kiosk_name>[\w\.]*)/.match(kiosk_locator)
      name = service_name
      if parts[:kiosk_name]
        kiosk_name = parts[:kiosk_name].gsub('.', '::')
        name = kiosk_name + '::' + name
      end
    end

    def class_from_string(str)
      #puts ">>> internal kiosk: class from str: #{str}"
      str.split('::').inject(Object) do |mod, class_name|
        mod.const_get(class_name)
      end
    end

    def call(action, input={})
      input_json = input.to_json
      #puts ">>> kiosk call: #{action} -> #{input_json}" if ENV[LOGGER_LEVEL].to_i < 1
      response = @service.method(action.to_sym).call(JSON.parse input_json)
      JSON.parse response.to_json
    end

    def list(input={})
      call('list', input)
    end

    def read(input={})
      call('read', input)
    end

    def create(input={})
      call('create', input)
    end

    def update(input={})
      call('update', input)
    end

    def delete(input={})
      call('delete', input)
    end
  end
end
