# MODULE 0x020 LECTURE 0x090 - OSINT

## OSINT - What is it?

* Info gathered from publicly available sources
    - Newspapers
    - Web-based communities
    - Public data
    - Observations and reporting
    - Professional and academic literature
    - Maps
    - Google!

## OSINT is directed!

* Intelligence is aimed at a specific individual or group regarding some specific aim
    - OSINT has a __target__ and a __purpose__
    - Wilson example: tax records of previous homeowners


## How to do OSINT?

* Center for Digital Content of the NSA's book:
    - [Untangling the Web: A Guide to Internet Research](https://governmentattic.org/8docs/UntanglingTheWeb-NSA_2007.pdf)

* Example: Searching web directories
    - If a web directory contains an index file (e.g. index.html, index.php), then that index file provides a document that searching to that directory will yield.
    - If a server allows directory searching, and there is no index in the file, then the web server will show all files in that directory
    - Usually disabled by secure sites
    - Try out `site:ufl.edu "Index of /" "last modified"` ... leads to a class directory listing of an astronomy class with exam results, homework keys, etc.

* Email addresses can reveal account names
* Links can reveal unpublished web directories
* Forums with entries of users from a company can reveal information about hardware / software used by the company and can often reveal poorly configured software
* Job sites (e.g. Indeed) may identify technologies a company is searching for... may give a clue as to what they're weak in
* Open directories on company sites can reveal all sorts of useful intel
* Pastebin, e.g. looking up "ufl.edu password" search on the site can yield many results


## What kind of tools can we use?

* Recon-ng
    - Web recon framework written in python
    - UI similar to Metasploit
    - Numerous sarchable info sources are consulted, some requiring API keys (Bing, Facebook, Twitter, Shodan, virustotal...)
    - Information identified includes domains, hosts, contacts, IP blocks, network ports, map locations, ...
    - Comprehensive, difficult to use

* Hunter.io
    - Online service that provides email address info
    - Can help phishing
    - Can help password crack


## Shodan

* A project from John Matherly
    - Free upgrade for and .edu shodan account used to be (and may still be) available)
    - Comprehensive database of banner messages from all open-internet accessible devices
    - Can tailor searches for specific IP ranges, ports, strings, etc.

* Examples
    - `port:3389 hostname:edu` shows a list of Windows Remote Desktop Protocol (RDP)-enabled internet facing servers for edu domains
    - `'Serial Number:' 'Built:' hostname:edu` Reveals serial number of network devices, e.g. printers, which may have vulns associated with them

##  PHRASE PART 7
```
Phrase part 7

2y0yXo6GFlpHM5O3aGpYKizmbzWaQl8f7KhEUo+IVe5f/qDxo5GQFDnwYhlSYFYGZ41RgNyQLI5yYcKLHKPM/C6ds3IixO/EGYnQ9CJ7QjHbLeOsIk8JR4E9c6bGDQsSji6i7E6ks2dye+nJsyzFR+9RbTGgEB6/9RyH0PWKwUCZ8lAYNPxpucBQjnMGelEz6qca6Qkkcno2pnyoOO2biotz40R1Qds0aIc4742WL2yHDJ2UcuptOgLlJG0C5vTAP4wYuCWr2JLm8/EveHh+Jo9zLcjFP0hZJ5IxysD+FtziKTYZWvwBRhS+ri+cOcEV9iG1URH8Y2q5AvA3gbkhQMVeAjr+rU/FM7nCxc70u+aCoQV0sR95BXiicUjU4EC2BOUjDguOXIYtywpV7x8DY9oeI75sijQ7MK2V/6lQW423i2xxwezBM9gIyW4Puz26qMJAK1qf78iR5nBDKGx0wap56xowEG+/MbTh/YLj8snmYNqK6IGLs89c7i79oR35RSGvVqnad6irknG1CLwxNANEp9BlX5dKDZs+MCe04XXwlMEsSTvMiOqY0Cv4NXonwMtXlYC5R551NDTI8Qf2FiSlYEZ6n4gPpxIfwpPFHge7I54ZiA0EAoZHENQutfRi3vMmN6+RNAvM7NfBosYsssXs
```





# MODULE 0x020 LECTURE 0x0a0 - OSINT example

## James Comey secret twitter revelation

* Gizmodo article
    - FBI issued a "no comment" response

