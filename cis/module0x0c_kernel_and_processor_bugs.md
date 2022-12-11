# MODULE 0x0c LECTURE 0x301 - THE DIRTY COW UNPENNED

## Who found the CoW?

* Phil administered a server that supported full packet capture of every HTTP request
    - That server had been compromised, so he investigated the server logs

* He found that the exploit employed a bug in __Copy on Write__ to elevate privileges to root.


## How long was it from discovery to patch?

* Linux kernel vulnerability assigned [CVE-2016-5195](https://dirtycow.ninja/).
    - CVE number assigned 31st of May 2016 (>4 months before resolution)
    - CVE report at Mitre was not even complete on the 23rd of October


## Order of events in disclosure

1. Oct 13, 2016: bugzilla.redhat.com reported that a kernel bug existed
    - "An unprivileged local user could use this flaw to gain write access to otherwise read-only memory mappings and thus increase system privs."
2. Oct 18, 2016: Linus Torvalds committed a [patch](https://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git/commit/?id=19be0eaffa3ac7d8eb6784ad9bdbc7d67ed8e619)
    - Linus mentioned that he tried to fix this 11 years ago; difficult to exploit at the time, but improvements to the virtual memory system made it more readily exploitable.
3. Oct 19, 2016: Peter Matousek posts a work-around at bugzilla.redhat.com
4. Oct 24, 2016: Kernel security update [released](https://lwn.net/Articles/704079/)





# MODULE 0x0c LECTURE 0x303 - HOW DOES THE DIRTY COW WORK

## How does the dirty cow work?

The exploit from [exploit-db](https://www.exploit-db.com/exploits/40616)

1. First, it opens the file which it wants to write to with RDONLY permission

2. Next, it __mmaps__ the readonly file into memory
    - This means that operations on the memory will be reflected by corresponding (permitted) operations on the file
    - The file is mapped with with protection `PROT_READ` and flags `MAP_PRIVATE` which will create a copy-on-write mapping.
    - A __copy-on-write__ mapping uses shared memory pages for the mapped file only if reads are performed and creates a local _copy_ if you write to the mapping.

3. Next, the code starts up three threads that run concurrently.
    - Two of them are doing the exploitation, one just waits.
    - They attempt to exploit a __race condition__ in which an unexpected (out-of-order) sequence of events can lead to consequences the kernel developer didn't intend.
    - 1: `madviseThread` declares that we no longer need the mmap
    - 2: `procselfmemThread` writes the memory
    - 3: `waitforWrite` waits for the write to succeed.

4. The `madvise()` call has advice value `MADV_DONTNEED` which says "We won't be using this memory in the near future and it can be freed up"
    - If it's used again, the pages will be reloaded from the mapped file.

5. The `procselfmemThread` writes to `/proc/sef/mem` (a file interface to the memory of the current process) in the area of the memory-mapped file
    - If the timing is right, a write will occur after the file memory has been freed and will cause the file association to be recreated in a kernel action where the `MAP_PRIVATE` flag has not been recoreded.

Here's a good liveoverflow video on this: [Link](https://www.youtube.com/watch?v=kEsshExn7aE)


## What does the exploit write?

1. The program saves `/usr/bin/passwd` to `/tmp/bak`

2. Then, it writes Metasploit shellcode that spawns a bash shell to `/usr/bin/passwd`, which is a set-user-root program
    - It tries to do so with the procselfmemThread over and over again.
    - If it succeeds, then the waitForWrite thread will recognize this and stop the attempts

3. After it writes the shellcode, it execs `/usr/bin/passwd` using the `system` function call, spawning a root shell when it succeeds.
    - WARNING! Once executed, this exploit can cause system to become unstable. Several things can be done to mitigate this, see them [here](https://github.com/dirtycow/dirtycow.github.io/issues/25)
    - __In CPTC, best practice was determined to be that, upon finding a system vulnerable to DirtyCoW, ask a competition admin if rather than exploiting it and potentially causing damage to systems, simply give them root on box.__


## Can use of the exploit be detected?

Maybe.

* Exploitation of the race condition reveals no direct forensic evidence from the kernel
    - (There is no kernel logging of this race condition)

* The time of the file will be changed
    - However, since the user is root, they can do a variety of things to erase or avoid disclosing tihs evidence


### QUIZ ACCESS CODE
Robert Johnson - Terraplane Blues


## How would someone even know this kernel race condition exists?

Two possibilities:

1. We are Linux page management experts, we've studied the kernel carefully, and we've discerned the existence of this error by modeling the possible event sequences.
2. We're smart but lazy and we found the "fix\_get\_user\_pages() race for write access" kernel patch issued in 2005 and then immediately rescinded. We looked at the documentation and were led to understand the underlying race condition.


## How long do kernel bugs stay undetected?

* Jon Corbett (2010) and Kees Cook (2016) did some pretty comprehensive studies on how long Linux kernel bugs go undetected
    - Link [here](https://lwn.net/Articles/410606/)
    - In the Cook study, about 3.3 years for bugs of _critical_ severity, 6.4 for _high_ severity.


## How can I manipulate the kernel to understand this condition?

"Become a kernel hacker. Just do it."

* [Linux Kernel development book](https://www.pearsonhighered.com/program/Love-Linux-Kernel-Development-3rd-Edition/PGM202532.html)


## How hard is it to compile a new (old) kernel?

See instructions from slide [here](https://i.imgur.com/1kn5zO7.png)


## Then what?

"The majority of kernel debugging is done by adding print statements to code by using the famous `printk` function.
    - This technique is well-described in Kernel [Debugging Tips](https://elinux.org/Kernel_Debugging_Tips)

* You can also use kgdb (GDB connected to the serial port of a machine with a _properly prepared kernel_)
    - Then debug the kernel (almost) just like a program.

* Look for events of interest (places where the `pte_dirty` bit is changed, places where CoW writes occur)

* Kernel hacking is great fun for problem solvers!


## How long will the cow be with us?

Many hosts were patched quickly, many were not.





# MODULE 0x0c LECTURE 0x305 - RECENT LINUX VULNERABILITIES

## Eternal Red

* Just as EternalBlue is an SMB exploit for Windows, Eternal Red is an exploit for Linux (possibly named after RHEL)
    - [CVE-2017-7494](https://cve.mitre.org/cgi-bin/cvename.cgi?name=cve-2017-7494)
    - Described well at [Sophos Naked security website](https://nakedsecurity.sophos.com/2017/05/26/samba-exploit-not-quite-wannacry-for-linux-but-patch-anyway/)

The exploitation plan is the following:

- Find a writeable SMB share being provided by a vulnerable version of Samba (Linux SMB service)
- Upload a .so (shared object library) file
- Identify the local filename of that .so file
- Send an IPC request to run that locally-stored programA

If these steps succeed, the file is executed by the user running Samba (usually root).


## Keep your socks clean (Dirty sock)

* One recent vulnerability/exploit that followed Dirty COW was given the name __dirty sock__
    - [CVE-2019-7304](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2019-7304)
    - _Not_ a kernel vulnerability

* Dirty sock is associated with an injection vulnerability in the __span__ daemon (the one in Ubuntu, for installing packages, creating users, etc.)

* To achieve some of its outcomes (the sensitive ones such as creating users) it checks the uid of the executing user and records this in an attribute string
    - When it checks for the _uid_, it searches the attribute string for the _uid_ property
    - One can __inject__ an extra uid property into this string by manipulating a parameter which is not sanitized.
    - Thus, one can get privilege escalation by injecting `uid=0` (root) into that parameter.
    - The code uses the last uid property it sees (the malicious one) and one can then create highly privileged users (i.e. root users)


## Sudo? More like Su-doh

[CVE-2019-14287](https://www.cvedetails.com/cve/CVE-2019-14287/)

* The problem occurs because of a vulnerability associated with a little-used configuration in the sudoers file (`/etc/sudoers`)
    - The sudoers file identifies which users can execute what programs with special privileges (for example, as root or another user). It can be useful to provide such capabilities to some users for limited activities

* A sample sudoers entry is the following:
    - `fred     hostname = (root, bin : operator, system) ALL`
    - Here, on the hostname, user `fred` may run a command (`ALL`) as either `root` or `bin`, optionally setting the group to `operator` or `system`

* A sample of the exploitable entry is the following:
    - `bob      ALL - (ALL, !root) /usr/bin/vi`
    - In this specificaiton, on any host (`ALL`), user `bob` can execute the command `/usr/bin/vi` as any user (`ALL`) except `root`.
    - When running sudo, the user can be set with `-u`, providing the username or uid (following a #)

* If sudo is executed in this way: `sudo -u#-1 /usr/bin/vi`
    - Then the command will run as root.


## Linux EBPF local privilege escalation

* This kernel bug (CVE-2020-8835) affects Linux Kernel versions 5.4.7 through 5.6.0
    - See the report on this by [The Zero Day Initiative](https://www.zerodayinitiative.com/blog/2020/4/8/cve-2020-8835-linux-kernel-privilege-escalation-via-improper-ebpf-program-verification)
    - This bug exploits the __extended Berkley Packet Filters__ that Linux provides so users can filter network packets and trace kernel functions

* The BPF syscall lets users provide what are essentially assembly language programs
    - The code that is supposed to verify that the filter is non-malicious has an erroneous assumotion that can be exploited to allow out-of-bounds read and write that can be leveraged to get arbitrary code execution.

END MODULE 0x0c
