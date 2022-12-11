# MODULE 0x0f LECTURE 0x380 - WIRELESS HACKING

## Wireless

The IEEE 802.11 family of standards defines how wireless LANs are implemented.

* 802.11 b/g work in the range of 2.4-2.483 GHz
    - This bandwidth range is broken up into 14 overlapping channels (1-14)
    - US channels 1-11 are supported (most commonly used non-overlapping channels 1, 6, 11)
    - Use of channels above 11 is prohibited in the US


## SSID

* SSID: Service Set Identifier (network name)
    - BSSID (Bassic SSID): MAC address of the Access Point (AP)
    - ESSID (Extended SSID): name for one or more APs providing the same service to get to a wired LAN


## 802.11 Handshake

Handshake occurrs between a _station_ and an _access point_.

1. Station sends a Probe Request to an AP to connect to its associated SSID
2. AP gives a Probe Response with its SSID
3. Station then gives an Auth Request
4. AP gives an Auth Challenge
5. Station gives an Auth Response (based on the particular standard that is being used)
6. AP gives an Auth Success
7. Station sends an Associate Request
8. AP gives an Associate Response
9. Finally, data can be exchanged between station and AP


## Wireless encryption

Open

WEP (Wired Equivalent Privacy)
    - Uses RC4 stream cipher and CRC32 for integrity check

WPA (Wifi Protected Access)
    - Uses Temporal Key Integrity Protocol (TKIP)
    - Message Integrity Check (Michael) subject to key reinstallation and spoofing

WPA2
    - Uses Advanced Encryption Standard (AES) encryption


## WEP Authentication


1. Station sends Auth Request
2. AP gives Auth Challenge (cleartext)
3. Auth Response (RC4[key, cleartext])
4. Auth Success

Can use Open System Authentication (common)in which no authentication occurs, the Station just encrypts packets using the key.
Shared key authentication can also be used.


## ARP and FMS attack

* ARP (Address Resolution Protocol)
    - Destination is always FF:FF:FF:FF:FF:FF and size is 86 or 68 bytes

* Fluhrer, Martin, and Shamir (FMS) devised an early attack against WEP encryption based on the use of ARP requests (2001).
    - This was followed by KoreK (2004), PTW (2007), Chopchop (2007), VX (2007), and Beck and Tews (2008)


## FMS Web Cracking Deals

Each WEP packet is separately encrypted (rather than stream encrypted) because of the likelihood a single packet might be dropped

* The WEP key is a string of 10 hex digits (4 bits each)
    - WEP uses this 40-bit key and dynamically generated 24-bit initialization vector (IV) to encrypt traffic.
    - IVs are transmitted in cleartext
    - RC4 is a stream cipher, so if a known message is sent many times with many different IVs, the stream cipher can be deciphered.

* So, WEP is cracked by sniffing an ARP packet, then replaying it over and over again to generate many encrypted replies with IVs


## How does WPA encryption work?

The Access Point and client Station share a secret Pairwise Master Key (PMK)

* The AP and station use a 4-way handshake to establish a Pairwise Transient Key (PTK) for session encryption, and Group Temporal Key (GTK) for broadcast and multicast traffic.
    - If the 4-way handshake can be captured, then a dictionary attack can be mounted to verify the PMK.
    - If a connection is dropped, it can be re-established by repeating the handshake.


## WPA Authentication

1. Access point sends Anonce to Station
2. Station generates PTK
    - PTK = PBKDF2-SHA1(PMK,Anonce,Snonce,AP,MAC,STA MAC)
    - MIC = Message Integrity Code (includes transmitted key)
3. Station sends Snonce, MIC(PTK)
4. AP generates GTK
5. AP sends MIC(GTK)
6. Station sends ACK


## How does WPA encryption work? continued

First WPA implementation used TKIP: Temporal Key Integrity Protocol which is based on RC4.

