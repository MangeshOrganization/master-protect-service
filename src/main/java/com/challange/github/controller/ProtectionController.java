package com.challange.github.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.challange.github.model.event.Event;
import com.challange.github.model.event.Response;
import com.challange.github.model.event.ServiceResponse;
import com.challange.github.service.BranchProtectionService;
import com.challange.github.service.IssueService;

/**
 * 
 * @author mangesh
 * Controller for Branch Protection Service.
 * Listens on the GitHub Org WebHook - Mainly for event type = "created" 
 * Enables default / configured Branch Protection via BranchProtectionService
 * Enables Notification to User via IssueService
 * 
 * TODO - This could have been just a facade Service - with Branch Protection and Issue Services
 * as independent microservices.
 * Has Basic Response Processing - Based on HTTP Status Only - Not parsing the Response Payload - Can be improved
 * Has Basic Error Handling - Can be improvised
 */
@RestController
@RequestMapping()

public class ProtectionController {
	@Autowired BranchProtectionService protectionService;
	
	@Autowired IssueService issueService;
	
	@RequestMapping(
	        produces = { "application/json" }, 
	        consumes = { "application/json" },
	        method = RequestMethod.POST)
	ResponseEntity<ServiceResponse> addProtection(@RequestBody Event event){
		ServiceResponse res = new ServiceResponse();
		ResponseEntity<ServiceResponse> response;
		
		if(event.getAction().equalsIgnoreCase("created")){
		//First Protect the Branch 
 		Response protectResponse = protectionService.protect(event.getRepository());
		res.getData().add(protectResponse);
		if(!protectResponse.getStatus().equals((HttpStatus.OK).value())) {
			
		    response = new ResponseEntity<ServiceResponse>(res,HttpStatus.resolve(protectResponse.getStatus()));
		    return response;
		}
		
		// Create the Issue and then Form the Response
		String body = protectResponse.getMessgage();
		Response issueResponse = issueService.create(event.getRepository(), body);
		res.getData().add(issueResponse);
		
		 response= new ResponseEntity<ServiceResponse>(res,HttpStatus.OK);
		
		}
		else {
		 response = new ResponseEntity<ServiceResponse>(res,HttpStatus.NOT_IMPLEMENTED);
		}
		
		return response;
	}

}
