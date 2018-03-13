object Versions {
    val lucene = "5.5.5"
    val mockito = "2.15.0"
    val junit = "4.12"
    val findbugs_annotations = "3.0.1"
    val hamcrest = "1.3"
    val commons_lang = "3.7"
    val commons_codec = "1.11"

    val bouncycastle = "1.59"
    val jetty = "9.4.8.v20171121"
    val scala = "2.11.12"
    val scala_binary = "2.11"
    val asm = "6.0"
    val metrics = "4.0.2"
    val netty = "4.1.17.Final"
}

/*
      <dependency>
        <groupId>org.apache.shiro
        shiro-core
        <version>1.4.0</version>
      </dependency>
      <dependency>
        <groupId>com.github.ben-manes.caffeine
        caffeine</artifactId>
        <version>2.6.1</version>
      </dependency>
      <dependency>
        <groupId>org.slf4j
        slf4j-nop</artifactId>
        <version>1.7.25</version>
      </dependency>
      <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.25</version>
      </dependency>

      <!-- testing -->
      <dependency>
        <groupId>org.neo4j.driver</groupId>
        <artifactId>neo4j-java-driver</artifactId>
        <version>1.4.3</version>
        <scope>test</scope>
      </dependency>
      <dependency>
        <groupId>org.apache.directory.server</groupId>
        <artifactId>apacheds-server-integ</artifactId>
        <version>2.0.0-M21</version>
        <scope>test</scope>
      </dependency>
      <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava-testlib</artifactId>
        <version>23.6-jre</version>
        <scope>test</scope>
      </dependency>

      <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.2</version>
      </dependency>
      <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.2</version>
        <classifier>tests</classifier>
      </dependency>
      <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpcore</artifactId>
        <version>4.4.5</version>
      </dependency>
      <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.25</version>
        <scope>test</scope>
      </dependency>

      <!-- The JUnit-Hamcrest-Mockito combo -->
      <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
      </dependency>

      <dependency>
          <groupId>org.hamcrest</groupId>
          <artifactId>hamcrest-library</artifactId>
          <version>1.3</version>
          <scope>test</scope>
      </dependency>
      <dependency>
        <groupId>org.hamcrest</groupId>
        <artifactId>hamcrest-all</artifactId>
        <version>1.3</version>
        <scope>test</scope>
      </dependency>

      <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>2.13.0</version>
        <scope>test</scope>
      </dependency>

      <!-- scala -->
      <dependency>
        <groupId>org.scala-lang</groupId>
        <artifactId>scala-library</artifactId>
        <version>${scala.version}</version>
      </dependency>
      <dependency>
        <groupId>org.scala-lang</groupId>
        <artifactId>scala-reflect</artifactId>
        <version>${scala.version}</version>
      </dependency>
      <dependency>
        <groupId>org.scala-lang</groupId>
        <artifactId>scalap</artifactId>
        <version>${scala.version}</version>
      </dependency>

      <!-- scala test dependencies -->
      <dependency>
        <groupId>org.scalatest</groupId>
        <artifactId>scalatest_2.11</artifactId>
        <version>2.2.5</version>
        <scope>test</scope>
        <exclusions>
          <exclusion>
            <artifactId>scala-library</artifactId>
            <groupId>org.scala-lang</groupId>
          </exclusion>
          <exclusion>
            <artifactId>scala-reflect</artifactId>
            <groupId>org.scala-lang</groupId>
          </exclusion>
        </exclusions>
      </dependency>
      <dependency>
        <groupId>org.scalautils</groupId>
        <artifactId>scalautils_2.11</artifactId>
        <version>2.1.7</version>
        <scope>test</scope>
        <exclusions>
          <exclusion>
            <artifactId>scala-library</artifactId>
            <groupId>org.scala-lang</groupId>
          </exclusion>
          <exclusion>
            <artifactId>scala-reflect</artifactId>
            <groupId>org.scala-lang</groupId>
          </exclusion>
        </exclusions>
      </dependency>
      <dependency>
        <groupId>org.scalacheck</groupId>
        <artifactId>scalacheck_2.11</artifactId>
        <version>1.12.5</version>
        <scope>test</scope>
      </dependency>
      <dependency>
        <groupId>com.novus</groupId>
        <artifactId>salat-core_2.11</artifactId>
        <version>1.9.9</version>
        <scope>test</scope>
        <exclusions>
          <exclusion>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-library</artifactId>
          </exclusion>
          <exclusion>
            <artifactId>scalap</artifactId>
            <groupId>org.scala-lang</groupId>
          </exclusion>
        </exclusions>
      </dependency>

      <!-- other -->
      <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.2</version>
      </dependency>
      <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.6</version>
      </dependency>
      <dependency>
        <groupId>commons-configuration</groupId>
        <artifactId>commons-configuration</artifactId>
        <version>1.10</version>
        <type>jar</type>
        <scope>compile</scope>
        <exclusions>
          <exclusion>
            <artifactId>commons-digester</artifactId>
            <groupId>commons-digester</groupId>
          </exclusion>
        </exclusions>
      </dependency>
      <!-- Added (directly) to avoid version clash in commons-configuration. -->
      <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-digester3</artifactId>
        <version>3.2</version>
        <type>jar</type>
        <scope>compile</scope>
        <exclusions>
          <exclusion>
            <groupId>commons-beanutils</groupId>
            <artifactId>commons-beanutils</artifactId>
          </exclusion>
        </exclusions>
      </dependency>
      <dependency>
        <groupId>commons-beanutils</groupId>
        <artifactId>commons-beanutils</artifactId>
        <version>1.9.3</version>
        <scope>compile</scope>
      </dependency>
      <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-text</artifactId>
        <version>1.1</version>
      </dependency>
      <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-compress</artifactId>
        <version>1.15</version>
      </dependency>

      <!-- Netty is used by three components: Com, Cluster and Bolt Socket Transport.
           Netty 4 is a significant improvement over Netty 3, in that it introduces
           buffer pooling (among lots of other improvements), and thus we want to move
           to Netty 4 to reduce GC overhead. Netty 4 (io.netty:netty-all) can co-exist
           with Netty 3 (io.netty:netty) because they have different modules and different
           package structure. As such, the two netty dependencies represents the in-between
           state while we move all remaining Netty 3 uses over to Netty 4 -->
      <dependency>
        <groupId>io.netty</groupId>
        <artifactId>netty</artifactId>
        <version>3.9.9.Final</version>
      </dependency>
      <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
      </dependency>
      <dependency>
        <groupId>net.sf.opencsv</groupId>
        <artifactId>opencsv</artifactId>
        <version>2.3</version>
      </dependency>
      <dependency>
        <groupId>com.sun.jersey</groupId>
        <artifactId>jersey-client</artifactId>
        <version>1.19</version>
        <exclusions>
          <exclusion>
            <groupId>javax.ws.rs</groupId>
            <artifactId>jsr311-api</artifactId>
          </exclusion>
        </exclusions>
      </dependency>
      <dependency>
        <groupId>com.sun.jersey</groupId>
        <artifactId>jersey-server</artifactId>
        <version>1.19</version>
        <exclusions>
          <exclusion>
            <groupId>javax.ws.rs</groupId>
            <artifactId>jsr311-api</artifactId>
          </exclusion>
        </exclusions>
      </dependency>
      <dependency>
        <groupId>com.sun.jersey</groupId>
        <artifactId>jersey-servlet</artifactId>
        <version>1.19</version>
        <exclusions>
          <exclusion>
            <groupId>javax.ws.rs</groupId>
            <artifactId>jsr311-api</artifactId>
          </exclusion>
        </exclusions>
      </dependency>
      <dependency>
        <groupId>com.sun.jersey.contribs</groupId>
        <artifactId>jersey-multipart</artifactId>
        <version>1.19</version>
      </dependency>
      <dependency>
        <groupId>org.codehaus.jackson</groupId>
        <artifactId>jackson-core-asl</artifactId>
        <version>1.9.13</version>
      </dependency>
      <dependency>
        <groupId>org.codehaus.jackson</groupId>
        <artifactId>jackson-jaxrs</artifactId>
        <version>1.9.13</version>
      </dependency>
      <dependency>
        <groupId>org.codehaus.jackson</groupId>
        <artifactId>jackson-mapper-asl</artifactId>
        <version>1.9.13</version>
      </dependency>
      <dependency>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-server</artifactId>
        <version>${jetty.version}</version>
      </dependency>
      <dependency>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-webapp</artifactId>
        <version>${jetty.version}</version>
      </dependency>
      <dependency>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-servlet</artifactId>
        <version>${jetty.version}</version>
      </dependency>
      <dependency>
        <groupId>org.eclipse.jetty.websocket</groupId>
        <artifactId>websocket-client</artifactId>
        <version>${jetty.version}</version>
      </dependency>
      <dependency>
        <groupId>org.mozilla</groupId>
        <artifactId>rhino</artifactId>
        <version>1.7R4</version>
      </dependency>
      <dependency>
        <groupId></artifactId>
        <version>${bouncycastle.version}</version>
      </dependency>
      <dependency>
        <groupId>org.bouncycastle</groupId>
        <artifactId>bcpkix-jdk15on</artifactId>
        <version>${bouncycastle.version}</version>
      </dependency>
      <dependency>
        <groupId>org.neo4j.3rdparty.javax.ws.rs</groupId>
        <artifactId>jsr311-api</artifactId>
        <version>1.1.2.r612</version>
      </dependency>
      <dependency>
        <groupId>io.dropwizard.metrics</groupId>
        <artifactId>metrics-core</artifactId>
        <version>${metrics.version}</version>
      </dependency>
      <dependency>
        <groupId>io.dropwizard.metrics</groupId>
        <artifactId>metrics-graphite</artifactId>
        <version>${metrics.version}</version>
        <exclusions>
          <exclusion>
              <groupId>com.rabbitmq</groupId>
              <artifactId>amqp-client</artifactId>
          </exclusion>
        </exclusions>
      </dependency>
      <dependency>
        <groupId>org.jprocesses</groupId>
        <artifactId>jProcesses</artifactId>
        <version>1.6.4</version>
      </dependency>

      <dependency>
        <groupId>com.google.testing.compile</groupId>
        <artifactId>compile-testing</artifactId>
        <version>0.15</version>
        <scope>test</scope>
      </dependency>
 */

