//void rename_build_file_to_module_name (project) {
//    project.buildFileName = "${project.name}.gradle"
//    project.children.each { child -> rename_build_file_to_module_name(child) }
//}
//
//// Will rename every module's build.gradle file to use its name instead of `build`.
//// E.g. `app/build.gradle` will become `app/app.gradle`
//// The root build.gradle file will remain untouched
//rootProject.children.each { subproject -> rename_build_file_to_module_name(subproject) }

rootProject.name = "neo4j"

include(

//"community:bolt",
"community:codegen",
"community:collections",
//"community:command-line",
"community:common",
//"community:configuration",
//"community:consistency-check",
"community:csv",
//"community:cypher",
//"community:dbms",
//"community:graph-algo",
"community:graphdb-api",
//"community:import-tool",
//"community:index",
"community:io",
//"community:jmx",
//"community:kernel",
//"community:kernel-api",
//"community:licensecheck-config",
"community:logging",
//"community:lucene-index",
//"community:lucene-index-upgrade",
//"community:neo4j",
//"community:neo4j-community",
//"community:neo4j-harness",
//"community:neo4j-slf4j",
"community:primitive-collections",
//"community:procedure-compiler",
"community:resource",
//"community:security",
//"community:server",
//"community:server-api",
//"community:server-plugin-test",
//"community:shell",
//"community:spatial-index",
"community:ssl",
//"community:udc",
"community:unsafe",
"community:values"
)


rootProject.children.onEach(::rename_build_file_to_module_name)
fun rename_build_file_to_module_name(project: ProjectDescriptor) {
    project.buildFileName = "${project.name}.gradle.kts"
    project.children.forEach { child -> rename_build_file_to_module_name(child) }
}
