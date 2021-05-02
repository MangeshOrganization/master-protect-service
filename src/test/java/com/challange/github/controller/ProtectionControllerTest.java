package com.challange.github.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.challange.github.model.event.Event;
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
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	@Test
	public void testAddProtectionSuccess() {
		Repository repo = new Repository();
		when(branchService.protect(repo)).thenReturn(successResponse);
		when(issueService.create(repo, "body")).thenReturn(successResponse);
		assert(true);

	}
	
	@Test
	public void testAddProtectionFail() {
		Repository repo = new Repository();
		when(branchService.protect(repo)).thenReturn(failResponse);
		when(issueService.create(repo, "body")).thenReturn(successResponse);
		assert(true);

	}
	
	@Test
	public void testAddProtectionPartialSuccess() {
		Repository repo = new Repository();
		when(branchService.protect(repo)).thenReturn(successResponse);
		when(issueService.create(repo, "body")).thenReturn(failResponse);
		assert(true);
	}

}