* [Tews & Beck (2008)](https://dl.aircrack-ng.org/breakingwepandwpa.pdf)
    - Active attack allows decryption of single short packets on WPA/TKIP networks (inital attack required support for QoS feature)


## How is WPA2 usually cracked?

WPA2 uses the CTR modewith CBC-Mac Protocol (CCMP) aka AES and AES-CCMP

* Dictionary attack is mounted on Pairwise Master Key (PMK)

Resources for dictionary attacks:
- [onlinehaskcrack.com](https://www.onlinehashcrack.com) -- pay with BTC if password is longer than 8 characters
- Wireless AP-specific wordlist (often include regionally meaningful sports teams and phrases)


## WPA3

Released in 2018 as a more secure alternative to WPA2

* Not yet in wide-scale deployment
    - Wigle shows 0% deployment

* WPA3 is mandatory in 2020 for all new certified devices

* It uses _Simultaneous Authentication of Equals_ handshake, also known as _Dragonfly_ -- described in IEEE 802.11-2016

* Devices must support backward compatibility with WPA2 using a __transitional mode of operation__.
    - Paper on dictionary attack when in this mode can be found [here](https://papers.mathyvanhoef.com/dragonblood.pdf)


## How do I get Kali to use my host's wireless connection?

You don't usually use the host wireless connection, though it is possible with some machines

* Many laptop wireless adapters do not support __monitor mode__.
    - Recommended: ALFA AWUS051NH 500mW 802.11 a/b/g/n





# MODULE 0x0f LECTURE 0x390 - WPA2EAP


## EAP: EXTENSIBLE AUTHENTICATION PROTOCOL

EAP is a framework for authentication with a variety of implementation types

**TLS**
- Original 2008 EAP standard. Still secure if warnings about false credentials are heeded.

**PEAP**
- Encapsulates EAP in a TLS tunnel.

**TTLS**
- Server is authenticated by its CA-signed PKI certificate. Username is never transmitted in cleartext

**EAP/FAST**
- Assigned Protected Access Credential (PAC) to each user. Difficult to do without running in secure mode.


## RADIUS: What is it, really?

RADIUS = Remote Authentication Dial In User Service (Livingston enterprises)

* A AAA Management Protocol:
    - Authentication
    - Authorization
    - Accounting

**Authentication**
- Historically uses locally stored flat file, but modern servers can use external services (Kerberos, LDAP, AD, etc.)
- These external services provide the authentcation to the RADIUS server, which then uses RADIUS API to provide that authentication to the service that's requesting authentication services


**Authorization**
- 3 responses (Reject, Challenge, Accept)
- Other attributes (either stored, or connection parameters)

**Accounting**
- Accounting Start, Interim Update, and Accounting Stop records.
- Usually intended for billing purposes.


## Attacking EAP RADIUS

3 parts of the communication: the Station, the Access Point, and the RADIUS Server

1. AP sends Identity Request to station.
2. Station sends Identity Response to AP (username)
3. AP sends Identity Response to RADIUS Server: RADIUS Access Request (EPA-Response/Identity)
4. RADIUS Server generates a challenge based on the username.

*Now, in a TLS tunnel*:
5. RADIUS sends a RADIUS Access Challenge(EAP-Request MD5) to the AP.
6. AP forwards this EAP-Request MD5 challenge to Station.
7. Station sends EAP-Response MD5 challenge to AP.
8. Station sends this challenge to RADIUS Server: RADIUS Access Request (EAP-Response/MD5 Challenge)
9. RADIUS sends this back to AP, AP sends EAP success to Station.
10. RADIUS server forwards the PMK advertisement to client through the AP
11. Station acknowledges PMK by sending this thru AP to RADIUS server.

Back outside of the TLS tunnel:
12. RADIUS sends a PMK notification to AP.
11. AP sends four-way handshake (computes PTK)

Back inside the TLS tunnel:
12. Encrypted communication ensues, with AP acting as middleman between Station and RADIUS server.

## Attacking EAP: Assumptions
Network SSIDis easily spoofed, thus unreliable.

* TLS gives us a way to validate the AP
    - If the Certificate Authority (CA) from the AP is validated, the client will pass information onto the RADIUS authentication server.
    - Web client cannot check that the certificate was assigned to the right host (it doesn't know the RADIUS server's Fully Qualified Domain Name -- FQDN)

* Authentication traffic is protected by a TLS tunnel

## Attacking EAP: Actual practice
Some deployments of EAP will disable validation and trust any Access Point.

* Attacker then sets up a fake AP mirroring the actual SSID, encryption type, and band
    - then, configures AP to accept Enterprise auth.

* Fake AP connects to a __hostpad-wpe__ (host access point daemon software for Linux, wireless pwnage edition) supplied RADIUS server that captures and records all auth requests.
    - This includes usernames and hashed passwords of the EAP traffic.

* Finally, Attacker waits for clients to attach (or deauths clients) and captures their credentials
    - Can then crack passwords offline

### Attacking EAP: It gets better
* Some wireless clients will attempt multiple EAP types during authentication
    - Trick clients into authenticating to the fake AP with an insecure EPA type (MD5).

* Deauth floods can be used to keep clients from connecting to a legitimate AP.


## Securing wireless networks

- Do not use TKIP or WEP
- Use WPA/EAP PEAP, TTLS, or TLS for authentication
- Have clients check the AP certificate.
- Avoid using pre-shared keys, but if you must, choose a complex password > 16 chars
- Always keep firmware and clients patched to latest revisions
- Do not use hidden APs (hidden SSIDs)
- Disable insecure EAP types in your clients
- Firewall the wireless network from the rest of the internal network


## Wireless IDS approaches

Detect:
- Deauth attacks
- DoS attacks
- Rogue APs

* IDS = detection, not prevention.
    - IPS may inadvertently attack neighboring wireless systems
    - IDS will not protect your users when they travel (UF DEFCON story)


## Shewhart/Deming cycle

Plan-Do-Check-Act

* Assessment must be performed on a regular basis
    - Third party (or at least, independent red-team) assessments are helpful.
    - Assessments should contain architecture and client configuration reviews





# MODULE 0x0f LECTURE 0x3a0 - SCAPY AND WIRELESS PACKETS

## What is Scapy, really?

Python program that imports Scapy module, then runs an interactive python interpreter session.

* Scapy provides functions that help you construct, send, receive, and analyze network packets.
    - Often used to generate or inspect packets
    - Pentesters use scapy to interact with network devices at a lower level not supported by most application programs


## Scapy supports Python, but you don't really have to be a Python guru to use it

- `ls()` shows protocol layers supported by scapy (over 300). Each one is implemented by a Python class.
- `ls(protocol_name)` shows the attributes (fields) of the specified protocol. Try `ls(Ether)` and `ls(TCP)` for examples.
- `lsc()` lists scapy commands (Python functions provided by scapy)


## Scapy packet construction

Each layer (shown in the `ls()` result) provides a constructor that contructs that layer of a packet

* To construct an entire packet, you can use the / operator to add a payload to the previously constructed packet layer

Examples:
- `pkt = IP(dst="192.168.0.1-254")/ICMP()`
- `pkt = IP(dst="192.168.0.*")/TCP(dport=80, flags="S")`
- `pkt = IP(dst="192.168.*.1-10")/UDP(dport=0)`

How could you use each of those packets in scanning a host?


## Sending/receiving packets with scapy

Python supports tuple return value. Scapy uses this in its send/receive sr function, which separately identifies the answered and unanswered packets

So, sr sends and receives packets, and returns the answered and unanswered packets.
`ans,unans = sr(IP(dst="192.168.0.1-254")/ICMP(), timeout=1)`
    - `ans[0][0]` is the initial answered packet
    - `ans[0][1]` is the answer to the initial packet
    - `unans` is a list of those packets for which there was no reply before the timeout occurred.

- Arguments: `sr(pkts, filter=None, iface=None, timeout=2, inter=0, verbose=None, chainCC=0, retry=0, multi=0)`


## Displaying packets in scapy

If the variable `pkt` contains a scapy layer, then you can view it in a variety of ways.

- `pkt` yields a short summary that shows any non-default attribute values (not a String value).
- `pkt.summary` yields a String value that gives a one-line summary of the packet.
- `pkt.show()` prints a hierarchical view of the packet with lower layers indented.
- `ls(pkt)` prints each field name of the packet together with its type and value


## Python supports lambda

`lambda` creates a function at runtime.

Example:
```
>>> lambda x,y: x + y
<function <lambda>> at 0x2094c80>
>>> f = lambda x,y: x + y
>>> f(1,2)
3
```

* More characteristically, lambda is used to create anonymous functions that are immediately used, but never stored
    - Lisp is based on lambda calculus


## Python supports map and join

* `map` applies a function to each element of a list, yielding another list of the same size.
    - Example: `unans_summary = map(lambda x: x.summary(), unans)`

* `join` is a string method that takes a list of strings as an argument and inserts the operand (receiver) string between each in the list.
    - Example: `','.join(['a','bcd','e']` returns `'a,bcd,e'`


## Using join, map, and lambda together

```
>>> ans,unans = sr(IP(dst="192.168.0.1-20") / ICMP(), timeout=1)
>>> print('\n'.join(map(lambda x:x[1].summary(),ans)))
IP / ICMP 192.168.0.1 > 192.168.0.9 echo-reply 0 / Padding
IP / ICMP 192.168.0.4 > 192.168.0.9 echo-reply 0 / Padding
IP / ICMP 192.168.0.3 > 192.168.0.9 echo-reply 0 / Padding
```
The above is the summary of the replies to the answered packets


## Sniffing packets with scapy

Starting a wireless adapter interface in Kali (see https://www.aircrack-ng.org/doku.php?id=airmon-ng):
- `airmong-ng check kill`
- `airmong-ng start wlan0`
- `ip a`

* Sniff can be used to capture a list of packets from an interface.

Example: `pkts = sniff(iface='wlan0mon', count=500)`

sniff takes other arguments, check documentation.


## Identifying probe request SSIDs

The scapy functions below will extract SSIDs from packets

- If applied to Beacons, this identifies available access points.
- If applied to Probe Requests, this identifies access points that are being sought.

```
def get_ssid(pkt):
    if isinstance(pkt.payload,Dot11Beacon) or isinstance(pkt.payload,Dot11ProbeReq):
        return pkt.payload[1].info

def get_ssids(pkt):
    dot11s = map(lambda x:x.payload, pkts)
    payloadpkts = [x for x in dot11s if not isinstance(x.payload,NoPayload)) and not isinstance(x.payload.payload,NoPayload)]
    return set(map(get_ssid,payloadpkts))
```


## Evil twin attack (Karma)

One can perform the evil twin attack as follows:
1. Identify client to attack, note its SSID
2. Act as access point and advertise that SSID
3. Increase power (set adapter country to Bolivia to allow power of up to 1W)
4. Deauthorize client
5. Client will now connect to your AP


## Karma

* Karma takes the evil twin attack one step further.
    - It uses the madwifi driver to allow it to respond to any probe with the requested SSID.
    - Comes installed on the Hak5 WiFi Pineapple


## Wifi Pineapple from Hak5

* Standalone attack device, easy to install.
    - Basically MitMs a real wifi AP and to its own fake one


## I HUNT PINEAPPLES: Defcon22 incident

* Wesley McGrew
    - Found 0-day in Wifi pineapples
    - Messed up skiddie's firmware.


## What about quiet clients

* Many devices will send probe requests for access points to which the device has connected.
    - Because of the Karma exploit, some devices now avoid sending such probes until a beacon is heard.

* Is there a way to overcome that security measure?


## Wigle.net list of top SSIDs

 * Useful list
    - [https://gist.github.com/jgamblin/da795e571fb5f91f9e86a27f2c2f626f](https://gist.github.com/jgamblin/da795e571fb5f91f9e86a27f2c2f626f)


## How can we use that info?

Scapy code to transmit beacons

```
def send_beacon(ssid):
    sendp(Dot11(addr1="ff:ff:ff:ff:ff:ff",addr2=RandMAC(),addr3=RandMAC())/
        Dot11Beacon(cap="ESS")/
        Dot11Elt(ID="SSID",info=ssid)/
        Dot11Elt(ID="Rates",info='\x82\x84\x0b\x16')/
        Dot11Elt(ID="DSset",info='\x03')
        Dot11Elt(ID="TIM",info='\x00\x01\x00\x00'),iface="mon0")()

f = open('top-ssids')
ssid_list = f.readlines()
ssid_list = ssid_list.rstrip()
map(send_beacon, ssid_list)
```

With this, we can send beacon messages to each of the top 10,000 SSIDS, i.e. all of those SSIDs are being advertised as if the AP for that SSID is availabe.


## QUIZ ACCESS CODE
Ambrosia - Nice, Nice, Very Nice


## Scapy SSID beacon demo

- Plug in wireless adapter with monitor mode

- `sudo airmon-ng check kill`

- `sudo airmon-ng start wlan0`

- `sudo airdump-ng wlan0mon`

- `sudo ./scapydemo.py 1000`
    - `ChinaData` gets sniffed
    - This means somebody in the past connected to an AP called ChinaData, and we were able to advertise that SSID to them (evil twin)
    - Can be malicious with this if we wanted to





# MODULE 0x0f LECTURE 0x3b0 - ELECTRONIC COMMUNICATIONS PRIVACY ACT of 1986 U.S.C CHAPTER 119

## ECPA definitions

- wire communication
    - aural transfer
    - by the aid of wire
    - operated by any person
    - providing
    - interstate or foreign COMMUNICATIONS
    - affecting interstate or foreign commerce

- oral communication
    - not subject to interception
    - does not include electronic communication

- electronic communication
    - does not include any wire or oral communication
    - does not include any communication made thru a tone-only paging device
    - does not include any communication from a tracking device
    - does not include any electronic funds transfer info
    - this prob includes wifi


## ECPA section 2510 (readily accessible)

is NOT:
- scrambled or encrypted communication
- things transmitted using modulation techniques whose essential parameters have been withheld from public
- carried on a subcarrier
- transmitted over a comm system provided by a common carrier
- satellite communications


## ECPA section 2510 (protected computer)

a computer:
A) exclusively used for a financial institution of US gov
B) used in or affecting interstate or foreign commerce or communication

almost every computer is a protected computer (even iot devices bc a lot of the times they phone home)

## ECPA section 2510 (computer trespasser)

A) someone who accesses a protected computer without authorization
B) does not include a person known by the owner or operator w/ existing contractual relationship


## ECPA section 2511 (Interception and disclosure of wire, oral, or electronic communications prohibited)

except as otherwise specifically provided in this chapter, any person who:
A) intentionally intercepts any electronic communication + wire + oral
B) intentionally dicloses electronic comm. knowing information was obtained thru interception


