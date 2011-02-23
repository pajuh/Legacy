VERSION_NUMBER = "0.0.1"
GROUP = "Legacy"

repositories.remote << "http://www.ibiblio.org/maven2/"
repositories.remote << "http://maven.laughingpanda.org/maven2/"

desc "The Legacy project"
define "Legacy" do
  eclipse.natures :java
  project.version = VERSION_NUMBER
  project.group = GROUP
  package :jar
  compile.with 'javassist:javassist:jar:3.1'
  test.with ['junit:junit:jar:4.8.2', 'org.jdave:jdave-junit4:jar:1.1', 'org.jdave:jdave-core:jar:1.1', 'cglib:cglib-nodep:jar:2.1', 'org.jmock:jmock:jar:2.5.1', 'org.jmock:jmock-legacy:jar:2.5.1', 'org.objenesis:objenesis:jar:1.0']
end
