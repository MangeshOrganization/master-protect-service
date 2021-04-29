package com.challange.github.model.protection;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"dismissal_restrictions",
"dismiss_stale_reviews",
"require_code_owner_reviews",
"required_approving_review_count"
})
public class RequiredPullRequestReviews {

@JsonProperty("dismissal_restrictions")
private DismissalRestrictions dismissalRestrictions;
@JsonProperty("dismiss_stale_reviews")
private Boolean dismissStaleReviews;
@JsonProperty("require_code_owner_reviews")
private Boolean requireCodeOwnerReviews;
@JsonProperty("required_approving_review_count")
private Integer requiredApprovingReviewCount;

@JsonProperty("dismissal_restrictions")
public DismissalRestrictions getDismissalRestrictions() {
return dismissalRestrictions;
}

@JsonProperty("dismissal_restrictions")
public void setDismissalRestrictions(DismissalRestrictions dismissalRestrictions) {
this.dismissalRestrictions = dismissalRestrictions;
}

@JsonProperty("dismiss_stale_reviews")
public Boolean getDismissStaleReviews() {
return dismissStaleReviews;
}

@JsonProperty("dismiss_stale_reviews")
public void setDismissStaleReviews(Boolean dismissStaleReviews) {
this.dismissStaleReviews = dismissStaleReviews;
}

@JsonProperty("require_code_owner_reviews")
public Boolean getRequireCodeOwnerReviews() {
return requireCodeOwnerReviews;
}

@JsonProperty("require_code_owner_reviews")
public void setRequireCodeOwnerReviews(Boolean requireCodeOwnerReviews) {
this.requireCodeOwnerReviews = requireCodeOwnerReviews;
}

@JsonProperty("required_approving_review_count")
public Integer getRequiredApprovingReviewCount() {
return requiredApprovingReviewCount;
}

@JsonProperty("required_approving_review_count")
public void setRequiredApprovingReviewCount(Integer requiredApprovingReviewCount) {
this.requiredApprovingReviewCount = requiredApprovingReviewCount;
}


}
