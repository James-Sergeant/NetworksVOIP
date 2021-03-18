package securityLayer.encryption;

import securityLayer.session.Session;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.SocketException;
import java.util.Random;
/**
 * <h1>RSA Class</h1>
 *<p>This class implements the RSA asymmetric encryption method</p>
 *
 * @author  James Sergeant
 * @version 1.0
 * @since   10/03/2021
 *
 * <h2>Change Log</h2>
 *   - 10/03/2021: Added in all of the functions needed for the algorithm - JS
 *
 *
 * <h2>References: </>
 *  -Explanation of RSA with examples @link https://www.geeksforgeeks.org/java-program-to-implement-the-rsa-algorithm/
 */
public class RSA {
    private BigInteger p=new BigInteger("0"),q=new BigInteger("0"), n, z, d = new BigInteger("0"), e, i, one=new BigInteger("1");

    public KeyPair publicKey;


    /**
     * Generates a new instance of RSA with key pairs.
     */
    public RSA(){
        while(d.intValue() == 0) {
            // Finds two independent probable primes p and q
            findBigPrime(10);
            //n is the product of the two primes.
            n = p.multiply(q);
            //The Euler Phi Function
            z = (p.subtract(one)).multiply(q.subtract(one));
            // finds e, the public key exponent.
            findPublicExponent();
            publicKey = new KeyPair(e, n);
            // finds d, the private exponent.
            findPrivateExponent();
        }
    }

    /**
     * Used to encrypt a message with the other users public key.
     * @param message int: The message to be encrypted.
     * @param keyPair keyPair: The public keypair of the other user.
     * @return Double: The encrypted value of the message.
     */
    public static String encrypt(int message, KeyPair keyPair){
        String cipher = "";
        String s = String.valueOf(message);
        for(int i = 0; i < s.length(); i++){
            int digit = Integer.parseInt(s.substring(i,i+1));

            cipher += (int) ((Math.pow(digit, keyPair.getExponent().intValue())) % keyPair.getN().intValue())+",";
        }
        return cipher;
    }

    /**
     * Decrypts the message with the local private key.
     * @param message double the message to be decrypted.
     * @return BigInteger: The original value of the message.
     */
    public BigInteger decrypt(String message){
        String[] encryptedDigits = message.split(",");
        String plaintext = "";
        for(int i = 0; i < encryptedDigits.length; i++){
            int digit = Integer.parseInt(encryptedDigits[i]);

            // converting float value of c to BigInteger
            BigInteger C = BigDecimal.valueOf(digit).toBigInteger();
            plaintext += (C.pow(d.intValue())).mod(n);
        }
        return new BigInteger(plaintext);
    }

    /**
     * Used to find a pair of big primes.
     * @param size the size of the big primes in bits, 8 bit maximum due to int restrictions.
     */
    private void findBigPrime(int size){
        while (p.equals(q)) {
            // 1st prime number p
            p = BigInteger.probablePrime(size, new Random());

            // 2nd prime number q
            q = BigInteger.probablePrime(size, new Random());
        }
    }

    /**
     * Finds the exponent for the public key e such that: -1 < e < Φ(n).
     */
    private void findPublicExponent(){
        for (e = new BigInteger("2"); e.intValue() < z.intValue(); e = e.add(one)) {
            // e is for public key exponent
            if (gcd(e.intValue(), z.intValue()) == 1) {
                break;
            }
        }
    }

    /**
     * Finds the private key exponent d such that d = (x*Φ(n) + 1) / e, for some value x.
     */
    private void findPrivateExponent(){
        for (i = new BigInteger("0"); i.intValue() <= 9; i = i.add(one)) {
            BigInteger x = (i.multiply(z)).add(one);

            // d is for private key exponent
            if (x.mod(e).intValue() == 0) {
                d = x.divide(e);
                break;
            }
        }
    }

    /**
     * Uses euclid's gcd algorithm
     * @param e int value 1
     * @param z int value 2
     * @return int the gcd of the two values.
     */
    public static int gcd(int e, int z)
    {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);
    }

    /**
     * A class used to store a key pair.
     */
    public static class KeyPair {
        private BigInteger Exponent;
        private BigInteger n;

        /**
         * Creates a new keypair value.
         * @param Exponent int the exponent.
         * @param n int the n value.
         */
        public KeyPair(BigInteger Exponent, BigInteger n){
            this.Exponent = Exponent;
            this.n = n;
        }

        public KeyPair(int Exponent, int n){
            this.Exponent = new BigInteger(Integer.toString(Exponent));
            this.n = new BigInteger(Integer.toString(n));
        }

        /**
         * @return The value of the exponent.
         */
        public BigInteger getExponent() {
            return Exponent;
        }

        /**
         * @return The value of n.
         */
        public BigInteger getN() {
            return n;
        }

        @Override
        public String toString() {
            return "KeyPair{" +
                    "Exponent=" + Exponent +
                    ", n=" + n +
                    '}';
        }
    }

    /**
     * Testing the values.
     * @param args
     */
    public static void main(String[] args) {
        for(int i = 0; i < 100; i++) {
            int msg = XOR.generateSessionKey();//BigInteger.probablePrime(31, new Random()).intValue();
            RSA rsa = new RSA();
            String encryptedMsg = rsa.encrypt(msg, rsa.publicKey);
            int decryptedMsg = rsa.decrypt(encryptedMsg).intValue();

            System.out.println("MESSAGE:   " + msg);
            if(msg != decryptedMsg) {
                System.out.println("P: " + rsa.p);
                System.out.println("Q: " + rsa.q);
                System.out.println("N: " + rsa.n);
                System.out.println("Z: " + rsa.z);
                System.out.println("E: " + rsa.e);
                System.out.println("D: " + rsa.d);
                System.out.println("ENCRYPTED: " + encryptedMsg);
            }
            System.out.println("DECRYPTED: " + rsa.decrypt(encryptedMsg));
        }
    }
}
