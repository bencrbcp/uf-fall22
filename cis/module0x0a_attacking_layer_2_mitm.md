# MODULE 0x0a0 LECTURE 0x290 - ARP SPOOFING AND ARP CACHE POISONING

## What is ARP again?

The __Address Resolution Protocol__ (layer 2).
Originally defined in IETF RFC 826.

* ARP used to identify the correspondence between an IP address (layer 3) of a host and its associated MAC address (layer 2) to support delivery of IP packets at the datalink layer.
    - ARP is replaced by __Network Discovery Protocol__ in IPv6


## ARP messages

* ARP Request (broadcast)
    - "Whoever has this IP address, please tell me your MAC address."

* ARP Reply
    - "This is my IP address -- here is my MAC address"
    - Can be supplied _gratuitously,_ i.e. even if not requested

* ARP Reverse Request
    - "Whoever has this MAC address, please tell me your IP."

* ARP Reverse Reply
    - "This is my MAC, here is my IP"


## ARP spoofing

When a host sees an ARP reply, it (usually) trusts that it is correct.

There is no way to verify the information contained in the reply.

* You can lie (spoof an ARP reply) in order to get information intended for a machine at a given IP address, sent to a different MAC address.
    - Since MAC addresses are not routed, this spoofing works on the local network

* So when you send such a reply to a machine it will generally trust it and this will poison its ARP cache.
    - So, ARP spoofing is used to achieve ARP cache poisoning


## How to use arpsoof

`arpsoof` is part of the [dsniff package](https://www.monkey.org/~dugsong/dsniff/) by Dug Song.

* To successfully use on a Linux machine, you must have `ip_forwarding` enabled
    - `echo 1 > /proc/sys/net/ipv4/ip_forward`
    - Sidenote: the `/proc/` directory represents system processes as files; allows you to communicate to them

* Simple parameter values:
    - `arpspoof -i [interface] -t [target] -r [host]`
    - The `[host]` is usually the network gateway on the local network subnet, but interesting traffic may be traveling from host to host on the local subnet as well.


## Avoiding ARP cache poisoning

* One can use static ARP entries, but this is difficult for all but the smallest and most static network
    - Inserting every ARP entry individually for every router is a pain

* Instead, use port security measures such as only one MAC address per physical port
    - If a machine changes its MAC address, then it can only have that MAC address
    - This way, you can't have an attacker stack multiple stacking up multiple MACs in an ARP poisoning

* Use an ARP monitoring tool to detect unusual ARP activity such as large numbers of gratuitous messages, etc.
    - In ARP poisoning, gratuitous ARP replies will have to be refreshed, so many may pop up.

* Tools like Xarp (Windows/Linux), ArpOn (Linux), ArpGuard (MacOS), Snort (Windows/Linux)


## Where will this not work?

* Amazon EC2, Amazon VPC, Google Compute Engine, and Microsoft Azure Cloud virtual networks (VNets)
    - These VNets employ techniques like layer-3 overlays to provide point-to-point communication rather than broadcast
    - No broadcast, no layer 2
    - This means that neither VLANs nor ARP are supported.





# MODULE 0x0a0 LECTURE 0x2a0 - SSLStrip, EXPLOITING USER ASSUMPTIONS

## What does sslstrip do?

Sits between browser and HTTP server.

* Converts and caches HTTPS links delivered to the browser and also converts and caches 302 location redirects.
    - When an HTTPS link in the cache is followed, the page is requested from the secure server and delivered via HTTP to the browser.
    - sslstrip effectively downgrades HTTPS to HTTP


## Requirements for best use

Target a webapp that has an HTTPS landing page that then goes to an HTTPS page for login

* Is this common?
    - Yes. Although many large providers provide HTTPS landing pages.
    - In corporate networks, HTTP is more rampant, so potentially more of a use case there.


## Exploiting browser behavior and user expectation

Modern browsers provide:
- Few and subtle indicators when the page being viewed is delivered via HTTPS (e.g. lock icon)

* Significant impediments when the secure page being viewed does not have a trusted certificate
    - Strategy: Downgrade HTTPS to HTTP instead => little to no difference
    - Q: is this true tho?
    - Proxy HTTPS pages to user as HTTP and intercept communications at packet level.


## Steps to use SSLStrip

1. Enable IP forwarding
    - `echo 1 > /proc/sys/net/ipv4/ip_forward`

2. Redirect HTTP traffic to sslstrip:
    - `iptables -t nat -A PREROUTING -p tcp --destination-port 80 -j REDIRECT --to-port <listenPort>`
    - _iptables_ affects the low-level routing of packets by a UNIX host.
    - '-A PREROUTING' indicates that this goes with the prerouting _chain_
    - '--destination-port 80' indicates that the traffic to intercept with sslstrip will be incoming on port 80
    - '-j REDIRECT' indicates that this is a redirect-type rule

3. Run sslstrip
    - `sslstrip.py -l <listenPort>`

4. Arpspoof to get in the middle:
    - `arpspoof -i <interface> -t <targetIP> <gatewayIP>`

5. Ettercap to grab credentials:
    - `ettercap -T -q -i <interface>`
    - If you log properly with sslstrip, this isn't necessarily needed, but can be useful.


## Potential problems with sslstrip

* Content encodings are hard to handle
    - Don't allow them

* Secure cookies will not be sent correctly
    - Kill ones that are either new or have been around for a long time. Users won't notice.

* A page that is already cached won't be rewritten
    - Don't allow caching


## Why/how to use sslstrip in a pentest?

- Allows misdirection without phishing
- Intercept secure logins to corporate/intranet servers
- Be cautious b/c sslstrip will strip all HTTPS comms, even personal ones. PII of individuals should not be compromised.

* It's actually getting harder to find sites that are subject to compromise by sslstrip
    - But that's OK. There are updated versions of this attack





# MODULE 0x0a0 LECTURE 0x2b0 - HTTP STRICT TRANSPORT SECURITY

## What problems does HSTS address?

* Users might navigate to HTTP resources accidentally and get MitM'd by attackers

* Web applications reached by HTTPS links may contain HTTP links

* A MitM attack may attempt to provide an invalid certificate for the user to accept


## How does HSTS stop these?

* HTTP requests are automatically redirected by the browser to identified HTTPS servers.

* HSTS redirects page-embedded HTTP links for identified domains to HTTPS links.

* HSTS prohibits user certificate override for identified domains.


## How does it work?

* The web application must request that the browser use HSTS by providing a Strict-Transport-Security header.
    - `Strict-Transport-Security: max-age=31536000; includeSubDomains`

* If the browser supports HSTS, it stores a __super-cookie which cannot be removed__ through any browser-supported action.
    - This cookie dictates the actions of the browser when accessing the identified domain
    - The maximum lifetime (age) of an HSTS is 31,536,00 seconds -- 1 year.

* If the header lacks the `includeSubDomains` specification, attackers can spoof subdomains


## But does anyone really use it?

According to W3tech surveys, as of March 2020, about 12.5% of websites use HSGTS


## What vulnerabilities can be exploited?

* If you can catch the first connection by a client to an HSTS-enabled site, you can intercept the HSTS header and prevent super cookie registration.

* If the header neglects to include subdomains, you could spoof a connection to `www.f4mc0rp.com` by presenting `wwww.f4mc0rp.com`, for example.

* If you can MitM NTP (Network Time Protocol) and set the date more than one year into the future, then the browser will no longer honor the super cookie.


## Tools

* sslstrip2

* dnsproxy





# MODULE 0x0a0 LECTURE 0x2c0 --SSH: How does it work, really?
