module Toolshed
  class HelloWorldKiosk
    def list(input={})
      if input['q']
        {'greeting' => 'Hello ' + input['q']}
      elsif input['attempt']
        raise 'TEST ERROR'
      else
        {'greeting' => 'Hello World'}
      end
    end

    def read(id, input={})
      {'greeting' => 'Hello Reader ' + id}
    end

    def create(input={})
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
