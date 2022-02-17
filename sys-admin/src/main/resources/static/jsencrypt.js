//import JSEncrypt from 'common/encrypt/jsencrypt';

// rac加密
function encrypt(word) {
    var publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9UcbGHLrLGbX1HA1BP8jZzMkXdC2yNLmmwfKq2IJnJ5oG9gKKApkIvzDr9EBVNWh4UKYW+uXoIyRUg3iKjS/d+lu16AJ/Yz9z5TJM3KM6AH5/kMXj1XycazZk8PsgikNsEyIOp0q5DvDgOq0vDqtmh5IXiRWc1B6GWvLxj+BnAwIDAQAB";
    const encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKey);
    let password = encrypt.encrypt(word);// rac加密后的字符串
    return password
}