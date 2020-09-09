package com.dbs.controller;

import com.dbs.controller.service.CommonService;
import com.dbs.controller.service.CryptoService;
import com.dbs.controller.service.PGPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/common")
@CrossOrigin(origins = "http://localhost:8080")
public class CommonController {

	@Autowired
	private CommonService commonService;
	@Autowired
	private CryptoService cryptoService;
	@Autowired
	private PGPService pgpService;

	@PostMapping("/encryptFile")
	public boolean encryptFile(@RequestParam("file") String file) throws Exception {
		boolean status = cryptoService.escrowEncrypt(file);
		return status;
	}
	@PostMapping("/decryptFile")
	public  boolean decryptFile(@RequestParam("file") String file) throws Exception {
		boolean status = cryptoService.escrowDecrypt(file);
		return status;
	}
	@PostMapping("/validateFile")
	public  String validateFile(@RequestParam("fileName") String fileName) {
		System.out.println("here1111111111");
		return commonService.validateFile(fileName);
	}
	@PostMapping("/getHashValue")
	public  String getHashValue(@RequestParam String text) {

		return commonService.getHashValue(text);
		//return "";
	}

	@PostMapping("/encryptText")
	public  String encryptText(@RequestParam String text) throws Exception {

		return cryptoService.standardEncryptMsg(text);
	}
	@PostMapping("/decryptText")
	public  String decryptText(@RequestParam String text) throws Exception {

		return cryptoService.standardDecryptMsg(text);
	}
	@PostMapping("/decryptPGP")
	public  String decryptPGP(@RequestParam String payload) throws Exception {

		//return pgpService.decrypt(payload).toString();
		return pgpService.decryptText(payload);
	}
	@PostMapping("/encryptPGP")
	public  String encryptPGP(@RequestParam String payload) throws Exception {

		return pgpService.encryptText(payload);
	}

}
