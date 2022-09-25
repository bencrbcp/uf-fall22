# MODULE 0x050 LECTURE 0x130 - BUFFER OVERFLOWS

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
