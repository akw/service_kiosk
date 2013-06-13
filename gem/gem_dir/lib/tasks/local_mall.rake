namespace :kiosk do
  task :init do
    boxes = `vagrant box list`
    if(/precise32/m.match boxes)
      puts 'box precise32 already defined.'
    else
      system 'vagrant init precise32 http://files.vagrantup.com/precise32.box'
    end
  end

  task :gem_push do
    exec "gem push service_kiosk-0.5.0.gem"
  end

  task :up do
    system 'vagrant up'
  end

  task :destroy do
    system 'vagrant destroy'
  end

  task :mall_start do
    exec "vagrant ssh -c 'cd /vagrant/mall; /usr/local/bin/rake start'"
  end

  task :mall_log do
    exec "vagrant ssh -c 'cd /vagrant/mall; /usr/local/bin/rake log'"
  end

  task :mall_stop do
    exec "vagrant ssh -c 'cd /vagrant/mall; /usr/local/bin/rake stop'"
  end

  task :start do
    unless mall_running?
      unless pid = fork
        write_procfile
        exec "foreman start > log/foreman.log 2>&1 & echo $! > log/pid"
      end
      sleep 0.1
      puts "Mall is opening (pid: #{mall_pid})"
    else
      puts "Mall already opened (pid: #{mall_pid})"
    end
  end

  task :stop do
    if mall_running?
      group = `ps axj`.match(/\d+\s+#{mall_pid}\s+(\d+).*foreman/m)[1]
      system "pkill -g #{group}"
      File.delete pidfile
      File.delete './Procfile'
      puts "Mall is closing..."
    else
      puts "Mall already closed"
    end
  end

  task :log do
    exec "tail -n 200 --pid=#{mall_pid} -f log/foreman.log"
  end

  task :mallcop do
    exec "jruby #{gem_dir}/lib/mallcop.rb"
  end

  def mall_pid
    File.read(pidfile).strip
  end

  def mall_running?
    File.exists?(pidfile)
  end

  def pidfile
    'log/pid'
  end

  def gem_dir
    spec = Gem::Specification.find_by_name("service_kiosk")
    spec.gem_dir
  end

  def write_procfile
    File.open('./Procfile', 'w') {|f| f.write(procfile) }
  end

  def procfile
    result = ''
    Dir["#{Dir.pwd}/kiosks/*"].each do |kiosk| 
      handle = /\/([^\/]+)$/.match(kiosk)[1]
      result << %Q[#{handle}: kiosk kiosk:run #{handle}\n]
    end
    result
  end
end
