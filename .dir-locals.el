((java-mode . (
               (projectile-project-compilation-cmd . "./gradlew desktop:build -Dorg.gradle.java.home=/usr/lib/jvm/java-8-openjdk-amd64/")
               (projectile-project-compilation-dir . ".")
               (projectile-project-configure-cmd . "./gradlew --recompile-scripts")
               (projectile-project-run-cmd . "./gradlew desktop:run -Dorg.gradle.java.home=/usr/lib/jvm/java-8-openjdk-amd64/")
               ;(dap-java-java-command . "./gradlew desktop:run -Dorg.gradle.java.home=/usr/lib/jvm/java-8-openjdk-amd64/ --debug-jvm")
               (dap-debug-template-configurations .
                                                  (("Java Run Configuration2"
                                                    :name "Java Run Configuration2"
                                                    :type "java"
                                                    :request "launch"
                                                    :args ""
                                                    ;:cwd "/home/tobias/source/platformer/android/assets"
                                                    :cwd "${workspaceFolder}/android/assets"
                                                    :stopOnEntry :json-false
                                                    :host "localhost"
                                                    :request "launch"
                                                    :modulePaths []
                                                    :classPaths nil
                                                    :projectName nil
                                                    :mainClass nil)))
               )))

