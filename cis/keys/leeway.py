from Crypto.Cipher import AES
import argparse

BLOCK_SIZE = 16
PADDING = b'{'

def pad(s):
    return s + ((BLOCK_SIZE - len(s)) % BLOCK_SIZE) * PADDING

def decodeAES(c, e):
    return c.decrypt(e)

passphrase = "The key is here."
key000 = "The key is here."

key = pad(passphrase.encode('utf8'))
iv = b'\n\xfdH\x92\x17\x05\xce\xe7\x84\xd0d\x06X#P+'
assert len(key) == BLOCK_SIZE, 'key has a bad size.'

decrypt_cipher = AES.new(key, AES.MODE_CBC, iv)

plaintext = decodeAES(decrypt_cipher, key000.encode('utf-8'))
print(plaintext.decode('ISO-8859-1'))
