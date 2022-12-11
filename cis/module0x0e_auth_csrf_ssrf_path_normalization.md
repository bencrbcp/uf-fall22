# MODULE 0x0e LECTURE 0x345 - AUTHENTICATION AND HTTPS

## Basic authentication

When an authorization-required webpage is reached in response to a GET or POST request.
1. An HTTP 401 error is sent (Authentication required)
2. Browser receives 401 and error and pops up an auth box (asking for user/password)
3. Browser sends username:password base64-encoded as the auth header
`Authorization: Basic QmFieVlvZGE6VG9vb0N1dGUK`

* Identifying base64 encoded strings is a useful skill as a pentester.


## Arranging basic auch with Apache server

`htpasswd` command - Creates encrypted flat files containing usernames and associated passwords for a realm

`htaccess` file - Specifies type of auth required, auth group, users, and limits of authentication

### HTTP auth sniffing demo


## HTTP Digest authentication

Digest authentication (uses htdigest command)

Server sends sends 401 with auth _realm_ (made up by server), _qop_ (quality of protection), and _nonce_ (number used once)

Client prompts for username and password and sends response including
- Username
- Cnonce (client nonce)
- Response = MD5(HA1:nonce:HA2)
    - HA1 = MD5(username:realm:password)
    - or, if algo is MD5-sess, then HA1 = (MD5(MD5(username:realm:password):nonce:cnonce)
    - HA 2 = MD5(method:digestURI)
    - or, if qop = auth-int, then HA2 = MD5(method:digest:URI:MD5(entityBody))

* This is susceptile to MitM!
    - MitM tells client "Use Basic Auth" and communicates to server using digest auth
    - There is _no way_ to verify server's identity


## HTTP Integrated Windows authentication

* No password provided by user
    - Client user's Windows user info is supplied by browser to the server via cryptographic exchange
    - Often used in intranets for company-facing web browsers because it will not function over a (known) proxy.
    - Subject to MitM capture and dictionary attack.

## HTTP Form-based authentication

By far the most common authentication used on the internet

* Requires client to provide username/password info via http form fields
    - These are exchanged with a web-based application on the server that determines if access should be allowed
    - Not inherently encrypted

* One must use https for this to be secure


## HTTPS sessions are encrypted

The HTTPS protocol uses Transport Layer Security (TLS) to encrypt an entire HTTP session.

* TLS requires a handshake to verify that the server and client can decrypt data that is encrypted using public keys.
    - Then, a master key is generated from which both the client and the server create symmetric session keys.

* If one does not have access to the server private key used to encrypt TLS negotiation, then there is no hope of decoding the packets
    - BUT, if the server private key is known, then the entire TLS session can be decoded


## HTTP decoding scenario

A pcap file including packets from an HTTPS session can be found [here](https://www.cise.ufl.edu/~jnw/Resources/https-stream.pcap)

The information we know:
- The web server providing data on port 443 was 192.168.0.11
- We want to see what the contents of the text files transferred via HTTPS using SSLv3

Set Wiresharrk filter to
`ip.dst == 192.168.0.11 and tcp.port == 443 and tls.record.version=0x0300`

Right click on the packet > Follow TCP stream
    - You will see that it is encrypted


## What if we knew the private key?

Knowing the private key allows Wireshark to inspect the packets, determine the session's master key exchange during TLS setup, and decrypt the session.

Private key for the session can be found [here](https://www.cise.ufl.edu/~jnw/Resources/https-stream-server.key)

Set up Wireshark to decrypt with this by going to Edit > Preferences, then expanding Protcols in the dropdown and selecting TLS, then add key to the RSA key list.


## Decrypting the session

With the right RSA key and its correct values in place, we can now "Follow TLS Stream" of packets and see their unencrypted contents.





# MODULE 0x0e LECTURE 0x350 - CROSS SITE REQUEST FORGERY (CSRF)

## What makes CSRF work?

A website trusts the user's browser to provide credentials (tokens) and make requests at only the right times.

* If the parameters of sensitive transactions are predictable, that enables CSRF attacks

* If the attacker can forge a request using valid authentication parameters and invalid transaction parameters, chaos ensues.
    - Forged requests (think of bank transfers) will be fulfilled by the web server.


## What does the attacker need?

- A link to initiate the transaction
- A way to deliver this link to a user's web browser in such a way that it will be followed.
- Examples of such a link are:
    - Image tags
    - iframes
    - CSS or JavaScript imports

* If a valid URL is followed under the right conditions, the web application cannot distinguish a forged transaction from a valid one


## CSRF plan in detail

1. The attacker finds a web application that has a transaction with predictable parameters
2. The attacker creates a CSRF webpage with a request URL for the forged transaction
3. The attacker places that on a server under their control
    - (It's even better if the server is the server providing the transaction Persistent XSS could provide that capability.)
4. The attacker arranges for a victim with an open sessionto browse the CSRF webpage
5. The victim's browser sends a request as a result of rendering the CSRF webpage.
6. The web application carries out the forged transaction

Example:
User is led to navigate to a bank containing an img link like:
`<img src=http://webbank/transfer_funds.cgi?from=31413523532&to=1618&amount=5000&date=10007288&re=0>`
This generates a valid transaction with the bank account, but transfers to account 1618 (hacker account) rather than the client's account


### QUIZ ACCESS CODE
Lou Reed - Intro / Sweet Jane


## (Bad) ideas to avoid CSRF

- Refuse to accept GET requests?
    - The evil website can easily cause a POST request to be sent from the user's browser instead of GET
- Require HTTP request to set the referrer field (which says where link came from)
    - This can be defeated if an appropriate referrer field can be provided (exploit code can forge this field)


## Good ideas to avoid CSRF

Good methods are supported by good frameworks. Use them.

Examples:

- Require provider to present a token that the attacker can neither guess nor find.
    - This requires a shared secret that is not stored in a regular cookie or otherwise available via Javascript to attacker
    - HttpOnly cookies prevent this sharing but must be used by the web application.

- Use SameSite=Strict cookie attribute (prevents cookie from being sent from a different site)
    - Sometimes, the attack may be originating from the same site, though.

- Server verification that the origin and target of request are the same site
    - Sometimes, the attack may be originating from the same site, though.


## Juice Shop CSRF demo





# MODULE 0x0e LECTURE 0x360 - Web Audit Tools


## Nikto

Open source website scanner

* Tests over 6700 vulns

* Identifies server vulns for over 270 different server versions

* Supports SSL

* Default behavior = not very stealthy
    - But it is configurable

* In addition to spidering, Nikto uses pathnames identified on numerous servers to guide its search


## What does nikto find in mutillidae (old OWASP vulnerable webapp)?

###Clickjacking
* The anti-clickjacking X-Frame-Options header is not present.
    - Clickjacking (Jeremiah Grossman, Robert Hansen) presents a website from one site in a transparent iframe on top of a webpage that presents a different view.
    - Good PoC [video](https://www.youtube.com/watch?v=gxyLbpldmuU)
    - Because this site does not set X-Frame-Options to disallow a page from rendering in an iframe, it is subject to clickjacking.

### Non-HttpOnly Cookies
* Both `PHPSESSID` and `showhints` are not declared as HttpOnly.
    - As mentioned, this means that these can be intercepted and manipulated with Javascript.
    - So, if there is any opportunity for injection, these cookies may be exposed
    - In addition, Nikto finds that this site is susceptible to Cross-Site Tracing (through HTTP TRACE or HTTP TRACK methods).
    - In some cases, this can be used to expose HttpOnly cookies to Javascript code

### Eight robots.txt entries
* The robots.txt Exclusion "standard" describes the form of robots.txt files
    - Entries in robots.txt are used _in theory_ to inform __compliant web spiders__ what portion of a website should not be visited
    - This file must be readable if it is to be useful
    - May contain info identifying sensitive locations on a website, so it provides info of value to intruders.
* Included in the mutillidae robots.txt file is the entry:
    - `Disallow: passwords/` -- looks interesting.

### Mod\_Negotiation is enabled with multiviews
* The multiviews option can make __directory listing__ easier.
    - If properly used, it allows one to find all the variants (versions with different extensions) of a base filename.
    - So, a query for index.html, index.php, index.bak, etc.
* This can significantly reduce the time required for brute-forcing a web directory to identify files it contains

### Path Injection vuln
* Nikto identifies what appears to be a vulnerability in the PHP Nuke-Rocket-add-in that allows arbitrary path traversal
    - e.g. `/mutillidae/index.php?page=../../../../../etc/passwd`


## SQLMap

* Normal trivial use:
    - `sqlmap -u http://example.com/app?uid=15 --dbs`
    - -u specifies a URL with a GET parameter, --dbs tells sqlmap to list the databases

* To get the tables in a database:
    - `sqlmap -u http://example.com/app?uid=15 --tables -D dbname`

* To get the columns in a table:
    - `sqlmap -u http://example.com/app?uid=15 --columns -D dbname -T tablename`

* To get the contents of a table:
    - `sqlmap -u http://example.com/app?uid=15 --dump -D dbname -T tablename`


## SQLMap -- URI with POST parameters

* To analyze a URI having POST parameters (such as `username` and `password`), sqlmap requires a sample request.
    - You can capture a request with a proxy or browser tool and store it in a file, say, `request.txt`

* Then, issue the following to test for injectability of the parameter username:
    - `sqlmap -r request.txt -p username`

* Then you can issue any of the variants of the command presented before to attack the database.


## What is this Ladanum thing?

* Collection of injectable files developed and maintained by Secure Ideas designed for use in pentests.
    - Found in `/usr/share/laudanum` in Kali
    - One useful script if you can do php executable file upload is `shell.php`

* Each of these scripts can be properly protected to limit access by IP address and user/password authentication
    - This is important! On a pentest, you don't want others using your remote shells.





# MODULE 0x0e LECTURE 0x370 - SSRF AND PATH NORMALIZATION

## What is SSRF?

Server-side request forgery (SSRF) is an attack in which one causes a server to generate and satisfy an unintended HTTP request.

* Web servers must parse URLs, headers, and bodies of requests

* Modern software development depends on the use of library code to solve well-defined subproblems
    - Different library functions may be used by diffferent phases of processing a web request
    - They may disagree on how to interpret requests.

## SSRF simple example

`http://1.1.1.1 &@2.2.2.2# 3.3.3.3/`
    - Different libraries will interpret this differently
    - urllib2, httplib: `http://1.1.1.1`
    - requests: `http://2.2.2.2`
    - urllib: `http://3.3.3.3`


## What is a URI, really?

* `foo://example.com:4444/path/info?param=value#whatup`
    - `foo://`: The Scheme. We mostly care about http and https.
    - `example.com:4444`: The Authority (the web server itself)
    - `/path/info`: The Path (from the root to the page we're trying to get to)
    - `?param=value`: The Query
    - `#whatup`: The Fragment

* Different parsing libraries have different [vulnerabilities](https://i.blackhat.com/us-18/Wed-August-8/us-18-Orange-Tsai-Breaking-Parser-Logic-Take-Your-Path-Normalization-Off-And-Pop-0days-Out-2.pdf) associated with them.


## Different parsers get different results

RFC 3986 describes how to parse the authority section using the English language

* Imagine you have code to accept URLs of google.com but reject all others, and you see this URL:
    - `https://google.com#@evil.com/...`
    - PHP's `parse_url`interprets that as a google link.
    - PHP's `readfile` interprets that as an evil.com link

* If your code determines acceptable urls with parse\_url, but fetches content with readfile, you can be in trouble!


## Do other URL parsers agree?

`https://foo.com@evil.com:80@google.com/`

- cURL and libcurl see this as evil.com:80
- NodeJS URL, Perl URI, Go net/url, PHP parse\_url, and Ruby addressable see this as google.com


## Using Unicode to exploit parsing

* The Unicode standard defines UTF-8, UTF-16, and UTF-32.
    - These employ 1, 2, and 4 bytes respectively to represent characters.

What about this URL: `http://foo.bar/whatever/NN/passwd`
    - The two NN above are unicode characters, each `\xFF\x2E`
    - But `\x2E` in ASCII is a period ('.')
    - NodeJS ignore the `\xFF` characters and interprets this URL as `http://foo.bar/whatever/../passwd`


## Unicode CRLF Injection of NodeJS

* Inserting `\xFF\x0D` and `\xFF\x0A` can be used to inject \x0D\x0A or CR/LF.
    - This allows you to inject a header field from a URL

This URL: `http://foo.bar/\r\nTHIS IS REAL\r\n` sends:
```
GET /%0D%0ATHHIS IS REAL%0D%0A HTTP/1.1

Host: foo.bar

Connection: close
```

But this URL: `* THIS IS REAL- *` sends
```
GET /

THIS IS REAL

HTTP/1.1

Host: foo.bar

Connection: close
```
, accomplishing header injection.


## How far can we take this?

* Orange Tsai demonstrated a 4-stage protocol-smuggling attack that generates remote code execution using github.
    - See the [video](https://www.youtube.com/watch?v=GoO7_lCOfic&feature=youtu.be)

* The exploit delivers remotely executable code and executes it to connect to a remote host


## Path normalization

* Orange Tsai also has a talk on this (BlackHat 2018, Defcon 26)

* Talk demonstrates how incorrect parsing in normalization can lead to security vulnerabilities


## Function definitions matter

Two javascript functions: replace and replaceAll:
`String replace(String target, String replacement)`
`String replaceAll(String regex, String replacement)`

* Mojarra, a Grails plugin, used `replace(pattern.quote("/"), '/')` to replace quoted / characters (`"/"`) with unquoted / characeters.
    - This makes it so that `"\Q \E"` is replaced by `/`
    - So, `..\Q \E` is the new `../`


## Make sure to replace the right string

Nginx has an alias directive that does string replacement for specific locations.

Here is a broken specification intended to map the `/static` directory to `/home/app/static`:
```
location /static {
    alias /home/app/static/;
}
```

* The problem here: the extra `/` at the end of `/static`
    - `http://127.0.0.1/static../settings.py`


## Rails broken file:// behavior

* Rails supports use of file:// tokens that bypass the absolute path.
    - So, `http://127.0.0.1/file://something` becomes `file://something`

* With appropriate URL encodings for / and ., `http://127.0.0.1:3000/assets/file:%2f%2f/app/assets/images/%252e%252e/%252e%252e/%252e%252e/etc/passwd`
    - ...becomes `file://app/assets/images/../../../etc/passwd`


## Amazon Collaborate System RCE

* Tsai's talk gives an impressive example of chaining exploits together
    - Path normalization for ACL bypass
    - Whitelist bypass to access an unauthorized servlet
    - Use of actionMethod to invoke gadgets in a known file
    - Second stage preparation using directoryNameForPopup
    - Bypass of blacklist using array-like operators
    - Shellcode written with the Java reflection API to deliver reverse shell exploit


END MODULE 0x0e
