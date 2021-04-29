package com.challange.github.controller;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.challange.github.service.BranchProtectionService;
import com.challange.github.service.IssueService;
public class ProtectionControllerTest {
	@InjectMocks  
	ProtectionController controller;
	
	@Mock
	BranchProtectionService branchService;
	
	@Mock
	IssueService issueService;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	@Test
	public void testAddProtectionSuccess() {
		assert(true);

	}
	
	@Test
	public void testAddProtectionFail() {
		assert(true);

	}
	
	@Test
	public void testAddProtectionPartialSuccess() {
		assert(true);

	}

}
