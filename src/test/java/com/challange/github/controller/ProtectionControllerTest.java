package com.challange.github.controller;

import static org.mockito.Mockito.when;

import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.challange.github.model.event.Event;
import com.challange.github.model.event.Owner;
import com.challange.github.model.event.Repository;
import com.challange.github.model.event.Response;
import com.challange.github.model.event.ServiceResponse;
import com.challange.github.service.BranchProtectionService;
import com.challange.github.service.IssueService;
@SpringBootTest

public class ProtectionControllerTest {
	@InjectMocks  
	ProtectionController controller;
	
	@Mock
	BranchProtectionService branchService;
	
	@Mock
	IssueService issueService;
	
	Response successResponse = new Response();
	Response failResponse = new Response();
	Event createdEvent = new Event();


	
	@BeforeEach
	void setUp() throws Exception {
		successResponse.setActivity("Branch Protection");
		successResponse.setMessgage("{Payload}");
		successResponse.setStatus(200);
		failResponse.setActivity("Branch Protection");
		failResponse.setMessgage("Failed");
		failResponse.setStatus(404);
		
		createdEvent.setAction("created");
		Repository repository = new Repository();
		repository.setOwner(new Owner());
		repository.setDefault_branch("main");
		repository.setName("main");
		createdEvent.setRepository(repository);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	@Test
	public void testAddProtectionSuccess() {
		Repository repo = new Repository();
		createdEvent.setRepository(repo);
		when(branchService.protect(repo)).thenReturn(successResponse);
		when(issueService.create(repo, "body")).thenReturn(successResponse);
		
		ResponseEntity<ServiceResponse> response =  controller.addProtection(createdEvent);
		assert(response.getStatusCode().is2xxSuccessful());

	}
	
	@Test
	public void testAddProtectionFail() {
		Repository repo = new Repository();
		createdEvent.setRepository(repo);
		when(branchService.protect(repo)).thenReturn(failResponse);
		when(issueService.create(repo, "body")).thenReturn(successResponse);
		ResponseEntity<ServiceResponse> response =  controller.addProtection(createdEvent);
		assert(!response.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void testAddProtectionPartialSuccess() {
		Repository repo = new Repository();
		createdEvent.setRepository(repo);
		when(branchService.protect(repo)).thenReturn(successResponse);
		when(issueService.create(repo, "body")).thenReturn(failResponse);
		ResponseEntity<ServiceResponse> response =  controller.addProtection(createdEvent);
		assert(response.getStatusCode().is2xxSuccessful());
	}
	@Test
	public void testAddProtectionNotSupported() {
		Repository repo = new Repository();
		createdEvent.setAction("deleted");
		createdEvent.setRepository(repo);
		when(branchService.protect(repo)).thenReturn(successResponse);
		when(issueService.create(repo, "body")).thenReturn(failResponse);
		ResponseEntity<ServiceResponse> response =  controller.addProtection(createdEvent);
		assert(response.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED));
	}
}
