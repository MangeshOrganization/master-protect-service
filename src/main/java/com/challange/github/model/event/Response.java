package com.challange.github.model.event;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"message",
"activity",
"status"
})
public class Response {

@JsonProperty("message")
private String messgage;
@JsonProperty("activity")
private String activity;
@JsonProperty("status")
private Integer status;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("message")
public String getMessgage() {
return messgage;
}

@JsonProperty("message")
public void setMessgage(String messgage) {
this.messgage = messgage;
}

@JsonProperty("activity")
public String getActivity() {
return activity;
}

@JsonProperty("activity")
public void setActivity(String activity) {
this.activity = activity;
}

@JsonProperty("status")
public Integer getStatus() {
return status;
}

@JsonProperty("status")
public void setStatus(Integer status) {
this.status = status;
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