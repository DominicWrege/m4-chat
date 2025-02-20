{
  pkgs ? import <nixpkgs> { },
}:

pkgs.mkShell {
  buildInputs = [
    pkgs.maven
    pkgs.jdk
    pkgs.jetty
  ];

  shellHook = ''
    export JAVA_HOME=${pkgs.jdk}
    echo "Java 21 and Maven are ready to use!"
  '';
}
