## How to generate Java keystore with self-signed certificate

```bash
# generate CA cert private key
openssl genrsa -des3 -out ca-key.pem 2048

# generate CA cert
openssl req -new -key ca-key.pem -x509 -days 1000 -out ca-cert.pem -config openssl.cnf

# generate server cert pk
openssl genrsa -des3 -out server-key.pem 2048

# create csr
openssl req -new -config csr.cnf -key server-key.pem -out sr.csr

# create server cert
openssl x509 -req -days 365 -in sr.csr -CA ca-cert.pem -CAkey ca-key.pem -CAcreateserial -out server-cert.pem -extfile v3.ext

# convert pem to pkcs
openssl pkcs12 -export -in server-cert.pem -inkey server-key.pem  -out server.p12

# import pkcs12 to jks
keytool -importkeystore -srckeystore server.p12 -srcstoretype PKCS12 -destkeystore server.jks -deststoretype jks
```