object Libs {
    val lucene_analyzers_common = "org.apache.lucene:lucene-analyzers-common:${Versions.lucene}"
    val lucene_core = "org.apache.lucene:lucene-core:${Versions.lucene}"
    val lucene_codecs = "org.apache.lucene:lucene-codecs:${Versions.lucene}"
    val lucene_queryparser = "org.apache.lucene:queryparser:${Versions.lucene}"
    val findbugs_annotations = "com.google.code.findbugs:annotations:${Versions.findbugs_annotations}"
    val commons_lang = "org.apache.commons:commons-lang3:${Versions.commons_lang}"
    val commons_codec = "commons-codec:commons-codec:${Versions.commons_codec}"
    val netty = "io.netty:netty-all:${Versions.netty}"
    val bouncycastle = "org.bouncycastle:bcpkix-jdk15on:${Versions.bouncycastle}"

    val junit = "junit:junit:${Versions.junit}"
    val mockito = "org.mockito:mockito-core:${Versions.mockito}"
    val hamcrest = "org.hamcrest:hamcrest-library:${Versions.hamcrest}"


    val asm = "org.ow2.asm:asm:${Versions.asm}"
    val asm_util = "org.ow2.asm:asm-util:${Versions.asm}"
    val asm_analysis = "org.ow2.asm:asm-analysis:${Versions.asm}"
    val asm_tree = "org.ow2.asm:asm-tree:${Versions.asm}"
}
