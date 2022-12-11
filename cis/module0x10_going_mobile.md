# MODULE 0x0f LECTURE 0x3d0 - ANDROID DEVICE SECURITY

## Android device history

Started in 2003, acquired in 2005 by Google. Targets of interest are smartphones, tablet computers.

* Software stack including:
    - Operating system (Linux)
    - Framework -- for providing applications
    - Middleware -- for enabling those applications to use the device
    - Applications


## Android software structure

Linux at the lowest layer. Libraries provide access to:

- Device functionality (camera, graphics, sound, GPS, phone network, wifi network)
- System underpinnings (SQLite database w/ no encryption that records all system and application settings and information)
- Dalvik virtual machine
- Application framework
- Applications


## Android security measures

- Application sandboxes Linux user protection scheme to isolate app resources.
    - Unique uid assigned to each process so processes cannot interact.

- Full system encryption using AES 128 (and higher) is available...
    - ...but only user data (not system partition which is read-only) is encrypted

- Address space Layout Randomization (ASLR) and no-execute permission on memory areas are used to avoid stack and heap attacks.

- Permission model controls access to cameras, location, data, telephony, SMS, and MMS.
    - Apps request such permission on installation.
    - Pre 6.0, permission is given as all-or-none. After 6.0, runtime permissions are available.

- All application must be signed with a cert signed by the app developer
    - (but certificate may be self-aligned. no need to be signed by a Certificate Authority)


## Android development tools

__System development kit (SDK)__ for Linux, Mac, Windows. Includes:
- Emulator for ARM (from ARM holdings) mobile devices
    - Can't send phone calls or SMS except to other emulator instances.
    - No bluetooth or camera/video
    - No Google apps(gmail, maps) or telephone carrier apps

__Android Debug Bridge__ and __Dalvik Debug Monitor Server__ also exist.


## Rooting a mobile device

* Rooting (Android nomenclature) or __jailbreaking__ (iOS) refers to an escalation of privilege attack in which you gain access to all phone resources.
    - Normal phone access is less privileged.

Rooting on android devices is arranged by:
- Exploiting a vulnerability in the underlying software of the device, or
- Replacing your entire ROM with one supporting superuser access.


## Benefits and drawbacks of rooting

Benefits
- Full control of the device
- Access to the latest software version (fewer vulns)
- Can run SuperUser.apk or other programs that control access to root privileges for other processes

Drawbacks:
- May brick your device
- Voids manufacturer warranty
- May open device up to new malware attacks


## Android app structure

Android applications (apk files) are just PK files (Phil Katz - developer of zip)

__Contents:__

* Manifest
    - Encoded XML file defining software components, services, activities, permissions, etc.
* Classes.dex
    - Davlik executable code that implements the application
* Broadcast receivers (event handlers)
* Services (that run in the background with no GUI elements)


## Trojan development

A common vector of Android infection is the Trojan app.

* Trojans behave as normally expected but provide a special malware component.

* Development plan:
    - Start with legitimate application
    - Disassemble dex code
    - Decode manifest
    - Include malicious code
    - Assemble dex code
    - Encode the (possibly modified) manifest
    - Sign the final apk


## Android master key vulnerability (pre-2013)

* On installation, Android checks the integrity of the application by using a checksum calculated on the apk dex components
    - These components are unpacked from the PK file one at a time
    - If any redundant components are encountered, they are ignored

