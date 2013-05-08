module OutputLogger
  class Logger
    def log(input={})
      input.each {|level,text| output(level, text)}
      {}
    end

    def output(level, text)
      puts ts + " #{level.upcase} | " + text
    end

    def ts
      Time.now.strftime('%H:%M:%S')
    end
  end
end
