# MODULE 0x040 LECTURE 0x110 - NETWORK DISCOVERY

The security of a network is preserved only if the individual devices on it are secure.

## Identifying hosts with ARP

Remember that ARP maps IPs to MACs

-First 3 bytes of MAC address identify vendor
-Last 3 bytes of MAC address identify device


## arp-scan

Program that consults the system table that keeps local IP-to-MAC pairs

* Argument `--interface` specifies a network device
* `--localnet` scans the network you're attached to


## NMAP

The network mapper

- Host discovery
- Port scanning
- Version detection
- OS detection
- Nmap Scripting Engine (scriptable interactions)

* Masscan is a very fast alternative to nmap
    - Does not support NSE
    - Uses custom TCP/IP stack


## Discovering hosts with ICMP Echo

* ICMP Echo (message type 8) will be satisfied by many machines
    - Usually responded to with Echo Reply (type 0)

* Ping scans (unlike ARP) can reach beyond the unrouted local network across routers (it uses IP packets)


## Bash script for ping sweepign

Shell programming is extremely useful to pentesters

```bash
i=0;

    while [ $i -le 255 ]; do

        ping -c 1 -W 1 172.24.0.$i

        let i+=1

    done
```





# MODULE 0x040 LECTURE 0x115 - MORE NETWORK DISCOVERY

## Ping scans don't always work :/

* By default, nmap performs host discovery, sending each of these:
    - IMCP ECHO REQUEST
    - ICMP TIMESTAMP
    - TCP SYN to port 443, and
    - TCP ACK to port 80

* Disable host-discovery with `-Pn`


## Other types of scans

* TCP Connect scan (SYN, SYN/ACK, ACK), with option `--sT`
    - available in nmap if you are not root

* TCP SYN scan, with option `-sY`
    - half-open sacnning, stealthy
    - Receive a SYN/ACK, then port is listening
    - Receive a RST/ACK, then port is not listening

* TCP FIN scan, with `-sF`
    - Unix hosts send RST for closed ports

* TCP Xmas scan with `-sX`
    - FIN, URG, PUSH -- should get back RST for closed ports

* TCP Null scan with `sN`
    - Should send RST for closed ports

* TCP ACK scan with `-sA`
    - Some firewall rules only allow established connection traffic, i.e. those with ACK flags set)

* UDP scan with `-sU`
    - Port unreachable = closed


## IDENTIFYING SERVICES WITH NMAP

* By default, scans 1,000
    - Ports are identified as either Open, Closed, Filtered, Unfiltered, Open|Filtered, Close|Filtered.

* open
    - A packet with SYN received SYN/ACK response
    - Actively accepting TCP connections, UDP datagrams, or SCTP associations

* closed
    - A packet with SYN received RST
    - Accessible, but no appliaction is listening on it. May come alive later

* filtered
    - A packet with SYN received no response whatsoever
    - nmap cannot determine

* unfiltered
    - ACK scan shows that packet gets through firewall, but no other response is specified

* open|filtered
    - Some scan types don't give a response for open ports

* closed|filtered
    - Similar to above


## Nmap Scripting Engine (NSE)

* Written in Lua

* Categories include:
    - Network discovery
    - Version detectionn
    - Vulnerability detection
    - Backdoor detection
    - Vulnerability exploitation

Large network scanners:
* Zmap
    - sends packets to IPs in order determined by cyclic multiplicative group which provides a pseudo-random ordering of the 2^32 IPs in the net (to reduce congestion from the bandwidth)
    - senerates packets at speeds of up to 1.4 million/sec
    - zgrab for banner grabbing in conjuction

* Masscan
    - Custom TCP/IP stack to improve performance
    - A few of the capabilities in nmap, sometimes hard to get right b/c of custom stack





# MODULE 0x040 LECTURE 0x110 - SERVICE DISCOVERY

## What are these services?

The services one would like to discover are provided by programs running on servers (devices running a service)

* A service is a program that will listen for incoming connections on a well-known port
    - When a connection is received, the service will satisfy some API requirements that are most often specified by IETF RFC
    - The RFCs specify how a client and server are to communicate, mainly for common services e.g. FTP

* Just because a port is available doesn't mean that port is actually being used


## QUIZ ACCESS CODE:
[Space] Truckin'--Deep Purple


## One way to do a banner grab

* `nc f4mc0rp.com 80`
    - Use netcat (a program that communicates between stdin, stdout, and the internet) on kali
    - This command attempts to connect to port 80 on the server and type input to see its responses
    - e.g. `??` provides an HTTP header


## Couple of useful scans for nmap

- `-O` for OS detection
- `-A` enables aggressive scan (OS detection, version detection, script scanning and more)


## Most common services

1. 80 (http)
2. 25 (smtp)
3. 22 (ssh)
4. 443 (https)
5. 21 (ftp)
6. 113 (auth -- verify users by email address from a host)
7. 23 (telnet)
8. 53 (domain)
9. 554 (rtsp)
10. 3389 (MS-Term-Server)


## AUTOMATED SCANNERS

Nessus, Nexpose, OpenVAS are most popular commercial network audit tools. Used mainly when stealth doesn't matter

Kali usually acts as both the server and client with these tools





# MODULE 0x050 LECTURE 0x120 - VULNERABILITY SCANNING

## Nessus

* Download and install Nessus, then start the daemon service.
    - systemctl start nessusd
    - Install NASL plugin updates from Tenable through the Settings panel in the Nessus web service

* Different setups
    - One can employ cloud scans that report back to tenable.io
    - Local scans that use Nessus agents in a variety of network locations

* Select scanner type
    - Basic network scan is usally what you want

* Set scan parameters
    - i.e. IPs
    - Host discovery may be done or skipped (like `-Pn` in nmap)

* Nessus scan
    - Vulnerabilities are reported as it goes; ranked according to CVSS 3.0)
    - Actual risk may differ from CVSS score -- sometimes OK to accept some risk depending on context

* Nessus plugin report (click on the plugin number on the scan results page)
    - Gives a description on the vulnerability alone and one or more possible solutions
    - References (e.g. a CVE if available) and metasploit modules may be provided
