package com.challange.github.service;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import com.challange.github.model.event.Owner;
import com.challange.github.model.event.Repository;
import com.challange.github.model.protection.Protection;
@SpringBootTest
class BranchProtectionServiceTest {
    @Value ("${github.apiBaseURL}")
    private String gitHubBaseURL;
    
	@InjectMocks  
	BranchProtectionService service;
	
	@Mock 
	RestTemplate template ;
	@Mock 
	Protection protection;
	
	ResponseEntity<String> successResponse;
	ResponseEntity<String> failResponse;

	@BeforeEach
	void setUp() throws Exception {
		successResponse = new ResponseEntity<String>("Ok", HttpStatus.OK);
		failResponse = new ResponseEntity<String>("Failed", HttpStatus.BAD_REQUEST);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testProtectSuccess() {
		BranchProtectionService ser = mock(BranchProtectionService.class);
		when(ser.buildProtection()).thenReturn( new Protection());
		
		Repository repository = new Repository();
		repository.setOwner(new Owner());
		repository.setDefault_branch("main");
		repository.setFull_name("master/main");
		repository.setName("main");
		when(ser.protect(repository)).thenCallRealMethod();
        ser.setRestTemplate(template);
		when(ser.getRestTemplate()).thenReturn( template);

		String url = gitHubBaseURL+"repos/"+repository.getFull_name()+"/branches/"+repository.getDefault_branch()+"/protection";
		 HttpHeaders headers = new HttpHeaders();
		    headers.add("User-Agent", "Spring's RestTemplate" ); 
		    headers.add("Authorization", "token abcd" );
		    headers.add("Accept", "abcd");
		    
		    HttpEntity entity = new HttpEntity("body", headers);
		    when(template.exchange(
		    		 Matchers.anyString(),
		    		 Matchers.any(HttpMethod.class),
		    		 Matchers.<HttpEntity<?>> any(), 
		    		 Matchers.<Class<String>> any())).thenReturn(successResponse);
		    
		
		;
		assert(ser.protect(repository).getStatus() == 200);

	}
	
	@Test
	void testProtectFailure() {
		BranchProtectionService ser = mock(BranchProtectionService.class);
		when(ser.buildProtection()).thenReturn( new Protection());
		
		Repository repository = new Repository();
		repository.setOwner(new Owner());
		repository.setDefault_branch("main");
		repository.setFull_name("master/main");
		repository.setName("main");
		when(ser.protect(repository)).thenCallRealMethod();
        ser.setRestTemplate(template);
		when(ser.getRestTemplate()).thenReturn( template);

		String url = gitHubBaseURL+"repos/"+repository.getFull_name()+"/branches/"+repository.getDefault_branch()+"/protection";
		 HttpHeaders headers = new HttpHeaders();
		    headers.add("User-Agent", "Spring's RestTemplate" ); 
		    headers.add("Authorization", "token abcd" );
		    headers.add("Accept", "abcd");
		    
		    HttpEntity entity = new HttpEntity("body", headers);
		    when(template.exchange(
		    		 Matchers.anyString(),
		    		 Matchers.any(HttpMethod.class),
		    		 Matchers.<HttpEntity<?>> any(), 
		    		 Matchers.<Class<String>> any())).thenReturn(failResponse);
		    
		
		;
		assert(!(ser.protect(repository).getStatus() == 200));
	}

}
