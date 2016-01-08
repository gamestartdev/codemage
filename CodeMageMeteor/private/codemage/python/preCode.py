
def mc(method, *args):
    from org.gamestartschool.codemage.python import PythonMethodCall
    import time
    methodCall = PythonMethodCall(method, args)
    #This variable injected from Java
    pythonMethodQueue.offer(methodCall)
    #Maybe better to statically reference?
    while not methodCall.isDone():
        time.sleep(.01)
    return methodCall.get()

def mc_fast(method, *args):
    from org.gamestartschool.codemage.python import PythonMethodCall
    import time
    methodCall = PythonMethodCall(method, args)
    #This variable injected from Java
    pythonMethodQueue.offer(methodCall)
startTimestamp = 0
ECHO = True
def trace_function(frame, event, arg):
    import time
    method_name = frame.f_code.co_name
    if int(time.time()) - startTimestamp >= 5:
        raise Exception("Programs cannot take more than 5 seconds!")
    if ECHO and event == 'call' and method_name != 'mc':
        frame = frame.f_back or frame
        print "LINE: %i: %s" % (frame.f_lineno, method_name)
    return trace_function
def settracefunc():
    global startTimestamp
    import sys
    import time
    startTimestamp = int(time.time())
    sys.settrace(trace_function)
settracefunc()