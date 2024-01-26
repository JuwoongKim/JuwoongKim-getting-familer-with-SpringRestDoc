package com.example.gettingfamilerwithspringrestdoc;

public class Member {
	private String name;
	private Integer age;

	public Member(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public Integer getAge() {
		return age;
	}

	public void updateMemberName(String memberName) {
		this.name = memberName;
	}
}

