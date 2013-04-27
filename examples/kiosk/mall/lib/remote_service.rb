require 'json'
require 'rest_client'
require 'open-uri'

module ServiceKiosk
  class RemoteService
    def initialize(kiosk, resource)
      @endpoint = external_host(kiosk + '/' + resource)
    end

    def url(id=nil, input=nil)
      @endpoint + (id ? ('/' + id) : '') + (input ? '?data=' + CGI::escape(input.to_json) : '')
    end

    def external_host(id)
      # TODO: this hack is to correct the fact that sinatra using jruby on vbox ubuntu won't
      #   bind properly to all interfaces 0.0.0.0
      host = /inet addr:(\S+)/m.match(`ifconfig eth0`)[1]
      id.sub('localhost', host)
    end

    def list(input=nil)
puts ">>> url: " + url(nil, input)
      response = RestClient.get url(nil, input)
      JSON.parse response
    end

    def read(id, input={})
      response = RestClient.get url(id, input)
      JSON.parse response
    end

    def create(input=nil)
      response = RestClient.post url, {data: input.to_json}
      JSON.parse response
    end

    def update(id, input={})
      response = RestClient.put url(id), {data: input.to_json}
      JSON.parse response
    end

    def delete(id)
      response = RestClient.delete url(id)
      JSON.parse response
    end
  end
end
