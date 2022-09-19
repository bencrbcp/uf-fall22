## 2: IP BLOCKS:

NS: ns.artstailor.com
SOA: ns.artstailor.com (217.70.184.38)

mail.artstailor.com (217.70.184.3)
    nearby: innerouter.artstailor.com (217.70.184.3)

ns.artstailor.com (217.70.184.38)

pdc.artstailor.com (10.70.184.90)
    nearby: books.artstailor.com (10.70.184.91)

pop.artstailor.com (217.70.184.3)

## 3: FIERCE WORDLIST

Located in /usr/lib/python3/dist-packages/fierce/lists/default.txt

Used from the wordlist: ns, mail, pdc, pop

## 4: CeWL

Command run: `cewl http://www.artstailor.com -d 3 -o -w artlist`
then `fierce --domain http://www.artstailor.com --subdomain-file artlist`

Found 3 new hosts:

linuxserver.artstailor.com (10.70.184.38)

costumes.artstailor.com (10.70.184.39)

KEY005-y5An8Bhr0kui0PBIj5pJrQ.atstailor.com (10.70.184.40)

## 5: Fierce methods

using fierce's default wordlist:
    - mail.artstailor.com
    - ns.artstailor.com
    - pdc.artstailor.com
    - pop.artstailor.com
    - the `subdomain_group.add_argument()` function is used here with the command --subdomain-file set to 'default.txt'

using the nearby scan (+-5 in the last octet):
    - innerouter.artstailor.com
    - books.artstailor.com
    - the `p.add_argument()` function is used here with the command --traverse's default value set to 5

using the subdomain file argument:
    - costumes.artstailor.com

using subdomain file AND nearby scan
    - linuxserver.artstailor.com
    - KEY005-y5An8Bhr0kui0PBIj5pJrQ.artstailor.com
    - the `subdomain_group.add_argument()` function is used here with the command --subdomain-file, this time overwriting the default value.

## 6: DNSMAP

DNSMAP only discovers 4 hosts by default:

<include screenshot>


## 7: KEY

found it already

## 8: Vulns:

HTTP enabled.
