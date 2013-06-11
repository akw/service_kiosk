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

    def self.gem_dir
      spec = Gem::Specification.find_by_name("service_kiosk")
      #gem_root = spec.gem_dir
      #gem_lib = gem_root + "/lib"
      spec.gem_dir
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

Dir[ServiceKiosk::Kiosk.gem_dir + "/lib/tasks/**/*.rake"].each { |ext| puts "loading #{ext}"; load ext } if defined?(Rake)
