((java-mode . (
               (projectile-project-compilation-cmd . "./gradlew build")
               (projectile-project-compilation-dir . ".")
               (projectile-project-configure-cmd . "./gradlew --recompile-scripts")
               (projectile-project-run-cmd . "./gradlew desktop:run")
               ;(dap-java-java-command . "./gradlew desktop:run -Dorg.gradle.java.home=/usr/lib/jvm/java-8-openjdk-amd64/ --debug-jvm")
               (dap-debug-template-configurations .
                                                  (("Launch DesktopLauncher"
                                                    :type "java"
                                                    :name "Launch DesktopLauncher"
                                                    :request "launch"
                                                    ;:mainClass "com.sa.game.desktop.DesktopLauncher"
                                                    :projectName "my-gdx-game-desktop"
                                                    :cwd "/home/tobbe/source/platformer/"
                                                    )))
               )))

