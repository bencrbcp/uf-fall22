# MODULE 0x050 LECTURE 0x140 - BUFFER OVERFLOWS

## Process memory organization

* strycpy() does not know size of buffer in the sample code
    - What happens to data when it goes past that buffer?

* rbp (base pointer) points at the start of the current stack frame

* X64 object code details
    - Addresses occupy 8 bytes
    - Stack pointer register is %rsp
    - Stack grows from high addresses to low
    - Push decrements the stack pointer, pop increments it
    - The base pointer (or frame) is %rbp
    - Up to four integer arguments are passed in registers
    - On call, the return address is pushed onto stack, so after instruction finishes, %rsp points to a location containing ret address
    - On return, the value pointed to b rsp is popped into the instruction pointer (%rip)


## How do we control execution?

* Old school method was to write shellcode into the stack
    - i.e. code to execute /bin/sh
    - Make sure the ret address location is overwritten by an address in the code that will cause its execution

* Many pitfalls and ways to avoid them (Control Flow Integrity/CFI)
    - ASLR (Address Space Layout Randomization)
    - Stack canaries
    - No execute stack pages
    - Now that CFI precautions have made stack smashing attacks difficult, techinques have been developed to defeat them.





# MODULE 0x050 LECTURE 0x150 - CFI

## Program control flow graph

A graph with _nodes_ corresponding to basic blocks or instructions (maximal sequences of non-branching instructions)
and _edges_, corresponding to control-flow transfer instructions e.g. jumps, calls.

Control flow may be:
- Direct: Target instructions are either implicit or provided as part of the instruction
- OR Indirect: Target instructions are provided by data stored in registers or memory locations referenced by the instruction


## Control Flow Integrity

Control Flow Integrity for a program is preserved if the set of possible control flow transfers is limited to those that are **strictly required** for correct program execution.

To preserve CFI, systems typically employ a two-phase process:
1. Analyze (to produce the CFG)
2. Enforcement (to ensure that all branches to be encountered at runtime correspond to all edges in the CFG.

CFI mechanisms usually assume that code integrity so direct branches are assumed to be correct.
Various methods are employed to enforce correspondance of indirect branches to graph edges produced during analysis.


## Axes of comparing CFI methods

1. Performance
    - CFI approaches today are shown to incur ~20% performance penalty)
2. Security
    - want to limit # of indirect branches that could occur incorrectly
    - Average Indirect target Reduction (AIR) is a measure of security improvement.
    - "targets" refers to branch locations for any control flow transfer; we want to lower these to only the ones that are correct
    - Typical AIR numbers are ~99%


## Analysis of CFI methods

* Qualitative
    - Precision in the forward direction
    - Precision in the backward direction
    - Supported control-flow transfer type
    - Reported performance
* Quantitative
    - SPEC CPU2600 Benchmark (compares non CFI vs CFI programs to compare branching properties)


## Reducing the possible transfers

* Most CFI methods operate by reducing the set of possible transfers using the CFG.
    - An equivalence class is the set of valid targets for a given indirect control-flow transfer
    - In the ppt sample, foo() and bar() are part elements of such an equivalence class


## Control flow transfer classes

* Forward:
    - Direct jump
    - Direct call
    - Indirect jump (switch or procedure linkage table)
    - Indirect call (function pointers, vtable dispatch [OOP], send-method dispatch [OOP])

* Backward:
    - Return
    - Complex control-flow (exception handling)


## Existing methods of control flow analysis

* No forward branch validation
* Ad-hoc algorithms and heuristics
* Context and flow-insensitive analysis
* Labeling equivalence classes
* Class heirarchy analysis
* Rapid-type analysis
* Flow-sensitive analysis
* Context-sensitive analysis
* Context AND flow-sensitive analysis
* Dynamic analysis (optimistic)


## QUIZ ACCESS CODE

Bob Dylan - Masters of War


## Existing methods of backward control transfer analysis

* No backward branch validation (bad)
* Labelign equivalence classes
* Shadow stack (maintain program stack that identifies control transfers we expect to see)


## Two basic CFI methods

1. Address Space Layout Randomization (ASLR)
    - Main program Heap and Stack will be placed at random locations in memory
    - Library code may or may not be compiled to support ASLR (if a single component of a program doesn't use ASLR, then ASLR is not really happening)
    - (Non ASLR library code can be targeted)

2. Data Execution Prevention (DEP)
    - Supported by NX (No eXecution enforcement -- provided by CPU)
    - An exception is raised if EIP is set to an address for which the NX bit is set


## What is ROP and why does it hurt so much?

* Early binary exploitation methods would write code into memory, then overwrite an indirect transfer location to transfer control to that code
    - If you can't transfer control to an NX-protected area of memory, then we need ROP

__Return Oriented Programming (ROP)__ employs code that contains indirect returns, overwriting return addresses to employ code that already appears in process memory without modifying instructions

* A ROP **gadget** is a sequence of process instructions that ends with an indirect return or branch

* ROP programming is achieved by assembling a sequence of gadgets to achieve a program's aim, then constructing a series of indirect return points that can be written to the stack to arrange for that sequence of gadgets to be executed (by returning successive gadgets one-at-a-time).

* ROP has been proven to be Turing-complete


## ROP Plan

1. [Find gadgets](https://github.com/JonathanSalwan/ROPgadget)
    - Store gadget addresses (and subsequent arguments to be popped) on the stack, starting at return address location





# MODULE 0x050 LECTURE 0x160 - METASPLOIT INTRO

## What is Metasploit?

Open-source project from 2003 that has, at its center, the metasploit framework.

Has __exploits__ and __payloads__ (script kiddie's dream).

## Running Metasploit

- Initialize metasploit database with `sudo msfdb init`
- `msfconsole`


## Selecting an exploit

* Search by CVE:`msf> search 2007-2447`


## Exploitation plan

1. Find vuln
2. Use exploit that exploits it (e.g. `use multi/samba/usermap_script`)
3. Set options for the exploit
4. Determine what payload to use (`show payloads` to only show applicable ones)
5. `show options` again for payload options, or even `show advanced`
6. Run (`run` or `exploit`)





# MODULE 0x050 LECTURE 0x170 - USING THE METERPRETER

## Meatsploit module rank

* How good an exploit is
    - excellent: will never crash the service
    - great (has default target and will auto-detect or use application-specific return address)
    - good (has default target which is the common case)
    - normal (reliable but depends on a specific version)
    - average (unreliable or difficult to exploit)
    - low (nearly impossible to exploit)
    - manual (unstable or difficult to exploit or of no use unless configured by user)

* Use this as an upper-bound for the actual rating
