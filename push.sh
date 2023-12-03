#!/usr/bin/expect

set u [lindex $argv 0]
set t [lindex $argv 1]

spawn git push -u origin main
expect "Username for 'https://github.com':"
send "$u\r"

expect "Password for 'https://$u@github.com':"
send "$t\r"

interact
