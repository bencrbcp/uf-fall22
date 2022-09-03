# MODULE 0x020 LECTURE 0x090 - OSINT

## OSINT - What is it?

* Info gathered from publicly available sources
    - Newspapers
    - Web-based communities
    - Public data
    - Observations and reporting
    - Professional and academic literature
    - Maps
    - Google!

## OSINT is directed!

* Intelligence is aimed at a specific individual or group regarding some specific aim
    - OSINT has a __target__ and a __purpose__
    - Wilson example: tax records of previous homeowners


## How to do OSINT?

* Center for Digital Content of the NSA's book:
    - [Untangling the Web: A Guide to Internet Research](https://governmentattic.org/8docs/UntanglingTheWeb-NSA_2007.pdf)

* Example: Searching web directories
    - If a web directory contains an index file (e.g. index.html, index.php), then that index file provides a document that searching to that directory will yield.
    - If a server allows directory searching, and there is no index in the file, then the web server will show all files in that directory
    - Usually disabled by secure sites
    - Try out `site:ufl.edu "Index of /" "last modified"` ... leads to a class directory listing of an astronomy class with exam results, homework keys, etc.

* Email addresses can reveal account names
* Links can reveal unpublished web directories
* Forums with entries of users from a company can reveal information about hardware / software used by the company and can often reveal poorly configured software
* Job sites (e.g. Indeed) may identify technologies a company is searching for... may give a clue as to what they're weak in
* Open directories on company sites can reveal all sorts of useful intel
* Pastebin, e.g. looking up "ufl.edu password" search on the site can yield many results


## What kind of tools can we use?

* Recon-ng
    - Web recon framework written in python
    - UI similar to Metasploit
    - Numerous sarchable info sources are consulted, some requiring API keys (Bing, Facebook, Twitter, Shodan, virustotal...)
    - Information identified includes domains, hosts, contacts, IP blocks, network ports, map locations, ...
    - Comprehensive, difficult to use

* Hunter.io
    - Online service that provides email address info
    - Can help phishing
    - Can help password crack


## Shodan

* A project from John Matherly
    - Free upgrade for and .edu shodan account used to be (and may still be) available)
    - Comprehensive database of banner messages from all open-internet accessible devices
    - Can tailor searches for specific IP ranges, ports, strings, etc.

* Examples
    - `port:3389 hostname:edu` shows a list of Windows Remote Desktop Protocol (RDP)-enabled internet facing servers for edu domains
    - `'Serial Number:' 'Built:' hostname:edu` Reveals serial number of network devices, e.g. printers, which may have vulns associated with them

##  PHRASE PART 7
```
Phrase part 7

2y0yXo6GFlpHM5O3aGpYKizmbzWaQl8f7KhEUo+IVe5f/qDxo5GQFDnwYhlSYFYGZ41RgNyQLI5yYcKLHKPM/C6ds3IixO/EGYnQ9CJ7QjHbLeOsIk8JR4E9c6bGDQsSji6i7E6ks2dye+nJsyzFR+9RbTGgEB6/9RyH0PWKwUCZ8lAYNPxpucBQjnMGelEz6qca6Qkkcno2pnyoOO2biotz40R1Qds0aIc4742WL2yHDJ2UcuptOgLlJG0C5vTAP4wYuCWr2JLm8/EveHh+Jo9zLcjFP0hZJ5IxysD+FtziKTYZWvwBRhS+ri+cOcEV9iG1URH8Y2q5AvA3gbkhQMVeAjr+rU/FM7nCxc70u+aCoQV0sR95BXiicUjU4EC2BOUjDguOXIYtywpV7x8DY9oeI75sijQ7MK2V/6lQW423i2xxwezBM9gIyW4Puz26qMJAK1qf78iR5nBDKGx0wap56xowEG+/MbTh/YLj8snmYNqK6IGLs89c7i79oR35RSGvVqnad6irknG1CLwxNANEp9BlX5dKDZs+MCe04XXwlMEsSTvMiOqY0Cv4NXonwMtXlYC5R551NDTI8Qf2FiSlYEZ6n4gPpxIfwpPFHge7I54ZiA0EAoZHENQutfRi3vMmN6+RNAvM7NfBosYsssXs
```





# MODULE 0x020 LECTURE 0x0a0 - OSINT example

## James Comey secret twitter revelation

* Gizmodo article
    - FBI issued a "no comment" response

* Comey spoke at National Security Alliance Dinner on March 2017
    - He mentioned he had an Instagram account with probably nine followers
    - Mentioned all followers were relatives or close friends
    - Feinberg identified Brien Comey, basketball player at Kenyon State, as James Comey's son
    - Searched "Brien Comey" -james on twitter to find his account (verified by post about his father being promoted to FBI director)
    - Feinberg then identified Brien Comey's Instgram account
    - At the time, IG would reveal followers of any account to other users
    - One of those was @reinholdniebuhr who had 10 followers
    - Google search revelealed article about Comey's time at the College of William and Mary (wrote a senior thesis on theologian Reinhold Niebuhr)
    - etc.


## QUIZ ACCESS CODE
Heigh Ho (The Dwarfs Marching Song) - Tom Waits


##