* But when the APK execution occurs, the dex components are unpacked in such a way that the most recently unpacked component is used
    - Upshot: You can replace a valid component with a redundant malicious comonent and get privileges associated with the original cert and signature.
    - Patch released by Google in Jul 2013 for this
    - [CVE link](https://www.cvedetails.com/cve/CVE-2013-4787/)


## CVE-2013-6271 Lock (possibly encryption) removal

* Application invokes the updateUnlockMethodAndFinish method in the com.android.settings.ChooseLockGeneric class with the `PASSWORD_QUALITY_UNSPECIFIED` option.
    - Result: Any lock (PIN or password) implemented on your phone is removed
    - Same method was used to update user data encryption settings.
    - It is conjectured that this could be modified but there was no PoC published.


## How do these apps get on your device?

2018 Kapersky survey, percent of users with:

- No passwords on their phone: 52%
- Use their phone for personal email: 57%

* Consider the app SMS replicator (no longer available on Play Store)

* Apps can be downloaded from sites other than the Google Play Store.


## StageFright (CVE-2015-3864)

Result of buffer overflow in mediaserver (that opens media files)

Vectors of attack:
- From an app
- From a URL
- From an MMS message
    - If phone's default setting is to _open MM messages_, a message could be sent to you (no user interaction), exploit vulnerability, then remove message.

Versions 4.0.1 - 5.1.1


Avoid vector 3 by disabling auto-opening of MMS messages


## 2019 Android CVEs

* 35 with severity >= 9
    - Essentially RCE/arbitrary CE/ownership of phone


## Drammer (2016)

* Application of the __Row Hammer__ technique developed by Google Project Zero.
    - This technique uses repeated writes to a row of cells in a memory device in order to get bits in neighboring cells to flip.

* [Drammer](https://github.com/vusec/drammer)
    - Developed by Victor Van der Veen
    - Workable row-hammer exploit for Android devices
    - [Video](https://www.youtube.com/watch?v=x6hL-obNhAw)
    - This exploit could be delivered thru a vuln like Stagefright


## New twist - Easy rates converter

Easy rates converter (which was recently removed from Play Store) would ask the user to install a subsidiary application: "Update Flash Player"

The Update Flash Player app has the following permissions:
- Draw over other apps
- Send and receive SMS and MMS messages

* Even if you subsequently uninstall Easy Rates Converter, Update Flash Player continues to run.
    - If you run various banking apps, it will overlay the page with a copy and steal your credentials.
    - It will also ask you to provide Google credentials for update

* The Easy Rates converter app (on Play Store) does nothing directly malicious.
    - It merely follows a link to install Update Flash Player


## Phishing attacks

Apps imitating legitimate banking apps are linked via email and sent to unsuspecting recipients

* Clients using these applications provide their banking credentials


## Poorly written applications

Many apps are written without encryption

* If users connect via wifi, data can be compromised

* Many applications with hardcoded passwords
    - Discover the passwordon your device and you can use it on another one


## Protections against vulnerabilities

- Upgrade, upgrade, upgrade
- Use a password on your device
- Encrypt your user data
- Don't download software from anywhere except the Play Store or other __approved__ download site
- (Don't?) root your device
- Make software devs write btter code???
    - or at least, don't use poorly written software.


## Android versions in use worldwide

About 8% of users are at OS version 5.1 or earlier (down from 25% in 2019)

Most of these are still vulnerable to stage fright.


## Alternate 2020 statistics show pattern of OS version use

Usage of newest versions fluctuates relatively rapidly.

Old versions are discarded slowly.


## Android versions in use worldwide

[Screenshot of version %s](blob:https://imgur.com/605e6e61-b86b-4403-89bb-4f514cdb47ce)


## Why are thes phones out of data

Providers don't have a reason to update.

Google cannot directly update Android b/c providers customize the OS for their own purposes

__Bottom line:__
- Neither your cell service provider, Google, nor the phone manufacturer lose money if your phone is secure.
- Solution: ?


### QUIZ ACCES CODE
Stanley Clarke - Silly Putty





# MODULE 0x0f LECTURE 0x3e0 - iOS DEVICE SECURITY

## iOS history

Development began in 1980sat NeXT with the NeXTPSTEP operating system.

- OS based on Mach kernel and BSD.

- Objective C programming language adopted.
    - Perhaps largest software base written in Objective C

- Apple acquired NeXT in 1996 converting NeXTPSTEP/OPENSTEP to Mac OS9, then MAC OS X

- 1997 iOS based on NeXTPSTEP/Mac OS X family
    - Followed by iPads and Apple TV

- All iOS devices based on ARM processors


## iOS Security

At initial release, Apple said "no third party apps. Make webapps intead"

* Hackers discovered how to jailbreak the device, and in 2008 Apple released iOS with support for the App Store which hosted third party apps

Early iOS versions:
- All processes ran as superuser
- No sandboxing
- No code signing
- No ASLR
- No Position Independent Execution for libraries or apps
- Few hardware controls


## Security added

- Third party apps run as `mobile` user
- Sandboxing restricts apps to limited set of resources
- Code signature verification was added (Apple signing required) both at load and runtime
- Compile-time options for Xcode support ASLR for OS components and libraries as well as position independent (PIE) code.

* iOS is now one of most secure consumer-grade OS due to business model.
    - Apple controls and profits from App store, security is arguably essential to business model


## Jaibreaking caveats

Typical rooting concerns apply.

* In addition, code signing disabled.
    - Unsigned, malicious code can be executed
    - Malware that attacks jailbroken phones has been found in the wild, e.g. Key Raider 2015, aimed at Chinese users to steal credentials.


## Different types of jailbreaks

* Tethered jailbreak
    - Requires phone to be connected to a computer to boot jailbroken
    - Not persistent across reboots

* Untethered jailbreak
    - Persistent across reboots

* Semi-untethered
    - Most common form nowadays
    - Not persistent across reboots, but can boot into jailbroken state directly from software on phone


## The politics of jailbreaking

Includes colorful characters such as:

* Saurik
    - Developer of Cydia, the program that lets users install software on jailbroken phones
    - Cydia updates have been slowing down, someone needs to take up his role

* Coolstar
    - Electra jailbreak dev

* Tihmstar
    - Apple watch jailbreak dev

* qertyoruiop (Luca Tedesco)
    - Yalu jailbreak dev whose activities led to the ABSE (Anyone But Stefan Esser) license

* axi0mX
    - Created checkm8 boot rom exploit
    - Can exploit A5 thru A11 devices (i.e. iphone 4 thru X) no matter the OS version


## Pangu checkrain jailbreak

* Uses checkm8 to install Cydia substrate

* Supports A5 thru A11


## Unc0ver jailbreak

From pwn20wnd.

* Applies to iOS 11.0-13.5

Installation involves either:
- Cydia impcator
- An AltStore account
- A Mac, Xcode, and the iOS app signer


## Hack other iDevices

* iOS devices have small (practically nonexistent) network profile with no services

* Exploits often aim at client-side vulns, local network access, or physical access.
    - Hak5 wifi pineapple at Starbucks is a typical local network access attack (requires wireless connectivity to be enabled)


## Ancient example: Jailbreakme3.0

Usually used by device owner, but same vuln can be used for remotely rooting another user's device

Exploits two vulns:
    - CVE-2011-0226: FreeType Type 1 Font-handling error allows RCE
    - CVE-2011-0227: IOMobileFrameBuffer bug leads to arbitrary code execution as superuser

* Countermeasures:
    - Don't read PDF docs???
    - Update!


## Malicious(?) apps in the app store: Handy light

* Purportedly a flashlight app (2010)
    - Actually provided tethering capability (banned by Apple for iPhone apps)
    - To initiate tethering, a special sequence of UI interactions was required
    - App store checking (5 guys in a room?) did not identify this functionality and approved the app.


## Malicious(?) apps in the app store: Instastock

Provided real-time stock ticker checking (2011)
Developed by Charlie Miller (iOS hacker, twitter employee, ex-NSA employee, hacked a Jeep)

* Exploited iOS signature validation bypass functionality intended to be used only in Mobile Safari
    - Allows Just In Time (JIT) compilation of Javascript

* InstaStock could connect to a CnC server to receive and execute commands to upload images and contact info from infected devices
    - Charlie Miller was banned from app store


## Malicious software for jailbroken phones

* FlexiSPY (now with many competitors)
    - Eavesdrop on phone calls
    - Collect SMS
    - Turn on mic/camera
    - GPS
    - Remote install/uninstall

* Apple would contend that it is __not__ iPhone malware bc only installable on jailbroken phones


## Wirelurker

* Source of infection: Maiyadi App Store (3rd-party Chinese language App Store for OS X)
    - Applications infected include Sims 3, International Snooker 2012, PES 2014, Bejeweled 3, Angry Birds (over 100k downloads of these alone)

* Attacks targeted iOS devices that connect to the infected OS X device
    - Grabs address book and text messages from target

* Solution?
    - Secure devices that your iPhone trusts


## Malicious software for jailbroken phones part 2

* Apps can't scan filesystem on iOS due to sandbox limitations
    - Such scanning would be possible for jailbroken phones

* Most iOS AV products scan email attachments and downloads
    - Imperium's zANTI addresses other issues like rogue APs and base stations, MitM attacks, etc.


## What kind of software exploits lead to jailbreaks now?

* Best CVEs for jailbreaks are highly cryptic:
    - "does not initialize an unspecified data structure"
    - "leverages an unspecified 'type confusion'"
    - "via a crafted app"
    - "via an app that uses a crafted syscall to interfere with locking"

* Best opportunity for reverse engineering is to capture download of new OS (that advertises a fix)

* Number of exploits is relatively high
    - Number of jailbreaks is getting lower


## Other recent iPhone malware

* Pegasus, a product of NSO group
    - Israeli company purchased by US firm Fransisco Partners in 2014

* Pegasus uses the __Trident__ vulnerabilities, three zero-days
    - CVE 2016-4655: Allows attackers to calculate kernel address in memory
    - CVE 2016-4656: Allows attackers to silently jailbreak phone and install software
    - CVE 2016-4657: Vulnerability in Safari that allows attacker to compromise device on click of a link

* Dicovered when human rights activist Ahmed Mansoor was targeted in a spearphish

* Vulnerabilities probably exploitable from iOS 7 thru 9.3.4


## How are iPhone exploits identified?

- Source code: the iOS kernel code, optimized for ARM, is now available
- As of August 2020?, Apple released jailbroken phones to developers.
    - They will also pay up to $1M for zero-click full-chain kernel execution attacks with persistence





# MODULE 0x0f LECTURE 0x3f0 - Mobile App Pentesting

## The OWASP Mobile Security Testing Guide

First rule: Don't JUST follow the OWASP Mobile Security Testing Guide

* Mobile apps are usually similar to webapps, but they have smaller attack surface that is more secure against injection attacks.
    - They prioritize data protection on the device and the network, and the security of any supporting backend service


## Key areas of mobile app security

Local data storage:
    - Use secure key-storage APIs

Communicating with trusted endpoints:
    - Use secure encrypted channels (TLS)

Authentication and authorization:
    - Use the right frameworks and architectures

Interaction w/ the mobile platform:
    - Use the platform APIs correctly.

Code quality and exploit mitigation
    - Compiler and SDK security features should be leveraged properly

Anti-tampering and anti-reversing
    - Code obfuscation can provide (limited) value


## Testing methods

* Transparent application testing vs. opaque application testing
    - A full-knowledge to zero-knowledge continuum with many variations
    - "Use the source, Luke."

__Static vulnerability analysis__:
* Manual code review
    - May involve search for vulnerability indicators (methods, names, etc.)
    - Capable of identifying business-logic errors
    - Requires expert who knows language and framework
* Automated code analysis
    - Some tools work on the compiled app
    - Others will require source code

__Dynamic vulnerability analysis__:
Verify that runtime security methods provide protection against prevalent types of attack
* Avoid false positives!
    - Automated scanning tools often test the backend for web browser vulnerabilities that cannot actually be exploited in a mobile app


## Mobile Application Security Verification Standard

Verification levels:

__MASVS-L1__
- Adheres to mobile appsec best practices.
- Testing process must be in place to verify security controls

__MASVS-L2__
- Has threat model, and security is an integral part of app architecture and design
- Appropriate for apps such as banking apps that handle sensitive data.

__MASVS-R__
- App is resilient to client-side attacks such as tampering, modding, or reverse engineering to extract sensitive code or data
- Applicable to apps that use __highly__ sensitive and confidential data


### Architecture design and threat modeling requirements
Threat modeling boils down to describing what the threat is, how it can be averted, and how the model can be verified.

* These usually __do not map to test cases.__

Examples:
- All app components are identified and known to be needed.
- Security controls are never enforced only on the client side, but on the respective remote endpoints
- A high-level architecture for the mobile app and all connected remote services has been defined, and security has been addressed in that architecture.
- Data considered sensitive in the context of the mobile app is clearly identified.


### Data storage and privacy requirements
Requirements include:
- No sensitive data should be stored outside of the app container or system credential storage facilities
- No sensitive data is written to application logs
- The keyboard cache is disabled on text inputs that process sensitive data
- No sensitive data is exposed via IPC mechanisms
- No sensitive data is included in backups generated by the mobile OS


### Cryptographic requirements
Requirements include:
- The app does not rely on symmetric cryptography with hardcoded keys as a sole method of encryption
- The app uses proven implementations of cryptographic primitives (do NOT roll your own)
- The app uses cryptographic primitives that are appropriate for the particular use cases, configured with paramaters that adhere to industry best practices
- The app should not use cryptographic protocols or algorithms that are widely considered depracated for security purposes
- The app should not re-use the same cryptographic key for multiple purposes (one key, one purpose)
- All random values are generated using a sufficiently random number generator


### Authentication and session management requirements
* Most apps require login to a remote service.
    - User account sessions must be managed securely

Requirements inckude
- If a remote service is provided on the app, some form of authentication such as username/password authentication, must be performed at the remote endpoint
- If stateful session management is used, the remote endpoint uses randomly generated session identifiers to authenticate client requests without sending the user's credentials
- If stateless, token-based authentication is used, the server provides a token that has been signed using a secure algorithm
- A password policy exists and is enforced at the remote endpoint
- Sessions are invalidated at the remote endpoint after a predefined period of inactivity and access tokens expire
- A second factor of authentication exists at the remote endpoint and the 2FA requirement is consistently enforced


### Network communication requirements
Confidentiality and integrity of information exchanged between the mobile app and remote service endpoints must be preserved.

Level 1 requirements:
- Data is encrypted on the network using TLS. The secure channel is then used consistently throughout the app
- TLS settings are in line w/ current best practices, or as close as possible if the mobile OS does not support recommended standards
- The app verifies X.509 certificates of the remote endpoint when the secure channel is established. Only certificates signed by a trusted CA are accepted.


### Platform interaction requirements
These requirements ensure that the app uses platform APIs in a secure way

These include:
- The app only requests the minimum set of permissions necessary
- All inputs from external sources and the user are validated and, if necessary, sanitized.
    - This includes data received via the UI, IPC mechanisms such as intents, custom URLs and network sources.
- The app does not export sensitive functionality via custom URL schemes, unless these mechanisms are properly protected
- If native methods of the app are exposed to a WebView, verify that the WebView only renders Javascript that is contained within the app package.
- Object deserialization, if any, is implemented using safe serialization APIs


### Code quality and build setting requirements
- The app is signed and provisioned with a valid certificate
- The app has been built in release mode with settings appropriate for a release build (e.g. non-debuggable)
- Debugging symbols have been removed from native binaries
- Debugging code has been removed and the app does not log verbose error messages
- All third party components used by the mobile app, such as libraries and frameworks, are identified and checked for vulns


### Resilience requirements
These requirements provide defense-in-depth. Failure to satisfy them will not in itself introduce a vuln. Satisfying them will reduce likelihood of vulns.

- The app detects and responds to the presence of rooted or jailbroken device either by alerting the user or terminating app.
- The app prevents debugging and/or detects and responds to a debugger being attached. All available debugging protocols must be covered
- The app detects and responds to tampering w/ executable files and critical data within its own sandbox
- The app implements a device binding functionality using a device fingerprint derived from multiple properties unique to the device
- All executable files and libraries belonging to the app are either encrypted on the file level or important code and data segments inside the executables are encrypted or packed


## How do we verify these requirements?

The MSTG (Mobile Security Testing Guide) discusses tools and methods that can be used to verify each of the requirements listed in MASVS

* Convenient checklist spreadsheets available in a number of languages to record each of our findings
    - hachisanju (Doctor Wilson's son, Thomas Wilson) says you can make a lot of money pentesting mobile apps by following OWASP process




# MODULE 0x0f LECTURE 0x400 - APP PENTESTING TOOLS

## Static analysis

* Commercial tools
    - Veracode SAST
    - Checkmarx

* Free tools
    - MobSF (originally  a static analyzer aimed at malware detection, now with support for dynamic analysis)
    - Hundreds of other tools


## The Android emulator

If pentesting from source code, use the emulator as much as possible

It lets you:
    - Have nearly complete control over the code
    - Debug as necessary (thru SDK)
    - Do everything except use SMS, MMS, and make calls. You can modify code (as necessary) to disable use of these features


## Android debug bridge

Distributed with Android SDK.

* Server runs on host computer (Mac, Windows, Linux) and connects either to emulator or to actual device via USB or Wifi

Capabilities:
    - Install/uninstall apps
    - Identify which app has focus (is in the foreground)
    - Open apps
    - View log messages
    - Push/pull/delete files to/from device
    - Clear all app data
    - Input key events
    - Download APK files (for analysis offline)


## Capturing network traffic

Multiple options.

* Rooted phone:
    - tcpdump

* Non-rooted phone:
    - Turn off cell network and configure a web proxy
    - Connect to wifi router and oufit uplink with ethernet tap (such as Hak5 Throwing Star LAN tap). Requires 3 network cables and 2 network interfaces (for upstream and downstream) on the sniffing computer running thru tcpdump/Wireshark. See [this video](https://www.youtube.com/watch?v=3zUsJm3bwGY)


## MitMing web connections for black box testing

With phone whose proxies can be set, or with phones using wifi, can use Burp to MitM web communications.

* What if the app uses SSL/TLS? Options
    - Android: Use Frida (reverse engineering framework) to hook SSL cert checking code (use inside adb -- android debug bridge)
    - iOS: Use SSL killswitch 2 to bypass ssl cert code. Need to use cydia to install this.


## More on interacting with apps using Frida

A unique mobile app reverse engineering tool.

* Free software project
    - Lets you inject scripts into opaque apps to interact w/ them and gather info about their processes, communications, data and behavior.

* Supports Windows, MacOS, GNU/Linux, iOS, Android, and QNX

* Wilson has not used it well, but he is assured that it is all hotness for pentesting mobile apps, providing access to info that is hard to get any other way.


## Rooting Android devices

* Not so necessary for debugging apps due to the availability of adb.
    - If you do need to root, Kingo-root is a popular tool.
    - Generally, you want to root an unlocked phone (region and carrier).
    - Rooting of verizon phones can be especially difficult


## Opening the black box for code testing

* Android
    - Want to convert Dalvik executable to Java source code.
    - Tools include dex2jar (good) and jadx (better).
    - Jadx uses symbols stored in the dex file to restore original variable names and other info (these may be obscured by packers and other obfuscators).

* iOS
    - Need to use Cydia to extract code (Objective C for most native apps).
    - Then you can use the Hopper disassemblerto analyze code


END MODULE 0x10.
