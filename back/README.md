# Yanken Backend

## Requirements

You need to have OpenJDK>=11, you can install it using your
distribution package manager or get the latest JDK from
https://adoptium.net/

Also, you will need to install clojure CLI tools:
https://clojure.org/guides/getting_started#_installation_on_linux
(this is only for developmet, the production bundle only requires JDK)


## Getting Started

```bash
./scripts/start
```

It will start watching on http://localhost:11010


## Generate deployable jar

```bash
clojure -T:build jar
```

This will generate target/backend.jar with everything compiled. You
can start it with `java -jar target/backend.jar`. Look at the
`systemd.service` for a concrete example on how it can be run on the
final server.
