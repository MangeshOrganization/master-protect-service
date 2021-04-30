package com.challange.github.service;

import java.util.Arrays;

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
import com.challange.github.model.issue.Issue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service to Create the Issue. This can be better designed with Interface
 * Service and Impl Service This can be totally different Microservice In Order
 * to quickly turn around the Challange - All the pieces were put together.
 * 
 * @author mangesh
 *
 */
@Component
public class IssueService {

	@Value("${GIT_HUB_TOKEN}")
	private String githubToken;

	@Value("${github.apiBaseURL}")
	private String gitHubBaseURL;

	@Value("${accept}")
	private String acceptHeader;

	private RestTemplate restTemplate = new RestTemplate();

	public Response create(Repository repository, String body) {

		Issue issue = new Issue();
		body = "@" + repository.getOwner().getLogin() + " Following Branch Protection has been added to Default Branch \n`" + body +"`";
		issue.setBody(body);
		issue.setTitle("Main Branch Protection Applied - Review");
		issue.setLabels(Arrays.asList("BranchProtection"));
		issue.setAssignees(Arrays.asList(repository.getOwner().getLogin()));

		String url = gitHubBaseURL + "repos/" + repository.getFull_name() + "/issues";

		ObjectMapper Obj = new ObjectMapper();
		ResponseEntity<String> response = null;

		try {
			String issueJSon = Obj.writeValueAsString(issue);

			HttpHeaders headers = new HttpHeaders();
			headers.add("User-Agent", "Spring's RestTemplate");
			headers.add("Authorization", "token " + githubToken);
			headers.add("Accept", acceptHeader);

			HttpEntity entity = new HttpEntity(issueJSon, headers);

			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Response res = new Response();
		res.setActivity("Issue Creation");
		if (response != null) {
			res.setMessgage(response.getBody());
			res.setStatus(response.getStatusCodeValue());
		} else {
			res.setMessgage("Internal Server Error");
			res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return res;
	}
}
