# MODULE 0x060 LECTURE 0x180 - UNIX/LINUX INTRO PASSWORD SECURITY

## Linux/Unix permission sets

File permissions can be set for any of the following sets of users:
- **u**: The user who owns the file
- **g**: The group associated with the file
- **o**: Any other users

* A user's permission set (for file access) is determined by the most restrictive set containing that user
    - If you are file owner, o permissions apply
    - If you are not owner but are in the file's group, g permissions will apply
    - If not owner or in group, o permissions apply

### Basic set of permissions (3 bits)

**r** permissions (Read):
    - Bit 2 in the octal representation of permissions (1xx)
    - For a directory, this means we can list files in it

**w** permissions (Write):
    - Bit 1 (x1x)
    - For a directory, this means we can store files in it.

**x** permissions (Execute):
    - Bit 0 (xx1)
    - For a directory, this means we can traverse it.

### The extra permissions (3 more bits)

**s/S** (set user/Set group):
    - Bit 2/1 of extra permissions group
    - If s is set, then wehn executing the file, it runs as the owner of it.
    - If S is set, then when executing the file, it runs it as the group that owns it.
    - Shows as s if the file has execute permissions or S if it does not... but why would you set execute permissions on a file that is not executable? read below)
    - For directories, set group ID means created files will belong to the same group as the directory.
    - Convenient way of creating files within a folder that is accessible by a set of members of the same group

**t** (Sticky bit):
    - Bit 0 of extra permissions group
    - Only root and file owner can remove file from directory it resides in
    - Linux ignores this on files, unfortunately. Does respect it for directories.


## File permissions example: /etc/passwd

`ls -l /etc/passwd`
- Output shows `-rw-r--r--`
- First dash represents the file type (`-` for a file, `d` for directory)
- Next three bits represent owner (u) permissions
- Next three represent group (g) permissions
- Last three are other (o) permission

Looking further:


`-rw-r--r-- 1 root root 1845 Oct  3 10:58 /etc/passwd`
- The number '1' represents the number of _links_ to this file in the filesystem
- The first 'root' is the owner
- The second 'root' is the group
- '1845' is the _inode_ number for that file in the filesystem


## File date/time (mac time)

Three times are associated with each file:
1. **mtime**: Time of last modification to the file contents (when it was last written)
2. **atime**: Time of last file access (when it was last read)
3. **ctime**: Time of last change to file's metadata (includes permissions)

* Note that for ctime, the metadata is anything about the file that might change -- where it appears in the filesystem (inode), permissions,

* By default `ls -l` shows the mtime.
    - `ls -lu` shows atime instead
    - `ls -lc` shows ctime instead

All of these times can be useful for forensics analyses.


## Linux File Access Control Lists (ACLs)

Access Control Lists let you assign specific file permissions to users or groups in a way that overrides the u, g, and o permission

* Use `setfacl` to assign a specific ACL to a file, e.g.
    - `setfacl -m u:jnw:rwx thisfile` (modifies the file so that user 'jnw' has read, write, and execute permissions)

* When using `ls -l` on a file with ACLs set, you will see a `+` sign on the right of the file permissions
    - Use `getfacl <filename>` to see the ACLs on a file.


## Other Linux file attributes

A number of special attributes can be set for Linux files.
These include the following (with their attribute letters):

append only (a), compressed (c), no dump (d), extent format (e), **immutable (i)**, data journalling (j),
secure deletion (s), no tail-merging (t), undeletable (u), **no atime updates (A)**, synchronous directory updates (D),
synchronous updates (S), and top of directory heirarchy (T).

* Note the bolded items above
    - Potential usage for wreaking havoc on sysadmins
    - May serve as indicators of compromise if these attributes are unexpectedly found

### How do we set/check these special attributes?
Answer: `chattr` and `lschattr`

e.g. `chattr +i test.txt` or `chattr -A hello.png`; view special attributes with `lschattr test.txt`

