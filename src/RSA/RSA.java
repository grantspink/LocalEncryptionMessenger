
package RSA;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA
{
    private final BigInteger modulus;
    private final BigInteger privatekey;
    private final BigInteger publickey;
    
    public RSA(int bitLength)
    {
        SecureRandom r = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength, r);
        BigInteger q = BigInteger.probablePrime(bitLength, r);
        this.modulus = p.multiply(q);
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        this.publickey = BigInteger.probablePrime(bitLength/2, r); 
        this.privatekey = this.publickey.modInverse(m);
    }
    
    public BigInteger getPrivateKey(){ return privatekey; }
    public BigInteger getPublicKey(){ return publickey; }
    public BigInteger getModulus(){ return modulus; }
    
    public static BigInteger crypt(BigInteger plaintext, BigInteger key, BigInteger modulus)
    {
        return plaintext.modPow(key, modulus);
    }
}
