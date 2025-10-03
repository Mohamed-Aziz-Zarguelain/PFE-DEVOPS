package com.example.backendbeetrack;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class BackendbeetrackApplication {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider()); // ← Add this

		SpringApplication.run(BackendbeetrackApplication.class, args);
	}


}
