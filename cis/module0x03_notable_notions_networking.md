# MODULE 0x030 LECTURE 0x0e0 - A LITTLE NETWORKING

## OSI 7-layer model

Layer 7: Application layer (HTTP, FTP, SMTP)
Layer 6: Presentation layer (JPEG, GIF, MPEG)
Layer 5: Session layer (AppleTalk, WinSock)
--------------------------------------------------------------- These 3 are referred to as the Application Layer in the TCP/IP model

Layer 4: Transport layer (TCP, UDP, SPX)
--------------------------------------------------------------- This layer remains the same in the TCP/IP 4-layer model

Layer 3: Network layer (IP, ICMP, IPPX; router)
--------------------------------------------------------------- This layer also remains the same in the TCP/IP 4-layer model

Layer 2: Data link layer (Ethernet, ATM; switch, bridge)
Layer 1: Physical layer (Ethernet, Token Ring; switch, bridge)
--------------------------------------------------------------- These first 2 layers are combined as the "Network Interface Layer" in TCP/IP

* Packets _sent_ to network from flow top layer to bottom
    - Service Data Units (SDUs) from the upper layers are encapsulated into Protocol Data Units (PDUs) as the messages go from one layer to the next

* Packets _received_ from network flow from bottom to top.
    - PDUs of received packets get unwrapped as they flow to the top

__MNEMONIC: All People Seem To Need Data Processing__


## What OSI layers do pentesters deal with?

Possibly all...

* Two layers that may be foreign but are of great importance are layer 3 (network) and 4 (transport)
    - In this unit, we will look at layer 3 (IP)
    - We will also briefly look at ICMP and the transport layer 4 (transport) protocols UDP and TCP


## IPv4 and ICMP packet structure

IPv4 is a Connectionless protocol used on packet-switching networks
    - Fixed/deterministic size sequence of bytes.
    - No guarantee of delivery or sequence of packets
    - Source and Destination addresses are 32 bits
    - Time-to-live (TTL) field is decremented upon forwarding to guarantee packets don't travel forever. When it reaches 0, packet is not forwarded.
    - Has a Data field where the Service Data Unit payload of the layer 4 information that it's carrying resides in

ICMP is the Internet Control Message Protocol
    - Embedded in IP with protocol = 1
    - ICMP messages can be used for control or diagnostic purposes or can be generated in response to errors with IP operations.


## UDP and TCP

* Transmission Control Protocol (TCP) is a communication protocol that provides _reliable, ordered_ streams of octets between programs
    - TCP communicates streams as ordered sequences of packets
    - The packets themselves are __not__ guaranteed to be received in the same order at which they were sent, but it can _reconstruct them_ in order.
    - e.g. File transmission

* User Datagram Protocol (UPD) is a communication protocol that provides communication of datagrams, or packets, of octets
    - UDP is useful for real time communication where speed of transmissions is more important than reliability or order of receipt
    - e.g. online FPS, video stream





# MODULE 0x030 LECTURE 0x0f0 - TCP in More Detail

## TCP Source Port
    - Ports are conceptual locations (really just numbers)
    - These source ports are associated with a program on the machine sending a packet

## TCP Destination Port
    - Assoicated with the program that's being contacted on the machine that's receiving the packet

## TCP Sequence Number
    - Identifies which packet this is in the sender's TCP stream
    - Packets may arrive out-of-order and must be reassembled in order

## TCP Acknowledgement Number
    - The sequence number of the next packet expected to be received by the sender
    - It asserts that all packets with lower sequence numbers have already been received

## TCP Data Offset
    - The data offset is the size of the TCP header in 32 bit words (also the offset where packet data begins)
    - It inherently also tells us how many options are being specified
    - Minimum value: 5
    - Maximum value: 5

## TCP Flags (9 bits)
    - Identify any control information communicated by a packet
    - Can be really important (not so much to us)
NS: Nonce sum
    - Explicit Congestion Notification (ECN) protection
CWR: Congestion Window Reduced
ECE: ECN Congestion
    - with SYN: ECN Capable
    - wihout SYB: Congestion Experienced
URG: Urgent pointer field
    - Significant to us
ACK: Acknowledgement field
    - Significant to us
PSH: Push buffered data to the application
RST: Reset the connection
    - Important in looking at behavior of services
SYN: Synchronize Sequence Numbers
FIN: No more data from sender

