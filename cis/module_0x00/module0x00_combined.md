# MODULE 0x000 LECTURE 0x000 - VITAL INFORMATION


## Intructor

Name: Joseph N. Wilson (jnw@ufl.edu)
Associate Professor in the CISE Dept. at UF
Not a professional pentester, but a GIAC certified pentester.o


## Class grades

Quizzes - 25%
Practical Assignments - 40%
    * Lowest grade gets dropped
CTFs - 10%
Final exam - 25%


## Quizzes

Each module has an associated quiz

* Quiz will be open during entire time period allotted to the module
* Each quiz access code is the name of a musical artist
* The access code revealed using PlayPosit popup.


## Practical Assignments

These simulate activities that could form or be an actual penetration test.
    - Randomly assigned teams of three.
    - Students complete most of these problems to whatever level of ocmpletion they're comfortable
    - Then. each individual student will -- perhaps re-do the problem -- write a partial pentest report using the LaTeX template.
    - The report verifies what you completed (vulnerabilities, exploits, etc.)


## Final exams

* 55 questions
    - Equally weighted (do easy ones first)
    - Various keys are available during the semester. Identify them, and decrypt the contained message for a phrase that provides leeway credit on final exam

* Leeway credit
    - For each non-welcome CTF chal, you get 2 leeway points for the final exam (up to a total of 6 out of the 100 points on the exam)


## TA and Professor interaction

* Email
    - jnw@ufl.edu
* Phone:
    - 352-514-2191
* Office hours
    - Zoom hours and links will be on Canvas announcements
* Discord
    - Check for the link to UF CISE PenTest discord


## No required textbooks... but

- Hacker playbook 1, 2, and 3 by Peter Kim


## Closing thoughts

