require_relative 'internal_service'
require_relative 'remote_service'

# built-in internal kiosks
Dir["#{File.dirname(__FILE__)}/kiosks/**/*.rb"].each { |f| require(f) }

module ServiceKiosk
  class Kiosk
    @@kiosks = {}

    def self.open(handle_raw)
      handle = handle_raw.upcase
      kiosk = @@kiosks[handle]
      if kiosk.nil?
        locator = ENV['KIOSK_' + handle]
        kiosk = @@kiosks[handle] = ServiceKiosk::Kiosk.new locator if locator
      end
      raise "No '#{handle}' kiosk found" unless kiosk
      kiosk
    end

    def self.register(handle, locator)
      @@kiosks[handle.upcase] = ServiceKiosk::Kiosk.new locator
    end

    def initialize(id)
      @id = id
      @is_remote = is_remote
    end

    def service(name)
      if @is_remote
        ServiceKiosk::RemoteService.new @id, name
      else
        ServiceKiosk::InternalService.new @id, name
      end
    end

    def is_remote
      /^http/.match @id
    end

    # internal default kiosks
    register('logger', 'ruby:///OutputLogger')

  end
end
