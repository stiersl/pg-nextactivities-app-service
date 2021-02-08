package com.pg.plantapps;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class SSLUtil {
	/**
	* For Disabling Security
	*/
	public static void disableSslVerification() {
		try {
				// Create a trust manager that does not validate certificate chains
				TrustManager[] trustAllCerts = new TrustManager[] { new
				X509TrustManager() {
						public X509Certificate[] getAcceptedIssuers() {
								X509Certificate[] empty = {};
								return empty;
						}
						public void checkClientTrusted(X509Certificate[] certs, String authType) {
						}
						public void checkServerTrusted(X509Certificate[] certs, String authType) {
						}
				}};
				
				// Install the all-trusting trust manager
					SSLContext sc = SSLContext.getInstance("SSL");
					sc.init(null, trustAllCerts, new java.security.SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				// Create all-trusting host name verifier
					HostnameVerifier allHostsValid = new HostnameVerifier() {
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}};
				// Install the all-trusting host verifier
						HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
						} catch( NoSuchAlgorithmException e) {
						} catch(KeyManagementException e) {
							
						}
		}
	}
