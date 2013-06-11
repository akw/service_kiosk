Gem::Specification.new do |s|
  s.name        = 'service_kiosk'
  s.version     = '0.2.0'
  s.homepage    = 'http://github.com/akw/service_kiosk'
  s.date        = '2013-05-29'
  s.summary     = "Kiosk is a cross-lingual software service component system"
  s.description = <<-EOS
    The Kiosk framework allows you to divide your software system to be divided into
    small, single-purpose software components.  Parameters and return values are passed as data
    that can be expressed in JSON maps.

    Components can be deployed in-process or in another process or on another machine.  Components
    don't have to know who is calling them and references to other components are injected using
    environment variables.  These components can make use of a consistent and simple 
    specification and testing framework built on cucumber.
  EOS
  s.authors     = ["Alex Wong"]
  s.email       = 'alex.wong@toolshed.org'
  s.files        = Dir["{lib}/**/*.rb", "{lib}/**/*.jar", "lib/Procfile.kiosk", "bin/*", "*.md",
                        "{provision}/**/*", "Vagrantfile", "{lib}/tasks/**/*.rake"]
  s.require_path = 'lib'
  #s.files       = ['lib/kiosk.rb', 'lib/remote_service.rb', 'lib/mall.rb', 'lib/mallcop.rb',
  #                  'lib/json-simple-1.1.1.jar', 'lib/internal_service.rb', 'lib/Procfile.kiosk']


  # If you have other dependencies, add them here
  # s.add_dependency "another", "~> 1.2"

  # If you need an executable, add it here
  s.executables = ["kiosk"]
end
