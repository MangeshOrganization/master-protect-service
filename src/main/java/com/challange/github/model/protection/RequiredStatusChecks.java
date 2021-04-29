package com.challange.github.model.protection;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"strict",
"contexts"
})
public class RequiredStatusChecks {

@JsonProperty("strict")
private Boolean strict;
@JsonProperty("contexts")
private List<String> contexts = null;

@JsonProperty("strict")
public Boolean getStrict() {
return strict;
}

@JsonProperty("strict")
public void setStrict(Boolean strict) {
this.strict = strict;
}

@JsonProperty("contexts")
public List<String> getContexts() {
return contexts;
}

@JsonProperty("contexts")
public void setContexts(List<String> contexts) {
this.contexts = contexts;
}


}
