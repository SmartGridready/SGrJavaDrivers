# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- uses `java-library` plugin in build.
- added build parameter to use snapshots of SGr dependencies.
- set libraries as `api` dependencies, when they are exposed in interfaces.


## [2.0.0] - 2024-10-08

### Changed

- Refactored according to driver interfaces.
- Connection parameters are given in constructor.


## [1.1.0] - 2024-06-28

### Added

- Initial official release.


## Note about Releases prior to 1.1.0

Releases prior to version 1.1.0 were created in a different package namespace
and were never meant to be used in production.