* Why would this be useful to an attacker?
    - e.g. maybe there is a file that normally would be modified. Set this attribute, and scripts that modify it may silently error. May cause general system misbehavior on victim hosts.


* Permissions on the /tmp directory
```
root@kali:/# ls -ld /tmp
drwxrwxrwt 11 root root 40
```
Note the sticky bit set at the end. It is set because all users must be able to create files in /tmp/, but other users must not delete your files in /tmp/.


## What's in /etc/passwd ?

* Note:
    - /etc/passwd is useful for _local authentication_

**passwd file**:
- One line for each user
- Entries on each line separated by colons
    - user name (string)
    - password (?)
    - user id (number; root is always 0)
    - group id (number; root group is also 0)
    - user's "real" name (gecos? -- string)
    - home directory (root's home is /root)
    - shell (filepath)

* If a user/group other than root has id 0, then something is wrong.


## Hash functions

A hash function is a surjective map (all elements in range are covered) from a large (possibly infinite) domain into a smaller, finite range.

* Cryptographic hash functions typically map unbounded sequences of bytes (files) into short, fixed-length bit-strings (hashes)
    - e.g. MD5, SHA256, SHA512

* Good hash functions are relatively uniform, yet not easily predictable (hard to generate a message that has a known hash value)


## Where are the passwords?

Symmetrically encrypted user passwords used to be stored in /etc/passwd

* In 1987, a break-in prompted Julie Haugh to develop the shadow file plan
    - The break-in was deduced to have been achieved with an offline password cracker
    - Realized having encrypted passwords available may not be so smart

* Under the shadow plan, password hashes (which users don't need to see) are placed in a file that is readable only by root.
    - It is the /etc/shadow file
    - Thus, password hashes are in the shadow file

* Each password is also assigned a salt (randomly generated string) that is concatenated to the password
    - This makes it so that multiple identical passwords have different hashes (with high likelihood)

Shadow file contents
- Like passwd, it is colon-delimited
- Fields:
    - username
    - salt and hashed password (dollar signs?)
    - days since epoch of last password change -- Epoch starts January 1, 1970
    - days until a change is allowed
    - days before a change is required
    - days before warning for expiration
    - days before account is inactive
    - days since epoch when account expires
    - reserved

### $id$salt$hash

* In /etc/shadow, there are three items that have dollar signs between them
    - an ID: tells what hash function is used (`$1$`: MD5; `$2a$`: Blowfish; `$5$`: SHA-256; `$6$`: SHA-512)
    - a salt: prepended to the password before hashing
    - a hash: the resulting hash


### Autentication plan
1. User provides password to authentication program
2. Login program looks up the hash function h, the salt, and the hash in /etc/shadow
3. Login program calculates h(salt:password) and compares to the hash
4. Login succeeds if match; otherwise, login fails





# MODULE 0x060 LECTURE 0x190 - Cracking Unix/Linux Passwords

## How does one crack Linux passwords?

A: Password cracking program...

* Required elements:
    - Shadow file
    - List of candidate passwords

Method -- dictionary attack:
* Do what login does but try each candidate password (from a dictionary) with every user account that has a hash


## How to get dictionary passwords

- Use actual dictionary words (not so great)

- Black hats post breached passwords to pastebin or other locations (getting better)

- Use Troy Hunt's password database from [HaveIBeenPwned passwords](https://haveibeenpwned.com/Passwords) -- 6GB hash download link is available there

- rockyou.txt (14 million plaintext passwords)

- Crackstation-human-only.tgz -- compendium of a huge variety of lists (~64 million passwords total)

- Troy Hunt's 6GB list of hashes is currently being cracked by hashes.org (but the site is down?)

#### NOTE DURING EXERCISES:
**Any passwords that can be cracked will be drawn from rockyou.txt**


## John The Ripper

A product from OpenWall

* `john --test`
    - Shows the different types of passwords John can crack and how quickly it can do it on your machine

* To prepare a Unix passwd and shadow file for cracking, use `unshadow /etc/passwd /etc/shadow >jpasswdfile`
    - Remember that John needs to be run as root

* To use rockyou.txt on an unshadow file with John:
    - `john -wordlist=/usr/share/wordlists/rockyou.txt jpasswdfile`
    - Stores results in `~/.john/john.pot`

* You can run `john -show jpasswdfile` to search for the entries in john.pot to see the passwords there
    - This sews passwords from john.pot into password entries from the specified file


## Hashcat and OCLHashcat

Alternative hash programs.

* hashcat
    - Originally CPU-based.
    - Supports multiple modes (brute-force, wordlist, wordlist with __rules__)
    - Now GPU-based
    - Can pose problems running in VMs without attached GPUs

* oclhashcat-lite
    - Brute-force attacker primarily for directed attacks (against a single hash) or password-cracking competitions.

### Preparing hashes for hashcat

* Must remove everything from file but the hashes
    - For Linux passwords, this means basically leaving what is between the colons in `/etc/shadow`
    - e.g. `$1$/avpfBJ1$x0z8w5UF9Iv./DR9E9Lid.`


* Attack modes (`-a` parameter value)
    - 0 = straight (use wordlist)
    - 1 = combination (multiword passwords)
    - 3 = brute force
    - 6 = hybrid wordlist + mask (you need to provide the mask)
    - 7 = hybrid mask + wordlist

* Hash modes (`-m` parameter)
    - 0 = MD5
    - 10 = md5($pass.$salt)
    - 50 = HMAC-MD5 (key = $pass)
    - 60 = HMAC-MD5 (key = $salt)
    - 100 = SHA-1
    - 400 = phpass, MD5 wordpress, MD5 phpBB3
    - 500 = md5crypt, MD5(unix), FreeBSD MD5, Cisco-IOS MD5
    - 900 = MD4
    - 1000 = NTLM
    - etc...

* Example
    - `hashcat -a 0 -m 500 /tmp/hashes /usr/share/wordlists/rockyou.txt`
    - (using Windows hash obtained with Meterpreter) `sudo john --wordlist=/usr/share/wordlists/rockyou.txt LMhashfile`
    - The above command ran in the lecture videwo gave us the password 'MALWARE' which is 7 characters long -- this is noteworthy for LM passwords as we will see later. Note that it is all upercase.
    - we can use od  (octal dump) to see ASCII codes of text in a file (e.g. last few entries of rockyou have space characters)
    - `man ascii` is useful
    - `od -t xC foo` hadndles little-endianness + separates into hex bytes
    - you can stop a John process and then resume later with `john -restore`


## Password cracking hardware

Every year, new reports about cracking hardware configs are posted

e.g. McCracken 2019; ~$5,000

### QUIZ ACCESS CODE
Tom Petty - American Girl





# MODULE 0x060 LECTURE 0x1a0 - WINDOWS PASSWORDS

## Remote password guessing vs. spraying

* Trying to _remotely_ guess a single password is a suboptimal strategy because of the time required plus likelihood of getting locked out
    - You want to do this offline if possible

* Spraying likely passwords at a number of accounts is often used when a remote password authenticator is available
    - Passwords like Fall2020, test, administrator...
    - Lockout less likely due to delay between each query on a single account

* Most Windows password guessing is done through SMB (TCP ports 139 and 445)
    - Often blocked by a firewall
    - This is often done via password guessing on remote shared mounts

* Example with 192.168.202.44 is our remote target w/ account Administrator:
    - `C:\> net use \\192.168.202.44\IPC$ * /u:Administrator`
    - The above is us mounting the remote share

* You can automate guessing for a single account on Windows:
```
FOR /F "tokens=1" %i in (credentials.txt) do
    net use \\target\IPC$ %i /u:Administrator
```

* To find out how Windows `FOR` works:
    - `FOR /?`


## Windows Password Guessing from Linux
TODO