## ECPA section 2511 (punishment)

A) first offense: federal gov entitled to appropriate injuctive relief
B) second or subsequent: 500$ fine

Exceptions:

- not unlawful to intercept or access electronic comm that is readily available
- if intercepted comm is causing harmful interference
- not unlawful for other users of same frequency if such comm is not scrambled or encrypted

Moral of the story: Wilson's packet captures were part of exceptions.





# MODULE 0x0f LECTURE 0x3c0 - CELLULAR TELEPHONE NETWORK SECURITY

## CELLULAR TELEPHONY

Several radio communication standards

* GSM (Global System for Mobile communications)
    - Worldwide (mostly -- not Japan) standard.
    - Uses Services Identity Module (SIM) card.
    - Being phased out, but Vodaphone promises to support 2G GSM until 2025

* CDMA (Code Division Multiple Access)
    - Primarily in US and Canada. Developed by Qualcomm.
    - Now supported on many phones.
    - Retired by Verizon as of Jan 1 2020

* LTE (Long Term Evolution)
    - The only standard going forward.
    - Voice over LTE is used for voice calls.
    - Current 4G, soon 5G


## What if my service is bad?

Some providers will let you use a microcell or femtocell: a small, low-powered, base station.

* The microcell connects to your high-speed (hopefully) internet connection