* Key point for me:
    - IF STUCK, LOOK FOR HELP (don't waste time... common habit for me)





# MODULE 0x000 LECTURE 0x010 - Pentesting Models


## The Flaw Hypothesis Model

* Thesis: "In absence of more formal correctness proof techniques, pentests are the most cost-effective method for assessing vulnerabilities"
    - presented by Richard R. Linde

Four stages:
1. Knowledge of system control structure
    - Pentest analysts must understand how users interact with the system
    - Read system  manuals, study literatre concerning system flaws, etc.
2. Flaw hypothesis generation
    - Record probable flaws
3. Flaw hypothesis confirmation
    - Carry out live tests of suspected flaws
    - Demonstrate their existence
4. Flaw hypothesis generalization
    - Deliniate generic weaknesses that can occur in similar scenarios
    - Outline possible remediations

## OSSTM-3.0 (Open Source Securiy Manual)

TL;DR:

Track:
    - What you test, i.e. targets
    - How you test them
    - Types of control discovered
    - What you didn't test
    - Security Test Audit Report (STAR report)

* OSSTM outlines the cross-disciplinary requirements for pentesting
    - Security
    - Law
    - Business Issues
    - Process technology


## Attack Framework: Lockheed Martin Cyber Kill Chain (TM)

A framework provides more structure than a model, allowing to specify instances of each model element

* Based on analysis of adversary campaigns

They say conventional incident response methods fail to mitigate risk by threat actors because of two flawed assumptions:
1. Response should happen after the point of compromise
2. The compromise was the result of a fixable flaw.

**Elements of Cyber Kill Chain:**

* Indicators of intrusion
    - Atomic (IPs, email addresses...)
    - Computed (hash values, regex matches...)
    - Behavioral (sequence of actions taken by attacker)
* Kill Chain Phases:
    - Reconnaisance
    - Weaponization
    - Delivery
    - Exploitation
    - Installation
    - Command & control
    - Action objectives


## PLAYPOSIT QUIZ ACCESS CODE
The James Gang - Funk #49


## Attack Framework: MITRE ATT&CK FRAMEWORK (TM)

Structured approach to describing the ways adversaries attack computer systems

* Very detailed, curated technical info that can be applied in many ways
* Describes methods of attack, detection, mitigation
* Can be used to evaluate and improve an enterprise's security posture

Built in three parts ("TTP"s)
1. Tactics
    - TA0043: Reconnaisance
    - TA0042: Resource Development
    - TA0001: Initial Access
    - TA0002: Execution
    - TA0003: Persistence
    - TA0004: Privesc
    - TA0005: Defense evasion
    - TA0006: Credential access
    - TA0007: Discovery
    - TA0008: Lateral movement
    - TA0009: Collection
    - TA0011: Command & control
    - TA0010: Exfiltration
    - TA0040: Impact (misc. stuff, e.g. deletion of data)
2. Techniques
3. Procedures


* Attack navigator (Webapp UI)
    - https://mitre-attack.github.io/attack-navigator/




# MODULE 0x000 LECTURE 0x020 - Pentesting: What is it Really?

## Class Ethics Agreement

Agree to it. Don't pentest without explicit permission from authorized persons.


## What is a pentest?

Security of a system can be characterized by three properties (CIA Triad):
1. Confidentiality
2. Integrity
3. Availability

Penetration test: An activity in which the security of a program, system, or network is evaluated by attempting to exploit a vulnerability to defeat the CIA triad properties.

Security audit: An attempt to determine if a program, system, or network is vulnerable without specifically attempting to defeat any protective measures and exploiting a vulnerability

**The difference between pentests and audits are the exploitation of vulnerabilities.**


## PHRASE PART 2 (for leeway credit key?):

```
Phrase part 1

wSoJmyhdriCrJwS17NgpdjAYzL+VzC+wWKGgn6cZw3/zurBXDs6FPwwgO7bKj1wmz3sTwPl1CptJMigPSarNSxVpWhqBDj0DGH7hdz+NV5IwzdHkLeyxPrisWZCp/HO7sUFtYG2UQ4X0DIJwWQKSMxx0oGJpDQmp8IOwfS+ZWvpKLV0XwLCE4Q7BNbdpg23ASBfVbfTMzHzbPelPzxLRzloOdTQQvMw1246nk39pcSu793e/o9KjYASFl1i27fneC7WyPBQM8bWT6YaCuzUs9TD3ycdfFfqenj4Pk7WieymTQFMuo+XAfDTToI1bhGONqpNgiVb0Dli+d9hmfea26F5XK9GeWVA2Q7Cm2kJERkVsupqKd9obYLRdE98+YyxxmvfLXexomd2Vf2DBR5HLmjXaA2WRo/NRf5PwUVG2R9yFG8rIQOG+1zYFdTK8D+ErI/JOx1p6YhC/4yFkLePDFxIvhSBcR/nd4X4EXHbfelkFBoVrKR7FEaVeNW1/X9DK9l5JEL8mWwtBWh6jeAAALkKWaXf9OE6YebWZhJ7drSbmJwLSXJaFpAKRXN2bvKXyF6C/PS2+EWUr/no9gD0n0BAvtgyxSJqde40nqBpGQ8n7eg1PPvqKzgGa0EEBAYhBmEccHj9txMrTO+mhXBhCn38n
```


## Scoping

* Arguably one of the most important and often overlooked components of a pentest.
    - Very little on the topic of how to prepare for a test
* Estimate effort based on your experience and the required tests
* Dallas county coalfire debacle makes it clear that scoping is critical
    - [Read here](https://www.desmoinesregister.com/story/news/crime-and-courts/2021/08/01/arrested-coalfire-security-testers-2019-file-dallas-county-iowa-courthouse-lawsuit/5431611001/)

Know:
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





# MODULE 0x000 LECTURE 0x030 - Rules of Engagement

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


## PHRASE PART 2 (for leeway credit key?):
```
Phrase part 2

7SfbCOqtkewQQmnqS78Tagpp9+BfV+8VvNKhQbtc1B7u7VdXmMO1FsBlFD8295XiVzy9U5PoFSdxnlgjpCLOmEbUBHYzmf6EyP7y6C2XxbmHpcRps2ayvV+IeCmWC7spBY24XVN+sX6k+c5HLx+qk7LbudqPzcJwP4hrYjIWwxxy6JbhtgktuzJAoEEXVJqDhRjvCisTvveGKofoIr+k4LtTMPCELyP33xi8/i4eC/VvczUa7051cOD4Kvz81hY2NLzrxoo7wvqxwhz+3RKH0aYcEHhf916aH2Mth858EtDyS1KdBKs5+G/2nE7Rl9jZeM4e1TDwxUr6+t3OFF0mDzDNllwA7hXk+5kytV23W+wvlHVlAU0mwoCQ3yPOWKjVnXbFu6rlzG6ys7whpe5sE+pLp7Hl7Wi9li3WuPl31dCum3rza0KCD9FPDDeYREP5matf1YylNlCVdt1NWM31Ti0AWO+gJXRPjhpM183c9ihDfIZL19PetVT2qlCp1+qu9p6OXGo9ccFg5HRFqkcNRv2tseiyEq4Fuz2IKzXBFL8ghLhP86KiTV1Y5rC/5aQvRhPA5hLTeha6x02/sqA7ecjuPvolruQZ7m440UB+u80GNmC1Gd+lWIMiVJo0nKrPzky/zFxG7kSNeMvgE6QnfuKg
```


## Communication and Contact Info

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
* Location (if on-site)
* Methods of disclosure of sensitive info
    - May be protected by HIPAA
    - Avoid collecting PII
* Evidence handling (use encryption)
* Regular status meetings
* Time of day to test
* Dealing with system shunning
* Permission to test
* Legal considerations
