((java-mode . (
               (projectile-project-compilation-cmd . "./gradlew desktop:build -Dorg.gradle.java.home=/usr/lib/jvm/java-8-openjdk-amd64/")
               (projectile-project-compilation-dir . ".")
               (projectile-project-run-cmd . "./gradlew desktop:run -Dorg.gradle.java.home=/usr/lib/jvm/java-8-openjdk-amd64/")
               ;(dap-java-java-command . "./gradlew desktop:run -Dorg.gradle.java.home=/usr/lib/jvm/java-8-openjdk-amd64/ --debug-jvm")
               )))

