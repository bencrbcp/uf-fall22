# MODULE 0x090 LECTURE 0x250 - ACTIVE DIRECTORY INTRO

## Organizational Units (OUs)

* OUs are AD containers that hold objects of various kinds.
    - Some of those objects are security principals.

* Security principals are associated with SIDs and can be user accounts, groups, or computer accounts.
    - __No other objects in an OU can be security principals__

* Policies and other settings apply to objects within the OU
    - Not the OU itself


## Security principals

Security principals have long been a foundation for controlling access to securable resources on Windows computers.

Each security principal is represented in the operating system by a unique security identifier (SID).

Security princals can be:
- Assigned permissions to access resources
- Given user rights
- Audited by tracking their actions

* Security principals form the basis of the AD security architecture...
    - ...just as filesystem permissions form the basis of Linux security architecture.


## OU Structural Overview

OUs contain:
- User
- Groups
- Computers
- Shared folder objects
- Contacts
- Printers
- InetOrgPerson objects (for LDAP, X.500)
- Microsft Message Queueing (MSMQ) aliases (used for inter-process communication)
- Other OUs

* OUs exist specifically to group these objects into a hierarchy so that a hierarchical policy structure can be built on them.

* OUs:
    - Support multiple hierarchical layers
    - Support inheritance of OU settings in child OUs
    - Support Group Policy settings applied to their objects
    - Allow delegation of OU administration to appropriate users and groups


### QUIZ ACCESS CODE
Art Brut - Emily Kane


## OUs group resources hierarchically

We use the term _resources_ in all senses of the word to include both people and things.

Hierarchical groupings corresponding to business structure are commonly employed, e.g.:

- Sales
    - US
    - International
- Marketing
- Engineering
    - Development
    - IT
    - Testing

Individuals are assigned roles such as _Manager,_ _Technical Staff,_ _Planner..._
Some singular individuals might find different roles in different department OUs.

OU names are relatively short (64 char limit).
OUs must be unique within a given OU nesting


## Inheritance and OUs

OUs are the smallest domain unit to which administrative permissions and group policies can be assigned.

* Examples:
    - One sysadmin role for _IT Computing resources_ and another for _Development_
    - One individual responsible for all user and group security permission

* Principle of least privilege should be enforced
    - Users should have minimum permissions to do their jobs.

* Access Control Entries (ACEs) grant administrative rights on objects in a container to a user or group.
    - A container's Access Control List (ACL) stores ACEs.

* You can allow child containers to inherit the permissions set on parent containers automatically.
    - This can save work, but... its implications must be understod.

* __Child objecst are configured to inherit permissions by default.__
    - This must be overridden explicitly if it is the wrong choice for an organization


## How do groups work?

Groups have a type:

* Security group:
    - Have associated rights and permissions and can receive email
* Distribution group:
    - Used for email only

Groups have a scope:

* Domain local
    - Remain in the domain in which created.
    - Can grant permissions in that single domain
* Global
    - Can contain other groups from the domain in which created. Can give permissions in any domain in the forest.
* Universal
    - Can include groups and accounts from any domain in the tree or forest. Can include permissions in any domain, as well.


## Group policies

Before group policies: Anarchy
After group policies: Mayhem

* Group policies can be assigned at the Site, Domain, or OU level.
    - Site refers to "like an IP domain"
    - They can apply to __user accounts__ and __computer accounts__

* Sample policies:
    - Restrict users from installing programs
    - Disallow use of the control panel
    - Limit choice for display and desktop settings
    - Policies can be edited with `gpedit.msc`


## ACLs and ACEs

Each object in AD has an Access Control List (ACL)

* This is a list of user accounts and groups that are allowed to access the resource.

* For each ACL, there is an Access Control Entry (ACE) defining what those users can do with the resource.

* Deny permission are always listed first.
    - Access is denied to a user even if later permission allows that user access


## Using group policy for security

Group policies can be used to assign many different settings and options for users, groups, and objects belonging to an OU.

* Useful for setting password policies, user rights, account lockout paremeters, etc.

