# Pi-Drive

RasberryPi based car logging and telemetry software

# Setup

## Equipment
- Raspberry Pi
  - I'm using a model 3. Other models would work fine too.
- [OBD reader](https://www.obdlink.com/products/obdlink-lx/)
  - Cheaper OBD readers exist but I wanted some of the other features this supports. Specifically the raw CAN dump feature.

## Raspberry Pi Setup
I'm going to lightly gloss over the pi setup since this is not intended to be a tutorial on the raspberry pi. If you have any questions though feel free to reach out.

### Create a bootable SD card for your raspberry pi

- The [pi imager](https://www.raspberrypi.com/software/) tool from the raspberry pi foundation makes this pretty easy. I installed raspian lite because the GUI is not needed for this project.
  - Create your user account
  - Configure the pi to connect to your wifi.
  - Configure the pi to enable ssh and setup ssh-keys
  - This can be done through the customize os option in pi imager

### Notes:

These are notes to myself.
- I could have the telemetry script open a socket
  - whenever a new telemetry script is spawned it checks this socket to see if the previous one is still alive
  - if the previous telemtry script is unresponive then the new script will kill the old one and picked up where the old script left off
  - The goal here being to prevent a script crashing from preventing anymore data from being sent to the server
  - This is a later feature / issue to worry about, however
  - For now, our telemetry script will simply check to see if the process already exist and then exit if it doesifco    
