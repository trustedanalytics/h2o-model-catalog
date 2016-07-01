[![Dependency Status](https://www.versioneye.com/user/projects/57236d4cba37ce00350af79b/badge.svg?style=flat)](https://www.versioneye.com/user/projects/57236d4cba37ce00350af79b)

# h2o-model-catalog
Service for exposing h2o data models.

This is the old model-catalog being replaced by new one: https://github.com/intel-data/model-catalog. It originally resided in model-catalog repo in which all it's history is preserved.

# Required services

* **service-exposer** - holds information about service instances

# How to build

It's a Spring Boot application build by maven. All that's needed is a single command to compile, run tests and build a jar:

```
$ mvn verify
```

# How to run locally

```
$ mvn spring-boot:run
```
After starting a local instance, it's available at http://localhost:9994
