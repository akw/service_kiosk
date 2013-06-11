require_relative 'kiosk'
require 'json'
require 'base64'
require 'rest_client'
require 'open-uri'

module ServiceKiosk
  class RemoteService
    def initialize(kiosk, service)
      @endpoint = external_host(kiosk + '/' + service)
      @logger = ServiceKiosk::Kiosk.open('logger').service('Logger') unless @logger
      debug "remote service wants to use a logger: " + @logger.inspect
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
      input_json = input.to_json
      debug "kiosk call: #{action} -> #{input_json}"
      response = RestClient.post action_url(action), {data: Base64.encode64(input_json)}
      JSON.parse Base64.decode64(response)
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

    def debug(message)
      @logger.call('log', {'debug' => message})
    end
  end
end
