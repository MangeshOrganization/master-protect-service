package com.challange.github.model.protection;


import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"users",
"teams",
"apps"
})
public class Restrictions {

@JsonProperty("users")
private List<String> users = null;
@JsonProperty("teams")
private List<String> teams = null;
@JsonProperty("apps")
private List<String> apps = null;


@JsonProperty("users")
public List<String> getUsers() {
return users;
}

@JsonProperty("users")
public void setUsers(List<String> users) {
this.users = users;
}

@JsonProperty("teams")
public List<String> getTeams() {
return teams;
}

@JsonProperty("teams")
public void setTeams(List<String> teams) {
this.teams = teams;
}

@JsonProperty("apps")
public List<String> getApps() {
return apps;
}

@JsonProperty("apps")
public void setApps(List<String> apps) {
this.apps = apps;
}


}
