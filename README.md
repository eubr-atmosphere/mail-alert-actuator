### Mail Alert Actuator

- This is a generic TMA actuator that sends an email to a list of pre-configured destination addresses whenever an alarm triggers its execution.

## Usage

- Once the keys are placed in src/resources/keys and the application is running, all is needed to trigger a mail alert is sending a http post request
to /mailActuator/act.
- The request body is gonna be mapped to an ActuatorPayload. Its structure template can be found in src/resources/actuator-payload.json.
