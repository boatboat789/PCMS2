var myPassword = "PCMSDISPLAY"

function createEncryptObj(userId) {
  // Define desired object
//  	let valS  = JSON.stringify(userId);       
// 	var salt = CryptoJS.lib.WordArray.random(128/8);
//    var iv = CryptoJS.lib.WordArray.random(128/8);  
//    var key = CryptoJS.PBKDF2(myPassword, salt, { keySize: 128/32, iterations: 100 }); 
//    var ivStr = iv.toString();
//    var keyStr = key.toString();   
//	var encryptedCP = CryptoJS.AES.encrypt(valS, key, { iv: iv });
//	var encryptedCPStr = encryptedCP.toString();
//	var decryptedWA = CryptoJS.AES.decrypt(encryptedCP, key, { iv: iv});
//	var cryptText = encryptedCP.toString(); 
//	console.log('cryptText ' ,cryptText);     ;  
//	 console.log(decryptedWA)  
//	 console.log(keyStr)           
//	 console.log(ivStr)      
//	 console.log(cryptText)          
//	 console.log(encryptedCPStr)       
let text  =  userId ;           
 console.log(userId, myPassword,text)         
var encrypted = CryptoJS.AES.encrypt(text, myPassword);
encrypted = encrypted.toString();
console.log("Cipher text: " + encrypted);  
  var obj = {   
//    name:  valS,
//    key: keyStr,            
//    iv: ivStr,      
//    cryptText: cryptText, 
    encrypted: encrypted  
//    decrypted: decrypted  
  };      
 
  return obj;
} 