* Comey spoke at National Security Alliance Dinner on March 2017
    - He mentioned he had an Instagram account with probably nine followers
    - Mentioned all followers were relatives or close friends
    - Feinberg identified Brien Comey, basketball player at Kenyon State, as James Comey's son
    - Searched "Brien Comey" -james on twitter to find his account (verified by post about his father being promoted to FBI director)
    - Feinberg then identified Brien Comey's Instgram account
    - At the time, IG would reveal followers of any account to other users
    - One of those was @reinholdniebuhr who had 10 followers
    - Google search revelealed article about Comey's time at the College of William and Mary (wrote a senior thesis on theologian Reinhold Niebuhr)
    - etc.


## QUIZ ACCESS CODE
Heigh Ho (The Dwarfs Marching Song) - Tom Waits





# MODULE 0x020 LECTURE 0x0b0 - WhoisAndDNS

## Internet Organizations

Internet Corporation for Names and Numbers (ICANN) and Internet Assigned Numbers Authority (IANA): Coordinate assignment of domain names, IP address numbers
    - ICANN delegates its work to the Address Supporting Organization (ASO)
    - ASO allocates IP address blocks to Regional Internet Registries (RIRs; e.g. APNIC, ARIN, LACNIC, RIPE, AfriNIC)

## IPv4 Address

* Composed of 4 octets with ranges from 0-255

* Original vision: internet would be assigned in network chunks of different sizes according to class:
    - Class A networks: first octet starts with 0-bit (0-127), network address is one octet; three octets for host addresses ==> 128 networks,  16 mil+ addresses
    - Class B networks (e.g. UF): first octet starts with bits `10` (128-191), network address is two octets (~16k networks, ~65k addresses)
    - Class C networks: first octet starts with 110 (192-223)

## CIDR - Classless Internet Domain Routing

When scalabiltiy of class-based network assignments became a problem, people started using classless routing.

* The number of bits in the network is variable (1-30 bits for network address, 31-2 bits for host address)
    - One can denote such a network address using e.g. a.b.c.d/n where n is the number of bits in the network address
    - e.g. UF has a class B address expressed as 128.227.0.0/16 in this notation
    - e.g. 192.168.100.0/22 contains hosts 192.168.100.0 to 192.168.103.255
    - ...why? b/c 100 = 0b01100100 and 103 = 0b01100111

* BUT...
    - Actually, 192.168.100.0 is what's known as the **network address** (with all zeros in the host portion of the address)
    - Additionally, 192.168.103.255 is known as the **broadcast address** (with all ones in the host portion)
    - The network address is not used by any host and the broadcast address is used to send a message to every host on the network segment
    - So only 2^(32-22) - 2 = 1022 host addresses are usable by network hosts

* The netmask has 1s in every bit of the network address and 0s in the host bits
    - Thus, for 192.168.100.0/22, the netmask would be 255.255.252.0 (252 = 0b11111100 = 128 + 64 + 32 + 16 + 8 + 4)


## How does DNS map names to numbers?

DNS = Domain Name Service/System/Server

* The **root domain name** is the empty domain name (also called epsilon or lambda)
    - Root nameservers know the IP addresses of all Top Level Domain (TLD) name servers like edu, com, etc.
    - In theory, there are 13 root server addresses. In fact, more than 13 physical servers, provided using anycast addressing (multiple servers that are provided the same IP address)
    - These servers in turn know the IP addresses of their subdomains (the **edu** server knows the address of the **ufl.edu** server)
    - And so forth... (ufl.edu knowss the address of cise.ufl.edu)


## How does DNS map numbers to names?

With the conceptual in-addr.arpa domain

* To find the name of an address like 128.227.205.227, you need to do a DNS lookup on 227.205.227.128.in-addr.arpa
    - This address has its octets reversed so that the most general network part comes rightmost
    - The heirarchical lookup starts (theoretically) at the in-addr.arpa server


## Why care about DNS and IPs?

* Clients often provide partial info about their domains (e.g. only externally-visible resources)
* You will need to understand the structure of client network to fully do the job


## WHOIS servers provide registrant information

