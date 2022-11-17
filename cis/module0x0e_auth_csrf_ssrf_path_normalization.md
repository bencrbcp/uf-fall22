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
