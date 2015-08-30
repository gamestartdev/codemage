from datetime import datetime
from os.path import join, expanduser
home = expanduser("~")

time_string = str(datetime.now()).replace(" ", "_").replace(":", ".")
out = join(home, "badger_backups/badger_backups_"+ time_string)

import subprocess, os, time
get_url = "meteor mongo --url badger.gamestartschool.org"
print "Getting credentials for backup: " + out
result = subprocess.check_output(get_url, stdin=subprocess.PIPE, shell=True)
result 				= result.replace("mongodb://", "")
user, result 		= result.split(":", 1)
password, result 	= result.split("@", 1)
host, database 		= result.split("/", 1)

dump = 'mongodump -u %s -p %s -h %s -d %s -o "%s"' % (user, password, host, database.strip(), out)
#localDump = "mongodump -h localhost:3001 -d meteor -o %s" % (out)
#DANGER# restore = "mongorestore -u %s -p %s -h %s -d %s --drop %s\\%s" % (user, password, host, database.strip(), out, database.strip())
localRestore = 'mongorestore -h localhost:3001 -d meteor --drop %s/badger_gamestartschool_org' % (out)

print "Dumping from server database..."
subprocess.check_output(dump, stdin=subprocess.PIPE, shell=True)
print "Restoring local database..."
subprocess.check_output(localRestore, stdin=subprocess.PIPE, shell=True)
print "Complete."