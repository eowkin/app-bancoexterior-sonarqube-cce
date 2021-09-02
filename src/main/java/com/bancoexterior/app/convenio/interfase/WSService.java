package com.bancoexterior.app.convenio.interfase;

import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.xml.bind.DatatypeConverter;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.seguridad.MiCipher;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;


@Service
public class WSService implements IWSService{
	
	 private static final Logger LOGGER = LogManager.getLogger(WSService.class);
	 private static final boolean VERIFYSSL = false;
	 private static final HostnameVerifier VERIFIER = NoopHostnameVerifier.INSTANCE;
	 
	 @Value("${${app.ambiente}"+".secret}")
	 private String secret;
	 
	 @Value("${${app.ambiente}"+".iss}")
	 private String iss;
	 
	 @Value("${${app.ambiente}"+".xapikey}")
	 private String xapikey;
	 
	 @Value("${${app.ambiente}"+".seed.monitorfinanciero}")
	 private String sconfigDesKey;
	 
	 private static final String CONTENTTYPE = "Content-Type";
	 
	 private static final String ACCEPTCHARSET = "Accept-Charset";
	 
	 private static final String UTF8 = "UTF-8";
	 
	 private static final String AUTHORIZATION = "Authorization";
	 
	 private static final String VALORXAPIKEY = "x-api-key";
	 
	 private static final String HTTPSTATUSCODEEXCEPTION = "HttpStatusCodeException: %1$s";
	 
	 private static final String RESPONSEBODY = "ResponseBody: %1$s";
	 
	 private static final String EXCEPTIONUNI = "Exception UNI: %1$s";
	 
	 private static final String BEARER = "Bearer ";
	 
	 @Override
	 public WSResponse post(WSRequest request) {
		
		 HttpResponse<String> retorno = null;
		 WSResponse response;
		 
		 String secretDecrypt = MiCipher.decrypt(secret, sconfigDesKey);
		 String issDecrypt = MiCipher.decrypt(iss, sconfigDesKey);
	     String xapikeyDecrypt = MiCipher.decrypt(xapikey, sconfigDesKey);
		 
		 String bearer2 = createJWT(null, issDecrypt, null, new Date(System.currentTimeMillis()+ 900000), secretDecrypt, new Date(System.currentTimeMillis()));
		 try {
			 initUniRest (request.getSocketTimeout(),request.getConnectTimeout() );
			 retorno = Unirest.post(request.getUrl().trim()) 
			  .header(CONTENTTYPE, request.getContenType())
			  .header(ACCEPTCHARSET, UTF8)
			  .header(AUTHORIZATION,BEARER+ bearer2)
			  .header(VALORXAPIKEY,xapikeyDecrypt)
			  .body(request.getBody()).asString();
			  response = new WSResponse(retorno);
			 
			  
		} catch (HttpStatusCodeException e) {
			LOGGER.error(String.format(HTTPSTATUSCODEEXCEPTION ,e));
			LOGGER.error(String.format(RESPONSEBODY ,e.getResponseBodyAsString()));
			response = new WSResponse(e);
		}catch (Exception e) {
			LOGGER.error(String.format(EXCEPTIONUNI ,e));
			response = new WSResponse(e);
		}	

		 return response;
	 }
	 
	 
	 /**
		 * Nombre:                  initUniRest
		 * Descripcion:             Inicializar Objeto Unirest
		 *
		 * @version 1.0
		 * @author Wilmer Vieira
		 * @since 13/10/20
		 */
     
		private void initUniRest (int socketTimeout, int connectTimeout ) {
			Unirest.config().reset();
			Unirest.config()
		    .socketTimeout(socketTimeout)
		    .connectTimeout(connectTimeout)
			.verifySsl(VERIFYSSL)
			.hostnameVerifier(VERIFIER);
			
		}
		
		
		public  String createJWT(String id, String issuer, String subject, Date exp ,String secret, Date issuedat) {
			
			
	        //The JWT signature algorithm we will be using to sign the token
	        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	   
	   
	        //We will sign our JWT with our ApiKey secret
	        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
	        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	   
	        //Let's set the JWT Claims
	        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
	                                    .setId(id)
	                                    .setIssuedAt(issuedat)
	                                    .setSubject(subject)
	                                    .setIssuer(issuer)
	                                    .setExpiration(exp)
	                                    .signWith(signatureAlgorithm, signingKey);
	           
	   
	        //Builds the JWT and serializes it to a compact, URL-safe string
	        return builder.compact();
	    }


		@Override
		public WSResponse put(WSRequest request) {
			HttpResponse<String> retorno = null;
			 WSResponse response;
			 
			 String secretDecrypt = MiCipher.decrypt(secret, sconfigDesKey);
			 String issDecrypt = MiCipher.decrypt(iss, sconfigDesKey);
		     String xapikeyDecrypt = MiCipher.decrypt(xapikey, sconfigDesKey);
			 
		     String bearer2 = createJWT(null, issDecrypt, null, new Date(System.currentTimeMillis()+ 900000), secretDecrypt, new Date(System.currentTimeMillis()));
			 	
			 try {
				 initUniRest (request.getSocketTimeout(),request.getConnectTimeout() );
				 retorno = Unirest.put(request.getUrl().trim()) 
				  .header(CONTENTTYPE, request.getContenType())
				  .header(ACCEPTCHARSET, UTF8)
				  .header(AUTHORIZATION,BEARER+ bearer2)
				  .header(VALORXAPIKEY,xapikeyDecrypt)
				  .body(request.getBody()).asString();
				  response = new WSResponse(retorno);
				 
				  
			} catch (HttpStatusCodeException e) {
				LOGGER.error(String.format(HTTPSTATUSCODEEXCEPTION ,e));
				LOGGER.error(String.format(RESPONSEBODY ,e.getResponseBodyAsString()));
				response = new WSResponse(e);
			}catch (Exception e) {
				LOGGER.error(String.format(EXCEPTIONUNI ,e));
				response = new WSResponse(e);
			}	

			 return response;
		}


		@Override
		public WSResponse delete(WSRequest request) {
			HttpResponse<String> retorno = null;
			 WSResponse response;
			 
			 String secretDecrypt = MiCipher.decrypt(secret, sconfigDesKey);
			 String issDecrypt = MiCipher.decrypt(iss, sconfigDesKey);
		     String xapikeyDecrypt = MiCipher.decrypt(xapikey, sconfigDesKey);
			 
		     String bearer2 = createJWT(null, issDecrypt, null, new Date(System.currentTimeMillis()+ 900000), secretDecrypt, new Date(System.currentTimeMillis()));
			 	
			 try {
				 initUniRest (request.getSocketTimeout(),request.getConnectTimeout() );
				 retorno = Unirest.delete(request.getUrl().trim()) 
				  .header(CONTENTTYPE, request.getContenType())
				  .header(ACCEPTCHARSET, UTF8)
				  .header(AUTHORIZATION,BEARER+ bearer2)
				  .header(VALORXAPIKEY,xapikeyDecrypt)
				  .body(request.getBody()).asString();
				  response = new WSResponse(retorno);
				 
				  
			} catch (HttpStatusCodeException e) {
				LOGGER.error(String.format(HTTPSTATUSCODEEXCEPTION ,e));
				LOGGER.error(String.format(RESPONSEBODY ,e.getResponseBodyAsString()));
				response = new WSResponse(e);
			}catch (Exception e) {
				LOGGER.error(String.format(EXCEPTIONUNI ,e));
				response = new WSResponse(e);
			}	

			 return response;
		}
		
		
}
