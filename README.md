# event-streams-spring-boot-starter
## About
Spring boot starter for the IBM Cloud Event Streams product - with minimal configuration developers will be able
to easily publish and subscribe to kafka topics.

## How to use
1. Add the `event-streams-spring-boot-starter` project as a dependency to your application.
2. Create either application.properties or application.yml in your resources folder.
3. Set the following properties:
- ibm.spring.event-streams.password=${YOUR_IBM_EVENT_STREAMS_PASSWORD}
- spring.kafka.bootstrap-servers=${YOUR_IBM_EVENT_STREAMS_BOOTSTRAP_SERVERS}

Upon starting your application will connect to the IBM Event Streams instance.

## License

This code is licensed under Apache v2.  See the LICENSE file in the root of
the repository.

## Dependencies

For a list of 3rd party dependencies that are used see the POM files of the individual projects.