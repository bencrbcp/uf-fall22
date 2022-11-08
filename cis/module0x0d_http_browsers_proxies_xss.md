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





# MODULE 0x0d LECTURE 0x310 - WEB HACKING
