plugins {
	java
	id("fabric-loom") version "0.11-SNAPSHOT"
}

val minecraft_version: String by project
val fabric_api_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val mod_version: String by project
val modmenu_version: String by project
val cloth_config_version: String by project

val isPublish = System.getenv("GITHUB_EVENT_NAME") == "release"
val isRelease = System.getenv("BUILD_RELEASE").toBoolean()
val isActions = System.getenv("GITHUB_ACTIONS").toBoolean()
val baseVersion: String = "$mod_version+mc.$minecraft_version"

group = "net.sssubtlety"
version = when {
	isRelease -> baseVersion
	isActions -> "$baseVersion-build.${System.getenv("GITHUB_RUN_NUMBER")}-commit.${System.getenv("GITHUB_SHA").substring(0, 7)}-branch.${System.getenv("GITHUB_REF")?.substring(11)?.replace('/', '.') ?: "unknown"}"
	else -> "$baseVersion-build.local"
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
	withSourcesJar()
}

repositories {
	mavenCentral()
	maven {
		name = "Fabric"
		url = uri("https://maven.fabricmc.net/")
	}
	maven {
		name = "shedaniel"
		url = uri("https://maven.shedaniel.me/")
	}
	maven {
		name = "Terraformers"
		url = uri("https://maven.terraformersmc.com/releases/")
	}
}

dependencies {
	minecraft("com.mojang", "minecraft", minecraft_version)
	mappings("net.fabricmc", "yarn", yarn_mappings, classifier = "v2")
	modImplementation("net.fabricmc", "fabric-loader", loader_version)

	modRuntimeOnly(fabricApi.module("fabric-resource-loader-v0", fabric_api_version))
	modRuntimeOnly(fabricApi.module("fabric-screen-api-v1", fabric_api_version))

	//Config library
	modRuntimeOnly(modCompileOnlyApi("me.shedaniel.cloth", "cloth-config-fabric", cloth_config_version) {
		exclude(group = "net.fabricmc.fabric-api")
	}) {
		exclude(group = "net.fabricmc.fabric-api")
	}
	// Config menu support
	modRuntimeOnly(modCompileOnly("com.terraformersmc", "modmenu", modmenu_version) {
		exclude(group = "net.fabricmc.fabric-api")
	}) {
		exclude(group = "net.fabricmc.fabric-api")
	}
}

tasks {
	withType<JavaCompile> {
		options.encoding = "UTF-8"
	}
	withType<Jar> {
		from("LICENSE")
	}
	processResources {
		val map = mapOf(
			"version" to project.version,
			"project_version" to mod_version,
			"minecraft_required" to project.property("minecraft_required")?.toString(),
			"cloth_config_version" to cloth_config_version,
			"modmenu_version" to modmenu_version
		)
		inputs.properties(map)

		filesMatching("fabric.mod.json") {
			expand(map)
		}
	}
}