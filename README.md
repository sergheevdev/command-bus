# :bus: CommandBus

[![MIT License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://github.com/sergheevdev/command-bus/blob/main/LICENSE)

## Introduction

**CommandBus** is a simple, lightweight and thread-safe, command bus (also known as
a **command dispatcher**). A command bus hands over commands to their handlers, and
may also perform other actions (i.e. validate data, wrap handler in a database 
transaction, provide queueing options, etc).

This library provides a unified entry point for your commands execution, promoting
best software design practices.

### Features
- Thread-safety (for concurrent operations when already serving, i.e. runtime loading of handlers).
- Custom annotations (to ease the handler-command association).
- Highly flexible and extensible library (the client can override default implementations).

### TODO
- Add tests and ensure code coverage (with JUnit and Mockito).
- Add middlewares (for multiple purposes, i.e. logging, units of work, etc).
- Add Spring Boot integration (handler-command provisioning, custom builder).
- Add async and multi-threaded command executors.

### Story

This project started as a coding kata for implementing a really simple command bus,
the problem raised other sub-problems, and the inclusion of fascinating concepts to
be implemented (i.e. heterogeneous containers).

In the process I started to design a more flexible API and I thought it was decent
enough to decide to open source it.

## Getting started

1. Package the library with [maven](https://maven.apache.org/) into a JAR file.
2. Add the packaged **JAR** as a provided dependency to your project.
3. Enjoy your new command bus library!

**NOTE**: make sure you do not include the demo submodule when compiling

## Usage

All the documentation and tutorials about the usage of this command bus is available
at the [wiki page of this repository](https://github.com/sergheevdev/command-bus/wiki).

## License

[MIT](LICENSE) &copy; Serghei Sergheev
