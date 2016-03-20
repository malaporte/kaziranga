# Kaziranga, a fence for your Nashorn

Kaziranga is a fork of the Nashorn JavaScript engine that enables sandboxing JavaScript execution and enforcing resource quotas (CPU + memory).

## CAUTION: Here be rhinos!

This project is still very early and doesn't yet provide appropriate protection. In particular, I have yet to be reasonably convinced that it's possible and reasonable to sandbox JS code running under Nashorn, as it exposes quite a lot of API surface.

## How it works

Kaziranga provides both *sandboxing* and *quota enforcement*.

### Sandboxing

Kaziranga provides protection against unknown (and potentially malicious) JavaScript code by restricting access to external APIs beside the ones standard in JavaScript, as well as those that are explicitly made available by the host application. As of now, this boils down to removing access to some particularly nasty Nashorn extensions (ex: `exit`), as well as denying access to Java packages.

### Quota-enforcement

To ensure that JavaScript code doesn't exceed the provided memory and CPU usage quotas, a call to a special check method is injected at the start of all JavaScript blocks, at the bytecode level. This method then looks for a quota registered for the current thread, and then validates if the configured values has been exceeded or not. A runtime exception is raised whenever a quota is exceeded, in order to stop execution of the script.

As a security precaution, any code running in a thread for which no quota has been registered will immediately raise an error. This is to avoid situations where code running JavaScript might end up executing without a quota by mistake; those can be pretty hard to track, especially when Java codes invokes callbacks that might be implemented in JavaScript.

## Packaging

The project compiles as a Maven artifact containing a full version of the Nashorn engine, repackaged under a different namespace.

## TODOS

It is not yet clear to me that my technique covers all the ways a Nashorn thread could exceeded resources. I need to write several unit tests that try to work around the quotas to ensure that it's really airtight.
