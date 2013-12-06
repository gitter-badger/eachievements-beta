package de.tud.eclipse_achievements.index.entities;

import com.google.common.base.Objects;

public class Achievement {
	
	private String name;
	private String description;
	private String icon;
	
	private boolean isCompleted;
	
	private int requiredCompletion;
	
	private int completionCounter;
	
	public Achievement() {		
		isCompleted = false;
		completionCounter = 0;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public void setMaxCompletion(int maxCompletion) {
		this.requiredCompletion = maxCompletion;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void complete() {
		if(isCompleted) {
			return;
		}
		
		if(completionCounter == requiredCompletion) {
			isCompleted = true;
		} else {
			completionCounter++;
		}
	}
	
	public boolean isCompleted() {
		return isCompleted;
	}
	
	public int getCompletionCounter() {
		return completionCounter;
	}
	
	public int getNumberOfRequiredCompletion() {
		return requiredCompletion;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("name", name)
			.add("description", description)
			.add("icon", icon)
			.add("completed", isCompleted)
			.add("required completion:", requiredCompletion)
			.add("number of completions", completionCounter)
			.toString();
	}
}
