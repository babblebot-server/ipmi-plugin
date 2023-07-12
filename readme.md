    

# Configuration Required

<img src="https://avatars.githubusercontent.com/u/138989349" alt="Image" height="100" />

## Required Application.properties

### ipmi.path
Specify the absolute path to the ipmitool executable on your machine. Make sure to escape backslashes as shown in the example above.


Windows example
```properties
ipmi.path=C:\\\ipmitool_1.8.18-dellemc_p001\\\ipmitool.exe
```

### ipmi.ip
Enter the IP address of the IPMI interface that you wish to connect to. Replace 192.168.1.75 with the appropriate IP address.

```properties
ipmi.ip=192.168.1.0
```

### ipmi.username
Provide the username for authenticating with the IPMI interface.

```properties
ipmi.username=root
```

### ipmi.password
Provide the password for authenticating with the IPMI interface.

```properties
ipmi.password=calvin
```


### ipmi.authUsers
Discord user id of the people you want to have access

```properties
ipmi.authUsers=124239574951002113,124239574951002113,124239574951002113,124239574951002113
```

## Disclaimer
The software is provided "as is" without any warranty or guarantee of compatibility with IPMI controllers other than the Dell R710. By using the software, you waive any legal claims for faults and agree to use it at your own risk. The developer/manufacturer does not provide technical support, but you may create an issue on GitHub for assistance. Please note that such communication should not be considered as formal technical support and the developer/manufacturer shall not be held liable for any damages or issues arising from the software's use.