* Useful for getting info about people registered to domains
    - [Internic](https://www.internic.net/whois.html)
    - [Whois.com](https://whois.com)

* If complete info is not available, you will be directed to another whois server w/ more complete information
    - Alternatively, you can specify the whois server with a Linux command: `whois -h [name server] domain_name`

* GDPR
    - Enacted April 2016, enforced May 2018
    - EU registrars have a lot less information due to GDPR





# MODULE 0x020 LECTURE 0x0c0 - WhoisAndDNS

## Dig and NSlookup

* The old go-to to identify relationships between host names and IPs was **nslookup**
    - e.g. `nslookup thunder.cise.ufl.edu`

* **nslookup** has been obsoleted by **dig**
    - `dig thunder.cise.ufl.edu`
    - `-x` option for reverse lookups, e.g. `dig -x 128.227.205.239`


## DNS record types

- A: IPv4 addresses
- AAAA: IPv6 addresses
- CNAME: Alias (mapping to another name)
- IPSECKEY: Key record for IPsec
- MX: Mail Exchange (list of mail transfer agents)
- NS: Name server record
- PTR: Pointer to canonical name (for reverse lookups)
- SOA: Start of Authority (identifies authoritative DNS zone)
- SRV: Server specific records


## DNS Zones

* A DNS zone is a portion of the DNS name space that has been delegated to an administrator who maintains a name server for that zone.
    - Examples: edu, cise.ufl.edu, pr0b3.com

* The (often unfollowed) rule is that zone control should follow administrative control boundaries


## Zone transfers

A transfer of all the recorsd of one name server to another (usually used to copy over records when introducing a new name server in a network)

* Zone transfers used to be the stock and trade of nefarious internet evildoers
    - nslookup doesn't support this anymore
    - dig still supports transfer of record type AXFR (which is for zone transfer), but many servers will refuse to honor that.
    - e.g. `dig @ns.name.ufl.edu -t AXFR ufl.edu` returns a "Transfer failed" message

* Nowadays, OSINT can be used to tease out this information out of public records and nameservers

* Are they legal?
    - Ensure you have authorization, just in case...
    - In North Dakota, a court ruling declared zone transfers without authorization a _criminal act._





# MODULE 0x020 LECTURE 0x0d0 - FIERCE AND CEWL

## Fierce domain scanner Rsnake

Fierce is a DNS reconaissance tool for finding hostnames.


##  First, a little bit about linux navigation:

- The shell is the program that listens to the commands you type in a terminal window
- The user manual can be accessed by executing `man` and then the program you want
- `locate` displays every asbolute pathname for which the user has access permission that contains any of the names of files and/or directories that are provided to it as arguments.
- `which` command searches the path of executable in the system paths set in the $PATH environment variable
- `file` determines the file type. For example, Fierce is a Python script (?)
- It's complicated... the file is a python script whose last line uses `load_entry_point()` to identify a package.
- `locate` can be used to find the actual fierce executable script
- On my arch setup, the full fierce script is in `/usr/lib/python3.10/site-packages/fierce/fierce.py`
- The 'default.txt' wordlist that it uses is `/usr/lib/python3.10/site-packages/fierce/lists/default.txt`, along with other subdomain files of interest (explore these for full credit in Ex020)


## Fierce example

Fierce example: `fierce --domain cise.ufl.edu`

* The above will presumably try to find all the hostnames in the cise.ufl.edu domain
    - The output lists a few name servers
    - It lists the SOA (Start of Authority) domain
    - Zone: failure
    - Wildcard: failure

* Fierce tries to do a zone transfer from each of the name servers we find, but it fails (fortunately for UF)
    - It also looks for a wildcard record (`*.cise.ufl.edu`) but that fails too

* After that, Fierce goes about doing its thing... but what is its thing?
    - The fierce `defaul.txt` file has a list of 1,594 common hostnames
    - If it finds a host, it looks up and down the IP range for nearby (+-5 in the last octet) IPs and does a reverse lookup on them.

* Fierce was able to find 29 entries in the cise.ufl.edu domain

## How could we do better? Introducing CeWL

* We can get more names by using CeWL from Robin Wood (digininja).
    - CeWL scans a website looking for words to be used in a password, account name, etc.
    - Useful for generating __custom word lists for a specific site__.

* `cewl https://www.cise.ufl.edu -d 3 -o -w cise-wordlist`
    - This looks 3 pages deep and grabs all strings 3 characters or longer
    - Adding these to the words in the default list yields a much larger set of unique words (over 70k)
    - Adding these to fierce makes it take longer, but **discovers 127 more hostnames**

* This is also a good way to find undiscovered internal networks

* Burp suite integration
    - CeWL has actually been integrated into Burp Suite (portswigger), a web proxy


## But can we do EVEN better?

* Fierce does a nearby IP lookup of +-5 octets, but why stop there?
    - Well, to make less noise on the network (the quieter you become, the more you can hear)
    - What if we don't care?: `fierce -traverse 255` yields 202 hosts in all, combined with the default wordlist and CeWL
    - Note that these reverse lookups rely on these IPs having PTR records associated with them (for reverse lookups)


## OTHER SCANNERS: Dnsmap

* Does not find as many hosts as fierce, and slower
    - e.g. `dnsmap cise.ufl.edu`

* Keep your mind open to new tools
    - Different tools ==> Obtain the positives from all of them, doesn't limit you to drawbacks of one tool.

END OF MODULE.