* Communicates using your carrier's radio standard to cellular telephones nearby.

* It transmits communications by wrapping them in IP and sending them to server maintained by carrier.

* This may become more (less?) of an issue with 5G due to limited range (<=500 meters)


## Femtocells hacked

Both the AT&T cisco and Verizon Samsung ones.

* ISEC partners demonstrated a hacked Samsung SCS-26UC4 and SCS-2U01.

* LMG Security used this ability to create an IDS for cell phones that runs on the femtocell.
    - Normally, it's difficult to detect intrusions that only exhibit behavior on cell network (how do we sniff?)

* fail0verflow has demonstrated a CISCO microcellhack as well


## ISEC Femtocell connectivity diagram

IPSec tunnel between femtocell and carrier internal network

See powerpoint for more info


## Samsung LMG's exploit approach

* Dec. 2012: Richard Allen found that the exposed HDMI port on the SCS-26UC4 is, in fact, a serial port.
    - A serial cable connected at 57600:8N1 can communicate to console.

* The machine is a linux box running the U-boot bootloader

* The U-boot interrupt key has been modified, but it can be determined thru experimentation (AKA swiping hand across keyboard)
    - LMG interrupted boot and added init=/bin/sh to the boot parameters, yielding a root command prompt

* LMG imaged the filesystem both with dcfldd (dumping contents of the device) and with tar (to dump file and directory structure)

