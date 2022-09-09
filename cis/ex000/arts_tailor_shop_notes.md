# NOTES ON ART'S TAILOR SHOP PENTEST EXERCISE:


# Scope of Work

- Why does the customer wants a pentest?
- When does the customer want the active portion of the pen test to be conducted?
- How many internal/external IPs are being affected?
- Are there any devices in place that may impact the results of a pentest such as IDS, firewall, webapp firewall, load balancer?

If the system is penetrated, should we:
- Perform a local vuln assessment on the compromised machine?
- Attempt to gain the highest privilege (root/Administrator/SYSTEM)?
- Perform no, minimal, dictionary, or exhaustive attacks against local password hashes? (e.g. `/etc/shadow`)

Also know:
- How many web apps are being assessed?
- How many login systems are being assessed?
- How many static pages are being assessed?
- How many dynamic pages are being assessed?
- Will the source code be readily available for viewing?
- Will there be any kind of documentation?
- Will we perform static analysis?
- Does the client want fuzzing?
- Black box/grey box/ white box test?


For wireless network tests:
- How many wireless networks are in place?
- Is a guest wireless network used? If so, does it require authentication?
- What type of encryption?
- What is the square footage?
- Will we be enumerating rogue devices?
- Will we be assessing wireless attacks against clients?
- Approximately how many clients will be on the network?

Social engineering:
- Are personnell emails provided?
- Will be be provided phone numbers?
- Will we be attempting to social engineer physical access, and if so, how many people targeted?





# Rules of Engagement

## Verifying IP Ranges & Third Party Providers

* Don't test the wrong machines in a pentest...
    - Ask clients to be specific, and let them know your purpose
* Third parties
    - Need their permission as well
    - Some have more lax pentest agreements than others
    - Third parties include: Cloud services, Web hosting, Managed Security Service Providers (MSSPs)


## Social Engineering Pretexts

* Some companies may be sensitive about what ways of fooling their employees are appropriate.
    - Explain current popular phishing exploits
    - Get explicit approval for any pretexts used in phishing tests.



* Identify how and how often information will be conveyed during the state of the penetration test
* Provide contact info about everyone involved in the test including emergency contact
    - Name
    - Title
    - Emergency 24/7 contact info
    - Secure bulk data transfer method (SFTP or encrypted mail)
    - Agree on incident reporting process
    - Share encryption keys (not optional)


## Test customer's detection abilities

Identify customer's abiltities to detect and respond to:
- info-gathering
- footprinting
- scanning & vulnerability analysis infiltration (attacks)
- data aggregation
- data exfiltration


## Rules of Engagement

* Timeline for test
    - Start anytime, finish around December, before Finals week
    - Don't test during UFSIT meetings
* Location (if on-site)
    - NOT on-site
* Methods of disclosure of sensitive info
    - May be protected by HIPAA
    - Avoid collecting PII
* Evidence handling (use encryption)
* Regular status meetings
* Time of day to test
* Dealing with system shunning
* Permission to test
* Legal considerations


# LIVE LECTURE NOTES

* Shop Owner: Art Rosenbaum
    - Contact info: 555-555-1414
* Sysadmin: Otto Oppenheimer
    - Contact info: 555-555-1313
* Pentest Company Owner: Big Boss
    -
* Pentest Team Lead: Hank
    - 555-555-1212
* Bookkeeper: Debbie


* Type of pentest
    - Blackbox (no credentials provided, just tossed into the network)

* Externally available IPs on assets owned by Art's Tailor Shoppe:
    - www.artstailor.com (217.70.184.38)
    - innerouter.artstailor.com (217.70.184.3)
    - THESE ARE IN SCOPE

* The above hosts are routed to the outer network (internet-facing) by Art's ISP, ISPRUS, via outerouter.artstailor.com (217.70.184.1)
    - THIS IS OUT OF SCOPE

* Domain:
    - artstailor.com

* Web Components
    - Functionality is not there yet, just a static page for now
    - Not testing any webapp, but rather the security of the web server


* They have a wireless network
    - WPA2 PEAP?

* Pentest at any time but not on UFSIT meetings?

* Firewall is in place

* DMZ in place
    - There is a protected network

* They have a router
    - Anything within that router is part of the network

* 7 or 8 people using the network

* Instability is fine... just don't break the machine somehow

* If you do PASSWORD CRACKING (hint...), don't reveal passwords

*
