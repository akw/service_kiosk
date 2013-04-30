require_relative 'remote_service'

module ServiceKiosk
  class Kiosk
    @@kiosks = {}

    def self.open(handle)
      kiosk = @@kiosks[handle]
      if kiosk.nil?
        locator = ENV['KIOSK_' + handle]
        kiosk = @@kiosks[handle] = ServiceKiosk::Kiosk.new locator
      end
      raise "No '#{handle}' kiosk found" unless kiosk
      kiosk
    end

    def self.register(handle, locator)
      @@kiosks[handle] = ServiceKiosk::Kiosk.new locator
    end

    def initialize(id)
      @id = id
      @is_remote = is_remote
    end

    def service(name)
      if @is_remote
        ServiceKiosk::RemoteService.new @id, name
      else
        ServiceKiosk::LocalService.new @id, name
      end
    end

    def is_remote
      /^http/.match @id
    end
  end
end
