
package RSA;

import java.math.BigInteger;

public class KeyPair
{
    private BigInteger publicKey;
    private BigInteger privateKey;
    private BigInteger modulus;
    private int bitLength;
    
    public KeyPair(int bitLength)
    {
        RSA rsa = new RSA(bitLength);
        publicKey = rsa.getPublicKey();
        privateKey = rsa.getPrivateKey();
        modulus = rsa.getModulus();
        this.bitLength = bitLength;
    }
    
    public KeyPair(BigInteger publicKey, BigInteger modulus, int bitLength)
    {
        setPublicKey(publicKey);
        setModulus(modulus);
        setBitLength(bitLength);
    }
    
    public BigInteger getPublicKey(){ return publicKey; }
    public BigInteger getPrivateKey(){ return privateKey; }
    public BigInteger getModulus(){ return modulus; }
    public int getBitLength(){ return bitLength; }
    
    public final void setPublicKey(BigInteger publicKey){ this.publicKey = publicKey; }
    public final void setPrivateKey(BigInteger privateKey){ this.privateKey = privateKey; }
    public final void setModulus(BigInteger modulus){ this.modulus = modulus; }
    public final void setBitLength(int bitLength){ this.bitLength = bitLength; }
}
