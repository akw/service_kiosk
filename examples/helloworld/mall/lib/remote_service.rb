require 'json'
require 'rest_client'
require 'open-uri'

module ServiceKiosk
  class RemoteService
    def initialize(kiosk, service)
      @endpoint = external_host(kiosk + '/' + service)
    end

    def url(id=nil, input=nil)
      @endpoint + (id ? ('/' + id) : '') + (input ? '?data=' + CGI::escape(input.to_json) : '')
    end

    def action_url(action)
      @endpoint + '/' + action
    end

    def external_host(id)
      # TODO: this hack is to correct the fact that sinatra using jruby on vbox ubuntu won't
      #   bind properly to all interfaces 0.0.0.0
      host = /inet addr:(\S+)/m.match(`ifconfig eth0`)[1]
      id.sub('localhost', host)
    end

    def call(action, input={})
puts ">>> kiosk call: #{action} -> #{input.to_json}"
      response = RestClient.post action_url(action), {data: input.to_json}
      JSON.parse response
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
