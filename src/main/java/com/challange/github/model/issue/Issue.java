package com.challange.github.model.issue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"title",
"body",
"milestone",
"labels",
"assignees"
})
public class Issue {

@JsonProperty("title")
private String title;
@JsonProperty("body")
private String body;
@JsonProperty("milestone")
private String milestone;
@JsonProperty("labels")
private List<String> labels = null;
@JsonProperty("assignees")
private List<String> assignees = null;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("title")
public String getTitle() {
return title;
}

@JsonProperty("title")
public void setTitle(String title) {
this.title = title;
}

@JsonProperty("body")
public String getBody() {
return body;
}

@JsonProperty("body")
public void setBody(String body) {
this.body = body;
}

@JsonProperty("milestone")
public String getMilestone() {
return milestone;
}

@JsonProperty("milestone")
public void setMilestone(String milestone) {
this.milestone = milestone;
}

@JsonProperty("labels")
public List<String> getLabels() {
return labels;
}

@JsonProperty("labels")
public void setLabels(List<String> labels) {
this.labels = labels;
}

@JsonProperty("assignees")
public List<String> getAssignees() {
return assignees;
}

@JsonProperty("assignees")
public void setAssignees(List<String> assignees) {
this.assignees = assignees;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}