## TCP Checksum
    - Checksum for verifying header and data
    - Set to zero to calculate this field.
    - Tells us whether the packet has the "correct" values, presumably


## Connection establishment

To establish a connection on a TCP port, a 3-way handshake is used:

1. Client sends a packet with FLAGS==SYN message to the server w/ sequence number A (chosen randomly)
2. Server replies with a packet w/ FLAGS==SYN|ACK and acknowledgement number = A+1 and sequence number B (chosen randomly)
3. Client sends a packet with w/ FLAGS==ACK and with sequence number A+1 and acknowledgement number = B+1


## Connection termination

This time, a 4-way handshake:

1. Machine X sends packet with FLAGS==FIN
2. Machine Y sends packet with FLAGS==ACK
3. Machine Y sends packet with FLAGS==FIN

* Packets 2 and 3 and be combined (FIN|ACK) for a 3-way disconnect
* If one side has sent FIN but the other has not, then it is called a half-open connection.

# MODULE 0x030 LECTURE 0x100 - UCP, ICMP, Traceroute and Wireshark

## UDP Packet Structure

* Like TCP packets, UDP have source and destination port numbers
    - Aside from this, they contain length, checksum, and application data
    - UDP is used most for DNS queries -- specifically for ones that require no more than 512 bytes of data (even though UDP can hold up 65,507 bytes per packet)


## ICMP Packet Structure

* IP Packet Payload
    - Message type: 8 bits
    - Code (subtype of message): 8 bits
    - Checksum (of header + data excluding checksum): 16 bits
    - Other header data: 32 bits
    - Data: rest of packet

* These packets are sometimes used for covert communication

* Common ICMP packet types:
    - 0: Echo reply (response to a ping)
    - 3: Destination unreachable
    - 5: Redirect (use another route to your destination)
    - 8: Echo request (ping)
    - 11: Time-To-Live exceeded (TTL became 0)


## Traceroute: what is it?

* Traceroute tries to find all hosts on the network between your local host and some desintation host
    - Does this by sending packets with TTL increasing from 1 to 30
    - For each different TTL, 3 packets are sent
    - As each packet expires, it should receive an ICMP Time Exceeded (11) reply from the host at which it arrived
    - Traceroute ways 5 seconds for each expiration (usually enough time to receive a message on the internet)
    - If 5 seconds are exceeded, an asterisk is printed
    - Otherwise, the TTL value is printed at a given host address, as well as the round-trip time of each probe


## Wireshark

Packet sniffing and analysis tools

* Extremely useful
    - Select an interface on which to sniff (must be root to do packet captures)

* Demo
    - NATed kali box receives back asterisks from traceroute
    - Packets that go outside the NAT are not being returned



## QUIZ ACCESS CODE
Moby - Extreme Ways





# MODULE 0x030 LECTURE 0x105 - NAT: WAT?

## What is a private IP anyway?

Three kinds of internet hosts within a network:

1. Hosts that only need access to local resources as a client, provides no services
2. Hosts that need access to a small range of outside services and provide few or no services
3. Hosts that need unfettered access to outside services and provide services to other internet hosts

* Many hosts in categories 1 and 2.
    - Private IP addresses exist for them, to not exhaust public IPv4 spaces


## RFC 1918 Address allocation for private internets

The following address ranges are provided for private networks:

- 10.0.0.0/8 (10.0.0.0 - 10.255.255.255): for big networks, e.g. UF
- 172.16.0.0/12 (172.16.0.0-172.31.255.255): VMs often use these
- 192.168.0.0/16 (192.168.0.0 - 192.168.255.255): Common in home networks


## NAT Demo

* Useful to watch, although a bit convoluted
    - Important point: NAT rewrites the SRC field from packets as it is tranlsating addresses between routers


## What is port forwarding?

* Port forwarding allows a router with a public IP to forward communications to a NATed machine's private IP
    - This allows machines on the WAN (Internet) side of your router to communicate with you (usually a bad idea -- only if you need to)
    - Minecraft multiplayer

* Requirements
    - Destination IP (the public IP)
    - Destination Port
    - Target IP (usually a local IP) where the packets will finally end up
    - A service actually needs to be running

## How to configure a pfSense router to do this

This example uses outerouter as the router with pfSense
    - pfSense is an open source router software built on FreeBSD, often used in VM environments
    - Uses a web configurator as its primary admin tool

* Initial state:
    - nc is running on Kali, but Mac cannot make contact
    - Kali machine has outerouter web config running on 174.24.0.1
