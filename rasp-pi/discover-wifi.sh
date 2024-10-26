#!/bin/bash

# name of wifi network with out server listening for incoming car telemetry
wifi_name="The Promised LAN"
# path to our script which will send our car telemtry to the server database
telemetry_script="path/to/script"

scan_wifi() {
    # Scan for Wi-Fi networks using 'iwlist' and grep for the wifi network
    networks=$(sudo iwlist wlan0 scan | grep "ESSID:\"$wifi_name\"")
}

# Call the function to scan for Wi-Fi
scan_wifi

# Check if our wifi network is found
if [[ -n "$networks" ]]; then
    echo "Network $wifi_name found! Running Python program..."

    # Run the Python program in the background
    /usr/bin/python3 $telemetry_script &

    # Disown the background process so the script can safely exit
    disown

    echo "Python program started. Exiting the bash script."
else
    echo "Network $wifi_name not found."
fi

# Exit the script
exit 0