* One creates a __Group Policy Object__ with the desired settings and links it to an OU or other object.


### Sample settings: Account Policy -> Password Policy

- Enforce password history
- Minimum password length
- Account lockout threshold
- Account lockout duration
- Reset account lockout counter after...

### Sample settings: Local Policy -> Security Options

- Accounts: Rename Administrator Account
- Domain Controller: Allow Server Operators to schedule tasks.
- Interactive logon: Do not display Last User Name
- Shutdown: Allow system to be shut down without having to log on






# MODULE 0x090 LECTURE 0x260 - LOCAL VS. DOMAIN ACCOUNTS, UAC BYPASS

## Local vs. Domain Account

* Domain user accounts are registered with an Active Directory server for a network of machines
    - Authorization credentials, access rights, and privileges are associated with that account for the entire domain.
    - Users in the domain administrative group have administrative rights over every machine in that domain.

* Local user accounts are recorded in the SAM database on a local machine and the access rights and privileges of the account extend only to the local machine
    - A local account will not support Kerberos authentication for network services.


## Network shares

Filesystems, printers, interprocess communication, etc. may be shared on an MS network via network shares

* You can see what shares can be made available remotely by executing:
    - `net share`

* You can see what hosts/shares are available at a host with:
    - `net view`
    - `net view \\host`

* You can mount a filesystem to drive `d` from the `host` by executing:
    - `net use d:\\host\filesystem`


## User account control

Maintains process integrity levels (high, medium, low) that determine a process's rights (privileges)

* Changing your integrity level to perform privileged actions has four notification setting values:
    - Always notify
    - Notify me only when programs try to make changes to my computer
    - Notify me only when programs try to make changes to my computer (do not dim desktop)
    - Never notify

If an administrative user running at medium level employs the `RunAs`, notifications as set in UAC occurs.


## Integrity levels

* High
    - Server generated challenge
* Medium
    - Mutual authentication of server and client
* Low
    -  Requires NT hash for authetnication


## UAC Bypass

Given:
- An administrative process at medium integrity level
- Default UAC notification setting, i.e. __Notify me only when programs try to make changes on my computer__

UAC bypass can be achieved by leveraging MS code that uses the autoelevate feature to elevate privs without notifying a user.

* A metasploit module that emplos an existing administrator session can be used to spawn a payload using `bypassuac_injection` using that session to exeute a payload with elevated privileges.
    - `use exploit/windows/local/bypassuac_injection`
    - `set session`
    - `set payload`


## But that's old stuff... does autoelevation still work?

