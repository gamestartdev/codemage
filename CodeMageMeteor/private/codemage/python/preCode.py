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

ECHO = True
def trace_function(frame, event, arg):
    method_name = frame.f_code.co_name
    if ECHO and event == 'call' and method_name != 'mc':
        frame = frame.f_back or frame
        print "LINE: %i: %s" % (frame.f_lineno, method_name)
    return trace_function
import sys
sys.settrace(trace_function)