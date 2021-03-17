package securityLayer.encryption;

import java.math.BigDecimal;
import java.math.BigInteger;
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
    private int p=0,q=0, n, z, d = 0, e, i;

    public final KeyPair publicKey;


    /**
     * Generates a new instance of RSA with key pairs.
     */
    public RSA(){
        // Finds two independent probable primes p and q
        findBigPrime(10);
        //n is the product of the two primes.
        n = p * q;
        //The Euler Phi Function
        z = (p - 1) * (q - 1);
        // finds e, the public key exponent.
        findPublicExponent();
        publicKey = new KeyPair(e,n);
        // finds d, the private exponent.
        findPrivateExponent();
    }

    /**
     * Used to encrypt a message with the other users public key.
     * @param message int: The message to be encrypted.
     * @param keyPair keyPair: The public keypair of the other user.
     * @return Double: The encrypted value of the message.
     */
    public static double encrypt(int message, KeyPair keyPair){
        return (Math.pow(message, keyPair.getExponent())) % keyPair.getN();
    }

    /**
     * Decrypts the message with the local private key.
     * @param message double the message to be decrypted.
     * @return BigInteger: The original value of the message.
     */
    public BigInteger decrypt(double message){
        // converting int value of n to BigInteger
        BigInteger N = BigInteger.valueOf(n);
        // converting float value of c to BigInteger
        BigInteger C = BigDecimal.valueOf(message).toBigInteger();
        return  (C.pow(d)).mod(N);
    }

    /**
     * Used to find a pair of big primes.
     * @param size the size of the big primes in bits, 8 bit maximum due to int restrictions.
     */
    private void findBigPrime(int size){
        while (p == q) {
            // 1st prime number p
            p = BigInteger.probablePrime(size, new Random()).intValue();

            // 2nd prime number q
            q = BigInteger.probablePrime(2*size/3, new Random()).intValue();
        }
    }

    /**
     * Finds the exponent for the public key e such that: -1 < e < Φ(n).
     */
    private void findPublicExponent(){
        for (e = 2; e < z; e++) {

            // e is for public key exponent
            if (gcd(e, z) == 1) {
                break;
            }
        }
    }

    /**
     * Finds the private key exponent d such that d = (x*Φ(n) + 1) / e, for some value x.
     */
    private void findPrivateExponent(){
        for (i = 0; i <= 9; i++) {
            int x = 1 + (i * z);

            // d is for private key exponent
            if (x % e == 0) {
                d = x / e;
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
        private int Exponent;
        private int n;

        /**
         * Creates a new keypair value.
         * @param Exponent int the exponent.
         * @param n int the n value.
         */
        public KeyPair(int Exponent, int n){
            this.Exponent = Exponent;
            this.n = n;
        }

        /**
         * @return The value of the exponent.
         */
        public int getExponent() {
            return Exponent;
        }

        /**
         * @return The value of n.
         */
        public int getN() {
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
        int msg = BigInteger.probablePrime(8, new Random()).intValue();;
        System.out.println(msg);
        RSA rsa = new RSA();
        System.out.println("P: "+rsa.p);
        System.out.println("Q: "+rsa.q);
        System.out.println(Math.abs(rsa.p-rsa.q));
        double encryptedMsg = rsa.encrypt(msg,rsa.publicKey);
        System.out.println(encryptedMsg);
        System.out.println(rsa.decrypt(encryptedMsg));

    }
}
