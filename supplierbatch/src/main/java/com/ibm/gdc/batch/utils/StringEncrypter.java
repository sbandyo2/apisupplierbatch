
/*
 *    Contractor Sourcing Application Fixed Price   
 *  
 *    © Copyright IBM Corp. 2008  
 * 
 *
 * FP_Batch/JavaSource/com/ibm/csa/fixedprice/batch/util/StringEncrypter.java,  FP_Batch
 * 
 */
//*****************************************************************************/
//* Change Log:                                                               */
//* Date: june 10, 2008														  */  
//* Description                                                               */  
//*  																		  */			
//* Manish Singh [manising@in.ibm.com]       CSA Fixed Price New Release    */
//*****************************************************************************/

package com.ibm.gdc.batch.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



/**
 * String Encrypter class to encrypt and decrypt password string.
 * This class uses DES scheme.
 * 
 * @verion 0.1
 * @author Manish Singh [manising@in.ibm.com]
 */

public class StringEncrypter {

    private Cipher cipher;

    /**
    * DEFAULT_ENCRYPTION_KEY can be anything but shoud be more then 24
    */
    private final String DEFAULT_ENCRYPTION_KEY = "1234567812345678123456781234567812345678"; //$NON-NLS-1$
    /**
     * DES algorithm is used
     */
    private final String DES_ENCRYPTION_SCHEME = "DES"; //$NON-NLS-1$
    private SecretKeyFactory keyFactory;
    private KeySpec keySpec;

    /**
    * UNICODE_FORMAT
    */
    private final String UNICODE_FORMAT = "UTF8"; //$NON-NLS-1$

    /**
     * StringEncrypter
     *
     * @throws com.ibm.csa.fixedprice.batch.exceptions.BatchException
     * @throws com.ibm.csa.fixedprice.batch.exceptions.BatchException 
     */
    public StringEncrypter() throws BatchException {
        try {

            byte[] keyAsBytes = DEFAULT_ENCRYPTION_KEY.getBytes(UNICODE_FORMAT);
            keySpec = new DESKeySpec(keyAsBytes);
            keyFactory = SecretKeyFactory.getInstance(DES_ENCRYPTION_SCHEME);
            cipher = Cipher.getInstance(DES_ENCRYPTION_SCHEME);
        } catch (InvalidKeyException e) {
            throw new BatchException(e);
        } catch (UnsupportedEncodingException e) {
            throw new BatchException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new BatchException(e);
        } catch (NoSuchPaddingException e) {
            throw new BatchException(e);
        }

    }

    private String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new String(bytes));
        return stringBuffer.toString();
    }

    /**
     * decrypt
     *
     * @param encryptedString
     * @throws com.ibm.csa.fixedprice.batch.exceptions.BatchException
     * @return
     * @throws com.ibm.csa.fixedprice.batch.exceptions.BatchException 
     */
    public String decrypt(String encryptedString) throws BatchException {
        if ((encryptedString == null)
            || (encryptedString.trim().length() <= 0)) {
            throw new IllegalArgumentException("encrypted string was null or empty"); //$NON-NLS-1$
        }

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
            byte[] ciphertext = cipher.doFinal(cleartext);

            return bytes2String(ciphertext);
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    /**
     * encrypt
     *
     * @param unencryptedString
     * @throws com.ibm.csa.fixedprice.batch.exceptions.BatchException
     * @return
     * @throws com.ibm.csa.fixedprice.batch.exceptions.BatchException 
     */
    public String encrypt(String unencryptedString)
        throws BatchException {
        if ((unencryptedString == null)
            || (unencryptedString.trim().length() == 0)) {
            throw new IllegalArgumentException("unencrypted string was null or empty"); //$NON-NLS-1$
        }

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] ciphertext = cipher.doFinal(cleartext);

            BASE64Encoder base64encoder = new BASE64Encoder();
            return base64encoder.encode(ciphertext);
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

}
