# Kaziranga, a fence around your Nashorn

Kaziranga is a fork of the Nashorn JavaScript engine that enables sandboxing JavaScript execution and enforcing resource quotas (CPU + memory).

WARNING: This project is still very early and doesn't yet provide appropriate protection.

## How it works

As of now, the only modification made to the core Nashorn code is to arrange for calls to a static quota checking method to be injected in the bytecode generated when compiling JavaScript code. This method then looks for a quota registered for the current thread, and then validates if the configured values has been exceeded or not. A runtime exception is raised whenever a quota is exceeded, in order to stop execution of the script.

As a security precaution, any code running in a thread for which no quota has been registered will immediately raise an error. This is to avoid situations where code running JavaScript might end up executing without a quota by mistake; those can be pretty hard to track, especially when Java codes invokes callbacks that might be implemented in JavaScript.

## Packaging

The project compiles as a Maven artifact containing a full version of the Nashorn engine, repackaged under a different namespace.

## TODOS

It is not yet clear to me that my technique covers all the ways a Nashorn thread could exceeded resources. I need to write several unit tests that try to work around the quotas to ensure that it's really airtight.
