module Toolshed
  class HelloWorldKiosk
    def fix(input={})
puts ">>> hwk.fix: #{input.inspect}"
      {'greeting' => 'Hello ' + input['name'] }
    end

    def list(input=nil)
puts ">>> hwk.list: #{input.inspect}"
      if input.nil? || input.empty?
        {'greeting' => 'Hello World'}
      elsif input['q']
        {'greeting' => 'Hello ' + input['q']}
      elsif input['attempt']
        raise 'TEST ERROR'
      end
    end

    def read(input={})
      id = input['id']
      puts ">>>>hwk: id=#{id}, input=#{input.inspect}"
      {'greeting' => 'Hello Reader ' + id}
    end

    def create(input={})
      puts ">>> helloworld create input " + input.inspect
      {'greeting' => 'Hello ' + input['box'] }
    end

    def update(input={})
      {'greeting' => 'Hello ' + input['box'] }
    end

    def delete(input={})
      {'status' => 'ok'}
    end
  end
end