* [Mitre: Bypassing UAC](https://attack.mitre.org/techniques/T1548/002/)
    - Last updated 2019
    - UAC bypass is still a thing


## Local admins are still powerful

* Suppose you have executed a meterpreter shell in a local user's account with medium level access and administrator privilege.
    - If you can bypass UAC controls, you can elevate the meterpreter process to have administrator level privileges

* In such a meterpreter shell, a successful execution of the getsystem command will yield the Local System account
    - That Local System account (NT AUTHORITY\SYSTEM and having SID 5-1-5-18), is like several other pseudo-accounts, built into Windows and does not behave in the same ways as user accounts.
    - It is not recognized by LSASS, for example.
    - This account has privileges that span beyond local administrator accounts and include access to most system objects.





# MODULE 0x090 LECTURE 0x270 - ACTIVE DIRECTORY AND GppLocalAdmin

## What about local admins?

* When new machines are deployed, it's easier to maintain them if you gain local administrative access easily and reliably.
    - As time goes on, we want to maintain this local admin over these machines

* AD made it easy to distribute this local admin control by creating a local administrative user account and propagating it to all members of the appropriate AD OU.
    - If such a central distribution system is implemented poorly, it provides a foothold for attackers


## How it USED to be done

Local admin accounts would be User accounts associated with an Organizational Unit
    - Their passwords would be assigned in a group policy preference and stored in the AD database
    - The account information would be stored in the SYSVOL volume (mountable by each domain computer)
    - The account would be associated with a domain-wide administrator group that can administer any machine

* More specifics in this cantipro archive from 2014:
    - https://learn.microsoft.com/en-us/archive/blogs/canitpro/group-policy-creating-a-standard-local-admin-account


## What's the problem?

The local administrator group policy information was stored in a file `groups.xml` in the SYSVOL directory on the domain controller
    - This directory is shared with everyone in the domain, readable by every user.
    - The `groups.xml` file contains an AES encrypted version of the password for the user
    - Thus, if the key were known, this would be a problem
    - ...UNFORTUNATELY, Microsoft published the 32-byte key used to encrypt passwords (very convenient for people who forgot their password, or those who never knew it)
    - [Click here to see the key](https://learn.microsoft.com/en-us/openspecs/windows_protocols/ms-gppref/2c15cbf0-f086-4c74-8b70-1f2fa45dd4be?redirectedfrom=MSDN#endNote2)


## Microsoft issued a patch

* __MS14-025__ was issued to address this problem (which had been present for at least 2 years xD)
    - The patch makes it impossible to place passwords in a group policy preference.
    - But existing group policiy preference files __were NOT removed by that patch!__

* Microsoft issued Local Administrator Password Solution (LAPS) software to address the need for controlling local admin passwords in a sane way


## What does LAPS do?

- Checks whether the password of the local admin has expired
- Generates new password when the old password is either expired or is required to be changed prior to expiration
- Validates the new password against the password policy
- Reports the password to AD, storing it with a confidential attribute with the computer account in Active Directory
- Reports the next expiration time for the password to AD, storing it with an attribute with the computer account in Active Directory as well.
- Changes the password of the Administrator account
- The password then can be read from AD by users who are allowed to do so. Elegible users can request a password change for a computer.


## Two years (plus five years) and counting

This problem still exists on machines in a variety of networks.

* The PowerSploit Exfiltration Get-GPPPasswords.ps1 module exploits this vulnerability if the `groups.xml` file exists in the SYSVOL share.

* The Metasploit module `post/windows/gather/credentials/gpp` exploits this as well.





# MODULE 0x090 LECTURE 0x280 - STICKY KEYS TO THE KINGDOM

## What happens if you press the shift key (or hold it down so that it repeats) 5 times?

A popup appears asking if you want to enable sticky SHIFT, CTRL, ALT so you can press one of those keys followed by the key it should modify

* This is an accessibility feature for people who may have limited ability to press 2 keys at the same time


## Running Sticky Keys

...But this popup appeared before I logged in!

* What user does Sticky Keys get run as at logon screen?
    - Answer: __NT AUTHORITY\System__

* What program got run when the Stickey Keys dialog box pops up?
    - Answer: __sethc.exe__

* Can set this with the registry entry HKLM\Software\Microsoft\Windows NT\CurrentVersion\Image File Execution Options\sethc.exe
    - This is a problem!!!
    - One can modify the sethc.exe program either by replacing the binary or adding/modifying the appropriate registry entry (__if they're an administrator__)


## Requirements for modification

How does one replace `sethc` with another program?
- We need offline or elevated access to the filesystem
- A Microsoft digitally-signed binary is required
- The substitute binary must be in `...\System32\`
- The substitute binary must be in Windows _protected file_ list

* `cmd.exe` satisfies all of the above


## Why would anyone do this?

* This is one of the first results on Google for "forgot windows password how to reset"


## Sticky Keys to the Kingdom

This script:
1. Identifies hosts running RDP (port 3389)
2. Sends Shift key presses
3. Takes screenshots, saves to a directory
4. Opens image files and looks at organization of screen to determine if a command shell was encountered (uses ratio of black pixels)

* 571 command prompts were achieved out of 100,000 machines scanned


## Remediations

- Don't let RDP slip out of your network.
    - Better yet, __don't let it go anywhere at all. Just turn it off.__
- Verify `sethc.exe` content
- Verify `sethc.exe` registry entry
- Enable full disk encryption and protect keys


END MODULE 0x09