* An FTP client was already installed on the Montevista Linux distro, allowing them to exfiltrate data.

* The system uses an ARM926EJ processor, so Android binaries can operate on it.
    - LMG copied binaries for nc and dcfldd onto the device.

* Upon connecting the device to the internet, it downloaded new software that set the Uboot wait time to 0 (disabling the ability to interrupt)
    - They had a :-( moment

* LMG purchased additional Samsung SCS-26UC4 femtocells (returning those w/ new firmware) until they found one that could boot properly)
    - Identified and implemented method to keep system from updating
    - Modifiedthe kernel to support expanded iptables functionality that was necessary to export traffic required to implement intrusion detection

* Check out the DEFCON talk


## Cisco Fail0verflow's exploit approach

Fail0verflow identified a Ralink System-On-Chip (SoC) and found its serial communication port (header JPI) which operates at 57600 baud.

* The machine boots with U-boot (again)

* They issued the U-boot md command to dump flash memory over the serial port

* The memory contents were an Lmza compressed 2.6.21 linux Kernel image (normal)

* System used busybox 1.8.2 to provide web service.

* There were two users: root and ssh with the same password (non-dictionary).
    - These were guessed after 5 days of processing the kernel image


## Cisco wizard

* Service is accessible over the WAN interface of the CISCO femtocell

