package org.springframework.cloud.release.internal;

import static org.springframework.cloud.release.internal.SpringCloudConstants.BUILD_ARTIFACT_ID;
import static org.springframework.cloud.release.internal.SpringCloudConstants.CLOUD_DEPENDENCIES_ARTIFACT_ID;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents versions taken out from Spring Cloud Release pom
 *
 * @author Marcin Grzejszczak
 */
class Versions {

	private static final String SPRING_BOOT_PROJECT_NAME = "spring-boot";

	String bootVersion;
	String scBuildVersion;
	Set<Project> projects = new HashSet<>();

	Versions(String bootVersion) {
		this.bootVersion = bootVersion;
		this.projects.add(new Project(SPRING_BOOT_PROJECT_NAME, bootVersion));
	}

	Versions(String scBuildVersion, Set<Project> projects) {
		this.scBuildVersion = scBuildVersion;
		this.projects.add(new Project(BUILD_ARTIFACT_ID, scBuildVersion));
		this.projects.add(new Project(CLOUD_DEPENDENCIES_ARTIFACT_ID, scBuildVersion));
		this.projects.addAll(projects);
	}

	Versions(String bootVersion, String scBuildVersion, Set<Project> projects) {
		this.bootVersion = bootVersion;
		this.scBuildVersion = scBuildVersion;
		this.projects.add(new Project(BUILD_ARTIFACT_ID, scBuildVersion));
		this.projects.add(new Project(CLOUD_DEPENDENCIES_ARTIFACT_ID, scBuildVersion));
		this.projects.addAll(projects);
	}

	String versionForProject(String projectName) {
		return this.projects.stream()
				.filter(project -> project.name.equals(projectName))
				.findFirst()
				.orElse(Project.EMPTY_PROJECT)
				.version;
	}

	boolean shouldBeUpdated(String projectName) {
		return this.projects.stream()
				.anyMatch(project -> project.name.equals(projectName));
	}

	boolean versionAlreadySet(String projectName, String version) {
		String versionForProject = versionForProject(projectName);
		return version.equals(versionForProject);
	}
}

/**
 * @author Marcin Grzejszczak
 */
class Project {

	static Project EMPTY_PROJECT = new Project("", "");

	final String name;
	final String version;

	Project(String name, String version) {
		this.name = name;
		this.version = version;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Project project = (Project) o;
		if (this.name != null ? !this.name.equals(project.name) : project.name != null)
			return false;
		return this.version != null ?
				this.version.equals(project.version) :
				project.version == null;
	}

	@Override public int hashCode() {
		int result = this.name != null ? this.name.hashCode() : 0;
		result = 31 * result + (this.version != null ? this.version.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		return "Project{" + "name='" + this.name + '\'' + ", version='" + this.version + '\'' + '}';
	}
}