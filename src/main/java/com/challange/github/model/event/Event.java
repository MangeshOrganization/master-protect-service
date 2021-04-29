package com.challange.github.model.event;

public class Event {
	private String action;
	private Release release;
	private Repository repository;
	    public Event() {
			// TODO Auto-generated constructor stub
		}
	    public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public Release getRelease() {
			return release;
		}
		public void setRelease(Release release) {
			this.release = release;
		}
		public Repository getRepository() {
			return repository;
		}
		public void setRepository(Repository repository) {
			this.repository = repository;
		}
		public Sender getSender() {
			return sender;
		}
		public void setSender(Sender sender) {
			this.sender = sender;
		}
		private Sender sender;
		
		@Override
		public String toString() {
		return this.action;
		}

}
