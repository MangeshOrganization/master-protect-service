package com.challange.github.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import com.challange.github.model.event.Repository;
import com.challange.github.model.event.Response;
import com.challange.github.model.protection.Protection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author mangesh
 *
 *Service Implementation to Create the Branch Protection 
 *Reads the Configuration from Config Server.
 */
@Component
public class BranchProtectionService {
    @Value ("${GIT_HUB_TOKEN}")
    private String githubToken;
	
    @Value ("${github.apiBaseURL}")
    private String gitHubBaseURL;
    
    @Value ("${accept}")
    private String acceptHeader;
    
    @Autowired Protection protection;

    
    private RestTemplate restTemplate = new RestTemplate();

	public Response protect(Repository repository) {
		
		String url = gitHubBaseURL+"repos/"+repository.getFull_name()+"/branches/"+repository.getDefault_branch()+"/protection";
		
        ObjectMapper Obj = new ObjectMapper();
        ResponseEntity<String> response = null;
        
        Protection prt = buildProtection();
          
        try {
			String body = Obj.writeValueAsString(prt);
			
			
		    HttpHeaders headers = new HttpHeaders();
		    headers.add("User-Agent", "Spring's RestTemplate" ); 
		    headers.add("Authorization", "token "+githubToken );
		    headers.add("Accept", acceptHeader);
		    
		    HttpEntity entity = new HttpEntity(body, headers);
		    
		    response = restTemplate.exchange(
		            url,
		            HttpMethod.PUT,
		            entity,
		            String.class
		    );
		    
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 
		Response res = new Response();
		res.setActivity("Branch Protection");
		if(response != null) {
			res.setMessgage(response.getBody());
			res.setStatus(response.getStatusCodeValue());
		}else {
			res.setMessgage("Internal Server Error");
			res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return res;
	}

	//Nasty Jackson Exception while deserializing prompted me to create copy of Protection Object here - Mainly because
    // of the way Interceptor Classes are bound by Spring Boot while building @Configuration Objects
    
	private Protection buildProtection() {
		Protection p = new Protection();
		
		p.setAllowDeletions(protection.getAllowDeletions());
		p.setAllowForcePushes(protection.getAllowForcePushes());
		p.setEnforceAdmins(protection.getEnforceAdmins());
		p.setRequiredLinearHistory(protection.getRequiredLinearHistory());
		p.setRequiredPullRequestReviews(protection.getRequiredPullRequestReviews());
		p.setRequiredStatusChecks(protection.getRequiredStatusChecks());
		p.setRestrictions(protection.getRestrictions());
		
		return p;

	}
}
