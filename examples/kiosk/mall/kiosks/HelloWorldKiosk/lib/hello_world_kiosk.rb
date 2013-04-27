module Toolshed
  class HelloWorldKiosk
    def list(input=nil)
      if input.nil?
        {'greeting' => 'Hello World'}
      elsif input['q']
        {'greeting' => 'Hello ' + input['q']}
      elsif input['attempt']
        raise 'TEST ERROR'
      end
    end

    def read(id, input={})
      {'greeting' => 'Hello Reader ' + id}
    end

    def create(input={})
      puts ">>> helloworld create input " + input.inspect
      {'greeting' => 'Hello ' + input['box'] }
    end

    def update(id, input={})
      {'greeting' => 'Hello ' + input['box'] }
    end

    def delete(id)
      {'status' => 'ok'}
    end
  end
end
