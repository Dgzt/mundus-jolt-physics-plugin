# Jolt Physics Mundus plugin

A [Jolt Physics](https://github.com/jrouwe/JoltPhysics) plugin for [Mundus Editor](https://github.com/JamesTKhan/Mundus).

This plugin works only with master version of Mundus.

## Platform status

This plugin uses [Xpe's version of Jolt Physics bindings](https://github.com/xpenatan/xJolt) version [v5.3.0.2](https://github.com/xpenatan/xJolt/releases/tag/5.3.0.2).


| Emscripten | Windows | Linux | Mac | Android | iOS |
|:----------:|:-------:|:-----:|:---:|:-------:|:---:|
|  ✅         | ✅       |  ✅    |  ✅  | ✅ | ❌ |

* ✅: Have a working build.
* ❌: Build not ready.

## Setup for Editor

You need to build the plugin from source code:

```shell
mvn clean install
```

Then you need to copy `plugin/build/libs/jolt-physics-plugin-0.0.1-SNAPSHOT.jar` jar file into your `.mundus/plugins/` directory.

## Usage in Editor

TODO

## Setup for Runtime

TODO
