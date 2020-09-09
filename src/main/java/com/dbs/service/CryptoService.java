package com.dbs.service;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoOutputStream;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.MasterKeyProvider;
import com.amazonaws.encryptionsdk.jce.JceMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.amazonaws.encryptionsdk.multi.MultipleProviderFactory;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Collections;
import java.util.Map;
@Service
public class CryptoService {

    private static PublicKey publicEscrowKey;
    private static PrivateKey privateEscrowKey;
    final String keyArn = "";

   /* public static void main(String[] args) throws Exception {
        // This sample generates a new random key for each operation.
        // In practice, you would distribute the public key and save the private key in secure
        // storage.
        generateEscrowKeyPair();

        final String kmsArn = "";
        final String fileName = "text.txt";

       // standardEncrypt(kmsArn, fileName);
        //standardDecrypt(kmsArn, fileName);
        escrowEncrypt(fileName);
        escrowDecrypt(fileName);
    }*/

    private static void standardEncrypt(final String kmsArn, final String fileName) throws Exception {
        // Encrypt with the KMS CMK and the escrowed public key
        // 1. Instantiate the SDK
        final AwsCrypto crypto = new AwsCrypto();

        // 2. Instantiate a KMS master key provider
        final KmsMasterKeyProvider kms = new KmsMasterKeyProvider(kmsArn);

        // 3. Instantiate a JCE master key provider
        // Because the user does not have access to the private escrow key,
        // they pass in "null" for the private key parameter.
        final JceMasterKey escrowPub = JceMasterKey.getInstance(publicEscrowKey, null, "Escrow", "Escrow",
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

        // 4. Combine the providers into a single master key provider
        final MasterKeyProvider<?> provider = MultipleProviderFactory.buildMultiProvider(kms, escrowPub);

        // 5. Encrypt the file
        // To simplify the code, we omit the encryption context. Production code should always
        // use an encryption context. For an example, see the other SDK samples.
        final FileInputStream in = new FileInputStream(fileName);
        final FileOutputStream out = new FileOutputStream(fileName + ".encrypted");
        final CryptoOutputStream<?> encryptingStream = crypto.createEncryptingStream(provider, out);

        IOUtils.copy(in, encryptingStream);
        in.close();
        encryptingStream.close();
    }

    private static void standardDecrypt(final String kmsArn, final String fileName) throws Exception {
        // Decrypt with the KMS CMK and the escrow public key. You can use a combined provider,
        // as shown here, or just the KMS master key provider.

        // 1. Instantiate the SDK
        final AwsCrypto crypto = new AwsCrypto();

        // 2. Instantiate a KMS master key provider
        final KmsMasterKeyProvider kms = new KmsMasterKeyProvider(kmsArn);

        // 3. Instantiate a JCE master key provider
        // Because the user does not have access to the private
        // escrow key, they pass in "null" for the private key parameter.
        final JceMasterKey escrowPub = JceMasterKey.getInstance(publicEscrowKey, null, "Escrow", "Escrow",
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");



        // 4. Combine the providers into a single master key provider
        final MasterKeyProvider<?> provider = MultipleProviderFactory.buildMultiProvider(kms, escrowPub);

        // 5. Decrypt the file
        // To simplify the code, we omit the encryption context. Production code should always
        // use an encryption context. For an example, see the other SDK samples.
        final FileInputStream in = new FileInputStream(fileName + ".encrypted");
        final FileOutputStream out = new FileOutputStream(fileName + ".decrypted");
        final CryptoOutputStream<?> decryptingStream = crypto.createDecryptingStream(provider, out);
        IOUtils.copy(in, decryptingStream);
        in.close();
        decryptingStream.close();
    }
    public  boolean escrowEncrypt(final String fileName) throws Exception {
        // You can decrypt the stream using only the private key.
        // This method does not call AWS KMS.

        // 1. Instantiate the SDK
        final AwsCrypto crypto = new AwsCrypto();

        // 2. Instantiate a JCE master key provider
        // This method call uses the escrowed private key, not null
        final JceMasterKey escrowPriv = JceMasterKey.getInstance(publicEscrowKey, privateEscrowKey, "Escrow", "Escrow",
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

        // 3. Decrypt the file
        // To simplify the code, we omit the encryption context. Production code should always
        // use an encryption context. For an example, see the other SDK samples.
        final FileInputStream in = new FileInputStream(fileName );
       final FileOutputStream out = new FileOutputStream(fileName + ".encrypted");
        final CryptoOutputStream<?> encryptingStream = crypto.createEncryptingStream(escrowPriv, out);
        IOUtils.copy(in, encryptingStream);
        in.close();
        encryptingStream.close();
        return true;
    }
    //Encrypt Text
    public String standardEncryptMsg( String text) throws Exception {
        // You can decrypt the stream using only the private key.
        // This method does not call AWS KMS.
        String str="";
        // 1. Instantiate the SDK
        final AwsCrypto crypto = new AwsCrypto();
        final byte[] EXAMPLE_DATA = text.getBytes(StandardCharsets.UTF_8);
        byte enText[] = new byte[0];
        // 2. Instantiate a JCE master key provider
        // This method call uses the escrowed private key, not null

        final KmsMasterKeyProvider masterKeyProvider = KmsMasterKeyProvider.builder().withKeysForEncryption(keyArn).build();

        // 3. Create an encryption context
        //
        // Most encrypted data should have an associated encryption context
        // to protect integrity. This sample uses placeholder values.
        //
        // For more information see:
        // blogs.aws.amazon.com/security/post/Tx2LZ6WBJJANTNW/How-to-Protect-the-Integrity-of-Your-Encrypted-Data-by-Using-AWS-Key-Management
        final Map<String, String> encryptionContext = Collections.singletonMap("ExampleContextKey", "ExampleContextValue");

        // 4. Encrypt the data
        final CryptoResult<byte[], KmsMasterKey> encryptResult = crypto.encryptData(masterKeyProvider, EXAMPLE_DATA, encryptionContext);
        final byte[] ciphertext = encryptResult.getResult();

        // 3. Decrypt the file
        // To simplify the code, we omit the encryption context. Production code should always
        // use an encryption context. For an example, see the other SDK samples.
        //final FileInputStream in = new FileInputStream(fileName );
        //final FileOutputStream out = new FileOutputStream(fileName + ".encrypted");
       // Map<String, String> encryptionContext =null;
     //   final CryptoOutputStream<?> encryptingStream = crypto.createEncryptingStream(escrowPriv, text,encryptionContext;
        // final byte enText[] = crypto.encryptData(escrowPriv, text,encryptionContext);
       /* IOUtils.copy(in, encryptingStream);
        in.close();
        encryptingStream.close();*/

        return ciphertext.toString();

    }
    public  String standardDecryptMsg(String text) throws Exception {
        // You can decrypt the stream using only the private key.
        // This method does not call AWS KMS.
        final KmsMasterKeyProvider masterKeyProvider = KmsMasterKeyProvider.builder().withKeysForEncryption(keyArn).build();
        final byte[] ciphertext = text.getBytes(StandardCharsets.UTF_8);
        // 1. Instantiate the SDK
        final AwsCrypto crypto = new AwsCrypto();
        byte deText[] = new byte[0];
        // 2. Instantiate a JCE master key provider
        // This method call uses the escrowed private key, not null
        final JceMasterKey escrowPriv = JceMasterKey.getInstance(publicEscrowKey, privateEscrowKey, "Escrow", "Escrow",
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        final Map<String, String> encryptionContext = Collections.singletonMap("ExampleContextKey", "ExampleContextValue");

        // 4. Encrypt the data
      //  final CryptoResult<byte[], KmsMasterKey> encryptResult = crypto.decryptData(masterKeyProvider, EXAMPLE_DATA, encryptionContext);

        final CryptoResult<byte[], KmsMasterKey> decryptResult = crypto.decryptData(masterKeyProvider, ciphertext);

        // 3. Decrypt the file
        // To simplify the code, we omit the encryption context. Production code should always
        // use an encryption context. For an example, see the other SDK samples.
      /*  final FileInputStream in = new FileInputStream(fileName + ".encrypted");
        final FileOutputStream out = new FileOutputStream(fileName + ".deescrowed");
        final CryptoOutputStream<?> decryptingStream = crypto.createDecryptingStream(escrowPriv, out);
        IOUtils.copy(in, decryptingStream);
        in.close();
        decryptingStream.close();*/
        return decryptResult.toString();
    }

    public  boolean escrowDecrypt(String fileName) throws Exception {
        // You can decrypt the stream using only the private key.
        // This method does not call AWS KMS.

        // 1. Instantiate the SDK
        final AwsCrypto crypto = new AwsCrypto();

        // 2. Instantiate a JCE master key provider
        // This method call uses the escrowed private key, not null
        final JceMasterKey escrowPriv = JceMasterKey.getInstance(publicEscrowKey, privateEscrowKey, "Escrow", "Escrow",
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

        // 3. Decrypt the file
        // To simplify the code, we omit the encryption context. Production code should always
        // use an encryption context. For an example, see the other SDK samples.
        final FileInputStream in = new FileInputStream(fileName + ".encrypted");
        final FileOutputStream out = new FileOutputStream(fileName + ".deescrowed");
        final CryptoOutputStream<?> decryptingStream = crypto.createDecryptingStream(escrowPriv, out);
        IOUtils.copy(in, decryptingStream);
        in.close();
        decryptingStream.close();
        return true;
    }

    private static void generateEscrowKeyPair() throws GeneralSecurityException {
        final KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");
        kg.initialize(4096); // Escrow keys should be very strong
        final KeyPair keyPair = kg.generateKeyPair();
        publicEscrowKey = keyPair.getPublic();
        privateEscrowKey = keyPair.getPrivate();

    }
}
