procfile = ''
count = 1
Dir["#{Dir.pwd}/kiosks/*"].each do |kiosk| 
  handle = /\/([^\/]+)$/.match(kiosk)[1]
  puts kiosk + ' -> ' + handle
  procfile << %Q[#{handle}: ruby -e 'Dir.chdir "#{kiosk}"; exec "env KIOSK_PORT=990#{count} ../../lib/start_kiosk.sh"'\n]
  count += 1
end
File.open('./Procfile', 'w') { |file| file.write(procfile) }
