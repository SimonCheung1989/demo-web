# demo-web
## Public Key & Private Key & Certificates
###Generting CSRs
1. Generate a Private Key and a CSR

        #>openssl req -newkey rsa:2048 -nodes -keyout domain.key -out domain.csr

2. Generate a CSR from an Existing Private Key

        #>openssl req -key domain.key -new -out domain.csr

3. Generate a CSR from an Existing Certificate and Private Key

        #>openssl x509 -in domain.crt -signkey domain.key -x509toreq -out domain.csr

###Generating SSL Certificates

1. Generating SSL Certificates

        #>openssl req -newkey rsa:2048 -nodes -keyout domain.key -x509 -days 365 -out domain.crt

2. Generate a Self-Signed Certificate from an Existing Private Key

        #>openssl req -key domain.key -new -x509 -days 365 -out domain.crt

3. Generate a Self-Signed Certificate from an Existing Private Key and CSR

        #>openssl x509 -signkey domain.key -in domain.csr -req -days 365 -out domain.crt

###View Certificates
1. View CSR Entries

        #>openssl req -text -noout -verify -in domain.csr

2. View Certificate Entries

        #>openssl x509 -text -noout -in domain.crt

3. Verify a Certificate was Signed by a CA

        #>openssl verify -verbose -CAFile ca.crt domain.crt

###Private Keys
1. Create a Private Key

        #>openssl genrsa -des3 -out domain.key 2048

2. Verify a Private Key

        #>openssl rsa -check -in domain.key

3. Verify a Private Key Matches a Certificate and CSR

        #>openssl rsa -noout -modulus -in domain.key | openssl md5
        #>openssl x509 -noout -modulus -in domain.crt | openssl md5
        #>openssl req -noout -modulus -in domain.csr | openssl md5

4. Encrypt a Private Key

        #>openssl rsa -des3 -in unencrypted.key -out encrypted.key

5. Decrypt a Private Key

        #>openssl rsa -in encrypted.key -out decrypted.key

###Public Key
1. Generating Public Key from Private Key

        #>openssl rsa -in domain.key -pubout > publickey.pub

2. Generating Public Key from Certificates

        #>openssl x509 -in domain.crt -pubkey -noout > publickey.pub

###Convert Certificate Formats
1. Convert PEM to DER

        #>openssl x509 -in domain.crt -outform der -out domain.der

2. Convert DER to PEM

        #>openssl x509 -inform der -in domain.der -out domain.crt

3. Convert PEM to PKCS7

        #>openssl crl2pkcs7 -nocrl -certfile domain.crt -certfile ca-chain.crt -out domain.p7b

4. Convert PKCS7 to PEM

        #>openssl pkcs7 -in domain.p7b -print_certs -out domain.crt

5. Convert PEM to PKCS12

        #>openssl pkcs12 -inkey domain.key in domain.crt -export -out domain.pfx

6. Convert PKCS12 to PEM(include public key & private key)

        #>openssl pkcs12 -in domain.pfx -nodes -out domain.combined.pem
    To extract the private key

        #>openssl pkcs12 -in domain.pfx -nocerts -nodes -out domain.key
    To extract the certificate

        #>openssl pkcs12 -in domain.pfx -clcerts -nokeys -out domain.crt

7. Convert PKCS12 to JKS

        #>keytool -importkeystore -srckeystore domain.pfx -srcstoretype pkcs12 -destkeystore domain.jks

8. Convert JKS to PKCS12

        #>keytool -v -importkeystore -srckeystore localhost.jks -destkeystore localhost.jks.pfx -deststoretype PKCS12

9. Convert the Private Key (PKCS1 to PKCS8)

        #>openssl pkcs8 -in domain.key -topk8 -nocrypt -out domain.key.pkcs8

10. Convert the Private Key (PKCS8 to PKCS1)

        #>openssl rsa -in domain.key.pkcs8 -out domain.key.pkcs8.key

###OpenSSL Version

        #>openssl version -a
    