* Supports a function named BackdoorPacketCmdLine that accepts a Linux command as its argument
    - Provides full, unauthenticated, remote backdoor execution
    - Communication over UDP

* Responses were sent to 234.2.2.7, a multicast address


## Law enforcement use: Stingray

Each cell phone has an International Mobile Subscriber Identity (IMSI) chip.

* IMSI catching technology is used by law enforcement agencies
    - FBI contended that no warrant was needed for these
    - Argument is based on the ECPA law


## Roll your own IMSI catcher

[https://openbts.org](https://openbts.org)

* Software-defined radio (SDR) hardware now readily available.

* LTE Base Station software now available.


## What about commercial devices to hack cell calls?

Alibaba IMSI catchers


## Countermeasures - Android

Use _Signal_

* There is Android IMSI catcher project

IMSI catcher detectors are subject to countermeasures

* Not much info for iPhone IMSI catchers


## But LTE is going to fix this, right?

* As with other standards, rogue base stations is employed

* The LTE standard provides a method of ending Tracking Area Update request, which causes the phone equipment to reset to an unencrypted state
    - Re-establishing a connection involves communicating the IMSI to the base station

* Talk by domi (insert link)
    - Domi's prototype software does not MitM, but is feasible to extend to do so.


## Other LTE attacks

* DoS attack using release message

* aLTEr-attack.net


## Why bother hacking cell phone hardware? SS7 is great!

[https://www.youtube.com/watch?v=-wu_pO5Z7Pk](https://www.youtube.com/watch?v=-wu_pO5Z7Pk)


## SS7 is being actively exploited

[https://www.theregister.com/2017/05/03/hackers_fire_up_ss7_flaw/](https://www.theregister.com/2017/05/03/hackers_fire_up_ss7_flaw/)


## Handset attacks SIM clone/swap

* SIM cloning

* SIM swapping

Why engage in these attacks? To leverage a second authentication factor


## Handset legacy interfaces

* AT commands
    - Used to communicate to modems
    - Saw that were used with cellphones
    - Touch injection, leaked sensitive data

END MODULE 0x0f
