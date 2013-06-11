procfile = ''
count = 1
Dir["#{Dir.pwd}/kiosks/*"].each do |kiosk| 
  handle = /\/([^\/]+)$/.match(kiosk)[1]
  puts kiosk + ' -> ' + handle
  procfile << %Q[#{handle}: kiosk kiosk:run #{kiosk} 990#{count}\n]
  count += 1
end
File.open('./Procfile', 'w') { |file| file.write(procfile) }
